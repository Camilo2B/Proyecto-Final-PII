package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthenticationService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Controlador para la vista de transacciones
 */
public class TransaccionesController {
    
    @FXML
    private Label lblNombreUsuario;
    
    @FXML
    private Label lblSaldo;
    
    @FXML
    private Button btnDashboard;
    
    @FXML
    private Button btnTransacciones;
    
    @FXML
    private Button btnPresupuestos;
    
    @FXML
    private Button btnCuentas;
    
    @FXML
    private Button btnCategorias;
    
    @FXML
    private Button btnCerrarSesion;
    
    @FXML
    private ComboBox<TipoTransaccion> cmbTipoTransaccion;
    
    @FXML
    private Button btnFiltrar;
    
    @FXML
    private Button btnLimpiarFiltro;
    
    @FXML
    private TableView<TransaccionFactory> tblTransacciones;
    
    @FXML
    private TableColumn<TransaccionFactory, String> colFecha;
    
    @FXML
    private TableColumn<TransaccionFactory, String> colTipo;
    
    @FXML
    private TableColumn<TransaccionFactory, Double> colMonto;
    
    @FXML
    private TableColumn<TransaccionFactory, String> colDescripcion;
    
    @FXML
    private TableColumn<TransaccionFactory, String> colCategoria;
    
    @FXML
    private Button btnDeposito;
    
    @FXML
    private Button btnRetiro;
    
    @FXML
    private Button btnTransferencia;
    
    @FXML
    private Label lblTipoTransaccion;
    
    @FXML
    private TextField txtMonto;
    
    @FXML
    private ComboBox<Cuenta> cmbCuentaOrigen;
    
    @FXML
    private Label lblCuentaDestino;
    
    @FXML
    private ComboBox<Cuenta> cmbCuentaDestino;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private Button btnRealizarTransaccion;
    
    private SceneController sceneController;
    private AuthenticationService authService;
    private BilleteraService billeteraService;
    private Usuario usuarioActual;
    private TipoTransaccion tipoTransaccionSeleccionado;
    private ObservableList<TransaccionFactory> transaccionesOriginales;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        sceneController = SceneController.getInstance();
        authService = AuthenticationService.getInstance();
        billeteraService = new BilleteraService();
        
        // Obtener el usuario actual
        usuarioActual = sceneController.getUsuarioActual();
        
        if (usuarioActual == null) {
            // Si no hay usuario autenticado, redirigir a la vista de inicio de sesión
            try {
                sceneController.cambiarEscena(SceneController.VISTA_SESION);
                return;
            } catch (IOException e) {
                sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Configurar la información del usuario
        lblNombreUsuario.setText(usuarioActual.getNombre());
        lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
        
        // Configurar el combo box de tipos de transacción
        cmbTipoTransaccion.setItems(FXCollections.observableArrayList(TipoTransaccion.values()));
        
        // Configurar la tabla de transacciones
        configurarTablaTransacciones();
        
        // Cargar las transacciones
        cargarTransacciones();
        
        // Configurar los combo box de cuentas
        cargarCuentas();
        
        // Ocultar campos de cuenta destino inicialmente
        lblCuentaDestino.setVisible(false);
        cmbCuentaDestino.setVisible(false);
        
        // Deshabilitar el botón de realizar transacción hasta que se seleccione un tipo
        btnRealizarTransaccion.setDisable(true);
    }
    
    /**
     * Configura la tabla de transacciones
     */
    private void configurarTablaTransacciones() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        colFecha.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFechaTransaccion().format(formatter)));
        
        colTipo.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTipoTransaccion().toString()));
        
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        colCategoria.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCategoria().getNombre()));
    }
    
    /**
     * Carga las transacciones del usuario
     */
    private void cargarTransacciones() {
        LinkedList<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        transaccionesOriginales = FXCollections.observableArrayList(transacciones);
        tblTransacciones.setItems(transaccionesOriginales);
    }
    
    /**
     * Carga las cuentas del usuario en los combo box
     */
    private void cargarCuentas() {
        ObservableList<Cuenta> cuentas = FXCollections.observableArrayList(usuarioActual.getCuentasAsociadas());
        cmbCuentaOrigen.setItems(cuentas);
        cmbCuentaDestino.setItems(cuentas);
    }
    
    /**
     * Filtra las transacciones por tipo
     */
    @FXML
    private void filtrarTransacciones() {
        TipoTransaccion tipoSeleccionado = cmbTipoTransaccion.getValue();
        
        if (tipoSeleccionado == null) {
            sceneController.mostrarError("Error", "Debe seleccionar un tipo de transacción");
            return;
        }
        
        LinkedList<TransaccionFactory> transaccionesFiltradas = billeteraService.obtenerTransaccionesPorTipo(tipoSeleccionado);
        tblTransacciones.setItems(FXCollections.observableArrayList(transaccionesFiltradas));
    }
    
    /**
     * Limpia el filtro de transacciones
     */
    @FXML
    private void limpiarFiltro() {
        cmbTipoTransaccion.setValue(null);
        tblTransacciones.setItems(transaccionesOriginales);
    }
    
    /**
     * Selecciona el tipo de transacción Depósito
     */
    @FXML
    private void seleccionarDeposito() {
        tipoTransaccionSeleccionado = TipoTransaccion.DEPOSITO;
        lblTipoTransaccion.setText("Depósito");
        lblCuentaDestino.setVisible(false);
        cmbCuentaDestino.setVisible(false);
        btnRealizarTransaccion.setDisable(false);
    }
    
    /**
     * Selecciona el tipo de transacción Retiro
     */
    @FXML
    private void seleccionarRetiro() {
        tipoTransaccionSeleccionado = TipoTransaccion.RETIRO;
        lblTipoTransaccion.setText("Retiro");
        lblCuentaDestino.setVisible(false);
        cmbCuentaDestino.setVisible(false);
        btnRealizarTransaccion.setDisable(false);
    }
    
    /**
     * Selecciona el tipo de transacción Transferencia
     */
    @FXML
    private void seleccionarTransferencia() {
        tipoTransaccionSeleccionado = TipoTransaccion.TRANSFERENCIA;
        lblTipoTransaccion.setText("Transferencia");
        lblCuentaDestino.setVisible(true);
        cmbCuentaDestino.setVisible(true);
        btnRealizarTransaccion.setDisable(false);
    }
    
    /**
     * Realiza la transacción seleccionada
     */
    @FXML
    private void realizarTransaccion() {
        // Validar campos
        if (tipoTransaccionSeleccionado == null) {
            sceneController.mostrarError("Error", "Debe seleccionar un tipo de transacción");
            return;
        }
        
        if (txtMonto.getText().isEmpty()) {
            sceneController.mostrarError("Error", "Debe ingresar un monto");
            return;
        }
        
        if (cmbCuentaOrigen.getValue() == null) {
            sceneController.mostrarError("Error", "Debe seleccionar una cuenta de origen");
            return;
        }
        
        if (tipoTransaccionSeleccionado == TipoTransaccion.TRANSFERENCIA && cmbCuentaDestino.getValue() == null) {
            sceneController.mostrarError("Error", "Debe seleccionar una cuenta de destino");
            return;
        }
        
        // Obtener datos del formulario
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText());
            if (monto <= 0) {
                sceneController.mostrarError("Error", "El monto debe ser mayor a cero");
                return;
            }
        } catch (NumberFormatException e) {
            sceneController.mostrarError("Error", "El monto debe ser un número válido");
            return;
        }
        
        String idCuentaOrigen = cmbCuentaOrigen.getValue().getIdCuenta();
        String idCuentaDestino = null;
        
        if (tipoTransaccionSeleccionado == TipoTransaccion.TRANSFERENCIA) {
            idCuentaDestino = cmbCuentaDestino.getValue().getIdCuenta();
            
            if (idCuentaOrigen.equals(idCuentaDestino)) {
                sceneController.mostrarError("Error", "La cuenta de origen y destino no pueden ser la misma");
                return;
            }
        }
        
        // Realizar la transacción
        boolean transaccionExitosa = false;
        
        switch (tipoTransaccionSeleccionado) {
            case DEPOSITO:
                transaccionExitosa = billeteraService.realizarDeposito(monto, idCuentaOrigen);
                break;
            case RETIRO:
                transaccionExitosa = billeteraService.realizarRetiro(monto, idCuentaOrigen);
                break;
            case TRANSFERENCIA:
                transaccionExitosa = billeteraService.realizarTransferencia(monto, idCuentaOrigen, idCuentaDestino);
                break;
        }
        
        if (transaccionExitosa) {
            sceneController.mostrarInformacion("Transacción exitosa", "La transacción se realizó correctamente");
            limpiarFormularioTransaccion();
            actualizarSaldo();
            cargarTransacciones();
        } else {
            sceneController.mostrarError("Error", "No se pudo realizar la transacción");
        }
    }
    
    /**
     * Limpia el formulario de transacción
     */
    private void limpiarFormularioTransaccion() {
        tipoTransaccionSeleccionado = null;
        lblTipoTransaccion.setText("Seleccione un tipo");
        txtMonto.clear();
        cmbCuentaOrigen.setValue(null);
        cmbCuentaDestino.setValue(null);
        txtDescripcion.clear();
        lblCuentaDestino.setVisible(false);
        cmbCuentaDestino.setVisible(false);
        btnRealizarTransaccion.setDisable(true);
    }
    
    /**
     * Actualiza el saldo del usuario
     */
    private void actualizarSaldo() {
        // Actualizar el usuario actual
        usuarioActual = authService.getUsuarioAutenticado();
        sceneController.setUsuarioActual(usuarioActual);
        
        // Actualizar la etiqueta de saldo
        lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
    }
    
    /**
     * Navega al dashboard
     */
    @FXML
    private void irADashboard() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_DASHBOARD);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar el dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la vista de transacciones (actual)
     */
    @FXML
    private void mostrarTransacciones() {
        // Ya estamos en la vista de transacciones, no es necesario hacer nada
    }
    
    /**
     * Navega a la vista de presupuestos
     */
    @FXML
    private void irAPresupuestos() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_PRESUPUESTOS);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de presupuestos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Navega a la vista de cuentas
     */
    @FXML
    private void irACuentas() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_CUENTAS);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de cuentas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Navega a la vista de categorías
     */
    @FXML
    private void irACategorias() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_CATEGORIAS);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    @FXML
    private void cerrarSesion() {
        if (sceneController.mostrarConfirmacion("Cerrar Sesión", "¿Está seguro que desea cerrar sesión?")) {
            authService.cerrarSesion();
            sceneController.clearData();
            
            try {
                sceneController.cambiarEscena(SceneController.VISTA_BIENVENIDA);
            } catch (IOException e) {
                sceneController.mostrarError("Error", "No se pudo cargar la vista de bienvenida: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
