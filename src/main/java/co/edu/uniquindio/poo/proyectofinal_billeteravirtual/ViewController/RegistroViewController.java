package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controlador para la vista de registro de usuarios
 */
public class RegistroViewController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnVolver;

    private SceneController sceneController;
    private AuthenticationService authService;

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        sceneController = SceneController.getInstance();
        authService = AuthenticationService.getInstance();
    }

    /**
     * Crea una nueva cuenta de usuario
     */
    @FXML
    private void crearCuenta() {
        // Validar campos
        if (camposVacios()) {
            sceneController.mostrarError("Error", "Todos los campos son obligatorios");
            return;
        }

        // Obtener datos del formulario
        String nombre = txtNombre.getText();
        String id = txtId.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();
        String password = txtPassword.getText();

        // Registrar usuario
        boolean registroExitoso = authService.registrarUsuario(nombre, id, email, telefono, direccion, password);

        if (registroExitoso) {
            sceneController.mostrarInformacion("Registro exitoso", "Usuario registrado correctamente");
            limpiarCampos();
            irAInicioSesion();
        } else {
            sceneController.mostrarError("Error", "No se pudo registrar el usuario. El ID ya existe.");
        }
    }

    /**
     * Verifica si hay campos vacíos en el formulario
     * @return true si hay campos vacíos, false en caso contrario
     */
    private boolean camposVacios() {
        return txtNombre.getText().isEmpty() ||
               txtId.getText().isEmpty() ||
               txtEmail.getText().isEmpty() ||
               txtTelefono.getText().isEmpty() ||
               txtDireccion.getText().isEmpty() ||
               txtPassword.getText().isEmpty();
    }

    /**
     * Limpia los campos del formulario
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtId.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtPassword.clear();
    }

    /**
     * Navega a la vista de inicio de sesión
     */
    @FXML
    private void irAInicioSesion() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_SESION);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vuelve a la vista de bienvenida
     */
    @FXML
    private void volver() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_BIENVENIDA);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de bienvenida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
