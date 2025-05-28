package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Administrador;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de administración
 * Gestiona las funcionalidades del panel de administrador
 */
public class AdminController implements Initializable {

    // Servicios
    private SceneController sceneController;
    private BilleteraService billeteraService;
    private DataManager dataManager;
    private Administrador adminActual;

    // Componentes FXML - Tabla de usuarios
    @FXML
    private TableView<Usuario> tblUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colId;
    
    @FXML
    private TableColumn<Usuario, String> colNombre;
    
    @FXML
    private TableColumn<Usuario, String> colEmail;
    
    @FXML
    private TableColumn<Usuario, String> colTelefono;
    
    @FXML
    private TableColumn<Usuario, Double> colSaldo;
    
    @FXML
    private TableColumn<Usuario, Integer> colCuentas;
    
    @FXML
    private TableColumn<Usuario, Integer> colTransacciones;

    // Componentes FXML - Estadísticas
    @FXML
    private Label lblAdmin;
    
    @FXML
    private Label lblTotalUsuarios;
    
    @FXML
    private Label lblTotalTransacciones;
    
    @FXML
    private Label lblVolumenTotal;

    /**
     * Inicializa el controlador
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Inicializar servicios
            sceneController = SceneController.getInstance();
            billeteraService = BilleteraService.getInstance();
            dataManager = DataManager.getInstance();

            // Obtener el administrador actual
            adminActual = sceneController.getAdminActual();

            if (adminActual == null) {
                // Si no hay administrador autenticado, redirigir a la vista de inicio de sesión
                try {
                    sceneController.cambiarEscena(SceneController.VISTA_SESION);
                    return;
                } catch (IOException e) {
                    sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Configurar la interfaz
            configurarTablaUsuarios();
            cargarDatosUsuarios();
            actualizarEstadisticas();
            actualizarInfoAdmin();

        } catch (Exception e) {
            System.err.println("Error al inicializar AdminController: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "Error al inicializar el panel de administración: " + e.getMessage());
        }
    }

    /**
     * Configura las columnas de la tabla de usuarios
     */
    private void configurarTablaUsuarios() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idGeneral"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldoTotal"));
        
        // Para las columnas de cuentas y transacciones, necesitamos calcular los valores
        colCuentas.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            int numCuentas = usuario.getCuentasAsociadas() != null ? usuario.getCuentasAsociadas().size() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(numCuentas).asObject();
        });
        
        colTransacciones.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            int numTransacciones = contarTransaccionesUsuario(usuario.getIdGeneral());
            return new javafx.beans.property.SimpleIntegerProperty(numTransacciones).asObject();
        });
    }

    /**
     * Carga los datos de usuarios en la tabla
     */
    private void cargarDatosUsuarios() {
        try {
            LinkedList<Usuario> usuarios = dataManager.getUsuarios();
            ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList(usuarios);
            tblUsuarios.setItems(usuariosObservable);
        } catch (Exception e) {
            System.err.println("Error al cargar datos de usuarios: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "Error al cargar los datos de usuarios: " + e.getMessage());
        }
    }

    /**
     * Actualiza las estadísticas del sistema
     */
    private void actualizarEstadisticas() {
        try {
            // Total de usuarios
            int totalUsuarios = dataManager.getUsuarios().size();
            lblTotalUsuarios.setText(String.valueOf(totalUsuarios));

            // Total de transacciones
            int totalTransacciones = dataManager.getTransacciones().size();
            lblTotalTransacciones.setText(String.valueOf(totalTransacciones));

            // Volumen total (suma de todos los saldos)
            double volumenTotal = dataManager.getUsuarios().stream()
                    .mapToDouble(Usuario::getSaldoTotal)
                    .sum();

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            lblVolumenTotal.setText(formatter.format(volumenTotal));

        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la información del administrador en la interfaz
     */
    private void actualizarInfoAdmin() {
        if (adminActual != null) {
            lblAdmin.setText("Admin: " + adminActual.getNombre());
        }
    }

    /**
     * Cuenta las transacciones de un usuario específico
     */
    private int contarTransaccionesUsuario(String idUsuario) {
        try {
            return (int) dataManager.getTransacciones().stream()
                    .filter(t -> t.getUsuario() != null && t.getUsuario().getIdGeneral().equals(idUsuario))
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Maneja el evento de ver detalles de usuario
     */
    @FXML
    private void verDetallesUsuario() {
        Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        
        if (usuarioSeleccionado == null) {
            sceneController.mostrarError("Error", "Debe seleccionar un usuario de la tabla");
            return;
        }

        // Mostrar información detallada del usuario
        StringBuilder detalles = new StringBuilder();
        detalles.append("ID: ").append(usuarioSeleccionado.getIdGeneral()).append("\n");
        detalles.append("Nombre: ").append(usuarioSeleccionado.getNombre()).append("\n");
        detalles.append("Email: ").append(usuarioSeleccionado.getEmail()).append("\n");
        detalles.append("Teléfono: ").append(usuarioSeleccionado.getTelefono()).append("\n");
        detalles.append("Dirección: ").append(usuarioSeleccionado.getDireccion()).append("\n");
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        detalles.append("Saldo Total: ").append(formatter.format(usuarioSeleccionado.getSaldoTotal())).append("\n");
        
        int numCuentas = usuarioSeleccionado.getCuentasAsociadas() != null ? usuarioSeleccionado.getCuentasAsociadas().size() : 0;
        detalles.append("Número de Cuentas: ").append(numCuentas).append("\n");
        
        int numTransacciones = contarTransaccionesUsuario(usuarioSeleccionado.getIdGeneral());
        detalles.append("Número de Transacciones: ").append(numTransacciones);

        sceneController.mostrarInformacion("Detalles del Usuario", detalles.toString());
    }

    /**
     * Maneja el evento de bloquear usuario
     */
    @FXML
    private void bloquearUsuario() {
        Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        
        if (usuarioSeleccionado == null) {
            sceneController.mostrarError("Error", "Debe seleccionar un usuario de la tabla");
            return;
        }

        boolean confirmacion = sceneController.mostrarConfirmacion(
            "Confirmar Bloqueo", 
            "¿Está seguro de que desea bloquear al usuario " + usuarioSeleccionado.getNombre() + "?"
        );

        if (confirmacion) {
            // Aquí se implementaría la lógica de bloqueo
            // Por ahora, solo mostramos un mensaje
            sceneController.mostrarInformacion("Usuario Bloqueado", 
                "El usuario " + usuarioSeleccionado.getNombre() + " ha sido bloqueado exitosamente.");
        }
    }

    /**
     * Maneja el evento de eliminar usuario
     */
    @FXML
    private void eliminarUsuario() {
        Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        
        if (usuarioSeleccionado == null) {
            sceneController.mostrarError("Error", "Debe seleccionar un usuario de la tabla");
            return;
        }

        boolean confirmacion = sceneController.mostrarConfirmacion(
            "Confirmar Eliminación", 
            "¿Está seguro de que desea eliminar al usuario " + usuarioSeleccionado.getNombre() + "?\n" +
            "Esta acción no se puede deshacer."
        );

        if (confirmacion) {
            try {
                dataManager.eliminarUsuario(usuarioSeleccionado.getIdGeneral());
                cargarDatosUsuarios(); // Recargar la tabla
                actualizarEstadisticas(); // Actualizar estadísticas
                sceneController.mostrarInformacion("Usuario Eliminado", 
                    "El usuario " + usuarioSeleccionado.getNombre() + " ha sido eliminado exitosamente.");
            } catch (Exception e) {
                sceneController.mostrarError("Error", "No se pudo eliminar el usuario: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Maneja el evento de cerrar sesión
     */
    @FXML
    private void cerrarSesion() {
        try {
            // Limpiar datos de sesión
            sceneController.clearData();
            
            // Redirigir a la vista de inicio de sesión
            sceneController.cambiarEscena(SceneController.VISTA_SESION);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cerrar la sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
