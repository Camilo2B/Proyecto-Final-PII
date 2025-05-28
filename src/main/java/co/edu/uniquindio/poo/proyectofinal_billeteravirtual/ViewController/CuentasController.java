package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoCuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Controlador para la vista de cuentas
 */
public class CuentasController {

    @FXML
    private TableView<Cuenta> tblCuentas;

    @FXML
    private TableColumn<Cuenta, String> colNombre;

    @FXML
    private TableColumn<Cuenta, String> colTipo;

    @FXML
    private TableColumn<Cuenta, String> colNumeroCuenta;

    @FXML
    private TableColumn<Cuenta, Double> colSaldo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cmbTipoCuenta;

    @FXML
    private TextField txtNumeroCuenta;

    @FXML
    private TextField txtSaldoInicial;

    @FXML
    private Label lblSaldo;

    @FXML
    private Label lblUsuario;

    private SceneController sceneController;
    private BilleteraService billeteraService;
    private AuthService authService;
    private Usuario usuarioActual;

    /**
     * Inicializa el controlador
     */
    @FXML
    private void initialize() {
        try {
            // Obtener instancias de los servicios
            sceneController = SceneController.getInstance();
            billeteraService = BilleteraService.getInstance();
            authService = AuthService.getInstance();

            // Obtener el usuario actual
            usuarioActual = sceneController.getUsuarioActual();
            System.out.println("Usuario obtenido del SceneController: " + (usuarioActual != null ? usuarioActual.getNombre() : "null"));

            if (usuarioActual == null) {
                // Si no hay usuario autenticado, intentar obtenerlo del servicio de autenticación
                usuarioActual = authService.getUsuarioAutenticado();
                System.out.println("Usuario obtenido del AuthService: " + (usuarioActual != null ? usuarioActual.getNombre() : "null"));

                if (usuarioActual != null) {
                    // Guardar el usuario en el SceneController
                    sceneController.setUsuarioActual(usuarioActual);
                } else {
                    // Si todavía no hay usuario, redirigir a la vista de inicio de sesión
                    System.out.println("No hay usuario autenticado, redirigiendo a la vista de inicio de sesión");
                    sceneController.mostrarError("Sesión no iniciada", "Debe iniciar sesión para acceder a esta vista");
                    sceneController.cambiarEscena(SceneController.VISTA_SESION);
                    return;
                }
            }

            // Sincronizar el usuario con BilleteraService
            billeteraService.setUsuarioActual(usuarioActual);

            // Configurar la tabla de cuentas
            configurarTablaCuentas();

            // Configurar el combo box de tipos de cuenta
            configurarComboBoxTiposCuenta();

            // Cargar las cuentas del usuario
            cargarCuentas();

            // Actualizar la interfaz con los datos del usuario
            actualizarInterfaz();

        } catch (Exception e) {
            System.err.println("Error al inicializar CuentasController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configura la tabla de cuentas
     */
    private void configurarTablaCuentas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colNumeroCuenta.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
    }

    /**
     * Configura el combo box de tipos de cuenta
     */
    private void configurarComboBoxTiposCuenta() {
        ObservableList<String> tiposCuenta = FXCollections.observableArrayList(
                "Cuenta Corriente", "Cuenta de Ahorros", "Tarjeta de Crédito", "Efectivo", "Otro"
        );
        cmbTipoCuenta.setItems(tiposCuenta);
        cmbTipoCuenta.getSelectionModel().selectFirst();
    }

    /**
     * Carga las cuentas del usuario
     */
    private void cargarCuentas() {
        try {
            LinkedList<Cuenta> cuentas = usuarioActual.getCuentasAsociadas();
            tblCuentas.setItems(FXCollections.observableArrayList(cuentas));

            // Si no hay cuentas, mostrar un mensaje
            if (cuentas.isEmpty()) {
                sceneController.mostrarInformacion("Sin cuentas", "No tiene cuentas asociadas. Por favor, cree una cuenta.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar cuentas: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudieron cargar las cuentas: " + e.getMessage());
            tblCuentas.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Actualiza la interfaz con los datos del usuario
     */
    private void actualizarInterfaz() {
        lblUsuario.setText(usuarioActual.getNombre());
        lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
    }

    /**
     * Crea una nueva cuenta
     */
    @FXML
    private void crearCuenta() {
        try {
            // Validar campos
            if (txtNombre.getText().isEmpty() || cmbTipoCuenta.getValue() == null ||
                txtNumeroCuenta.getText().isEmpty() || txtSaldoInicial.getText().isEmpty()) {
                sceneController.mostrarError("Error", "Todos los campos son obligatorios");
                return;
            }

            // Validar que el saldo inicial sea un número válido
            double saldoInicial;
            try {
                saldoInicial = Double.parseDouble(txtSaldoInicial.getText());
                if (saldoInicial < 0) {
                    sceneController.mostrarError("Error", "El saldo inicial no puede ser negativo");
                    return;
                }
            } catch (NumberFormatException e) {
                sceneController.mostrarError("Error", "El saldo inicial debe ser un número válido");
                return;
            }

            // Crear la cuenta
            String nombre = txtNombre.getText();
            String tipo = cmbTipoCuenta.getValue();
            String numeroCuenta = txtNumeroCuenta.getText();

            // Generar ID único para la cuenta
            String idCuenta = java.util.UUID.randomUUID().toString();

            // Convertir tipo de String a TipoCuenta
            TipoCuenta tipoCuenta;
            switch (tipo) {
                case "Cuenta de Ahorros":
                    tipoCuenta = TipoCuenta.AHORRO;
                    break;
                case "Cuenta Corriente":
                    tipoCuenta = TipoCuenta.CORRIENTE;
                    break;
                case "Tarjeta de Crédito":
                    tipoCuenta = TipoCuenta.CREDITO;
                    break;
                case "Efectivo":
                    tipoCuenta = TipoCuenta.EFECTIVO;
                    break;
                default:
                    tipoCuenta = TipoCuenta.OTRO;
                    break;
            }

            Cuenta nuevaCuenta = new Cuenta(idCuenta, nombre, tipoCuenta, numeroCuenta, saldoInicial);

            // Agregar la cuenta al usuario
            billeteraService.agregarCuentaUsuario(usuarioActual, nuevaCuenta);

            // Actualizar la tabla de cuentas
            cargarCuentas();

            // Actualizar la interfaz
            actualizarInterfaz();

            // Limpiar los campos
            txtNombre.clear();
            cmbTipoCuenta.getSelectionModel().selectFirst();
            txtNumeroCuenta.clear();
            txtSaldoInicial.clear();

            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Cuenta creada correctamente");

        } catch (Exception e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo crear la cuenta: " + e.getMessage());
        }
    }

    /**
     * Elimina la cuenta seleccionada
     */
    @FXML
    private void eliminarCuenta() {
        try {
            // Obtener la cuenta seleccionada
            Cuenta cuentaSeleccionada = tblCuentas.getSelectionModel().getSelectedItem();

            // Validar que se haya seleccionado una cuenta
            if (cuentaSeleccionada == null) {
                sceneController.mostrarError("Error", "Debe seleccionar una cuenta para eliminar");
                return;
            }

            // Confirmar la eliminación
            boolean confirmacion = sceneController.mostrarConfirmacion("Confirmar eliminación",
                    "¿Está seguro de que desea eliminar la cuenta " + cuentaSeleccionada.getNombre() + "?");

            if (!confirmacion) {
                return;
            }

            // Eliminar la cuenta
            billeteraService.eliminarCuentaUsuario(usuarioActual, cuentaSeleccionada);

            // Actualizar la tabla de cuentas
            cargarCuentas();

            // Actualizar la interfaz
            actualizarInterfaz();

            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Cuenta eliminada correctamente");

        } catch (Exception e) {
            System.err.println("Error al eliminar cuenta: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo eliminar la cuenta: " + e.getMessage());
        }
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
     * Navega a la vista de transacciones
     */
    @FXML
    private void irATransacciones() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_TRANSACCIONES);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de transacciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión actual
     */
    @FXML
    private void cerrarSesion() {
        try {
            // Confirmar cierre de sesión
            boolean confirmacion = sceneController.mostrarConfirmacion("Confirmar cierre de sesión",
                    "¿Está seguro de que desea cerrar sesión?");

            if (!confirmacion) {
                return;
            }

            // Cerrar sesión
            authService.cerrarSesion();

            // Limpiar usuario actual
            sceneController.setUsuarioActual(null);

            // Navegar a la vista de inicio de sesión
            sceneController.cambiarEscena(SceneController.VISTA_SESION);

        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
