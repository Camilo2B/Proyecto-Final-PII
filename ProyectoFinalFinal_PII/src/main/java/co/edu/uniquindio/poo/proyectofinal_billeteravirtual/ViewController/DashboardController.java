package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthenticationService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Presupuesto;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Controlador para la vista de dashboard del usuario
 */
public class DashboardController {

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblSaldo;

    @FXML
    private Label lblSaldoTotal;

    @FXML
    private Label lblCantidadCuentas;

    @FXML
    private Label lblCantidadPresupuestos;

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
    private TableView<Presupuesto> tblPresupuestos;

    @FXML
    private TableColumn<Presupuesto, String> colNombrePresupuesto;

    @FXML
    private TableColumn<Presupuesto, Double> colMontoTotal;

    @FXML
    private TableColumn<Presupuesto, Double> colMontoGastado;

    @FXML
    private TableColumn<Presupuesto, String> colCategoria;

    @FXML
    private TableColumn<Presupuesto, Double> colPorcentaje;

    private SceneController sceneController;
    private AuthenticationService authService;
    private BilleteraService billeteraService;
    private Usuario usuarioActual;

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        try {
            System.out.println("Inicializando DashboardController");
            sceneController = SceneController.getInstance();
            authService = AuthenticationService.getInstance();
            billeteraService = new BilleteraService();

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
                    sceneController.cambiarEscena(SceneController.VISTA_SESION);
                    return;
                }
            }

            // Configurar la información del usuario
            lblNombreUsuario.setText(usuarioActual.getNombre());
            lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
            lblSaldoTotal.setText("$" + String.format("%.2f", usuarioActual.getSaldoTotal()));
            lblCantidadCuentas.setText(String.valueOf(usuarioActual.getCuentasAsociadas().size()));
            lblCantidadPresupuestos.setText(String.valueOf(usuarioActual.getPresupuestos().size()));

            // Configurar las tablas
            configurarTablaTransacciones();
            configurarTablaPresupuestos();

            // Cargar los datos
            cargarTransacciones();
            cargarPresupuestos();

            System.out.println("DashboardController inicializado correctamente");
        } catch (Exception e) {
            System.err.println("Error al inicializar DashboardController: " + e.getMessage());
            e.printStackTrace();
            try {
                sceneController.mostrarError("Error", "No se pudo cargar el dashboard: " + e.getMessage());
                sceneController.cambiarEscena(SceneController.VISTA_SESION);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
    }

    /**
     * Configura la tabla de presupuestos
     */
    private void configurarTablaPresupuestos() {
        colNombrePresupuesto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMontoTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        colMontoGastado.setCellValueFactory(new PropertyValueFactory<>("montoGastado"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaEspecifica"));
        colPorcentaje.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPorcentajeUso()).asObject());
    }

    /**
     * Carga las transacciones del usuario
     */
    private void cargarTransacciones() {
        LinkedList<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        tblTransacciones.setItems(FXCollections.observableArrayList(transacciones));
    }

    /**
     * Carga los presupuestos del usuario
     */
    private void cargarPresupuestos() {
        LinkedList<Presupuesto> presupuestos = billeteraService.obtenerPresupuestosUsuario();
        tblPresupuestos.setItems(FXCollections.observableArrayList(presupuestos));
    }

    /**
     * Muestra el dashboard
     */
    @FXML
    private void mostrarDashboard() {
        // Ya estamos en el dashboard, no es necesario hacer nada
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
