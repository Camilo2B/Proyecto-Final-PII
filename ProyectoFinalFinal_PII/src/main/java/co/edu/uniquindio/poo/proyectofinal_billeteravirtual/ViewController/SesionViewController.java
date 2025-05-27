package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Administrador;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthenticationService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controlador para la vista de inicio de sesión
 */
public class SesionViewController {

    @FXML
    private TextField txtId;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox chkAdmin;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrarse;

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
     * Inicia sesión con las credenciales proporcionadas
     */
    @FXML
    private void iniciarSesion() {
        try {
            System.out.println("Iniciando sesión...");

            // Validar campos
            if (camposVacios()) {
                sceneController.mostrarError("Error", "Todos los campos son obligatorios");
                return;
            }

            // Obtener datos del formulario
            String id = txtId.getText();
            String password = txtPassword.getText();
            boolean esAdmin = chkAdmin.isSelected();

            System.out.println("Intentando autenticar: ID=" + id + ", esAdmin=" + esAdmin);

            // Autenticar usuario
            boolean autenticacionExitosa;

            if (esAdmin) {
                autenticacionExitosa = authService.autenticarAdmin(id, password);
                System.out.println("Autenticación de administrador: " + (autenticacionExitosa ? "exitosa" : "fallida"));

                if (autenticacionExitosa) {
                    Administrador admin = authService.getAdminAutenticado();
                    System.out.println("Admin autenticado: " + admin.getNombre() + ", ID: " + admin.getIdGeneral());
                    sceneController.setAdminActual(admin);
                    irADashboardAdmin();
                }
            } else {
                autenticacionExitosa = authService.autenticarUsuario(id, password);
                System.out.println("Autenticación de usuario: " + (autenticacionExitosa ? "exitosa" : "fallida"));

                if (autenticacionExitosa) {
                    Usuario usuario = authService.getUsuarioAutenticado();
                    System.out.println("Usuario autenticado: " + usuario.getNombre() + ", ID: " + usuario.getIdGeneral());

                    // Guardar el usuario en el SceneController
                    sceneController.setUsuarioActual(usuario);

                    // Verificar que se guardó correctamente
                    Usuario usuarioGuardado = sceneController.getUsuarioActual();
                    System.out.println("Usuario guardado en SceneController: " +
                        (usuarioGuardado != null ? usuarioGuardado.getNombre() : "null"));

                    irADashboardUsuario();
                }
            }

            if (!autenticacionExitosa) {
                sceneController.mostrarError("Error", "Credenciales incorrectas");
            }
        } catch (Exception e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "Error al iniciar sesión: " + e.getMessage());
        }
    }

    /**
     * Verifica si hay campos vacíos en el formulario
     * @return true si hay campos vacíos, false en caso contrario
     */
    private boolean camposVacios() {
        return txtId.getText().isEmpty() || txtPassword.getText().isEmpty();
    }

    /**
     * Navega al dashboard de usuario
     */
    private void irADashboardUsuario() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_DASHBOARD);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar el dashboard de usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega al dashboard de administrador
     */
    private void irADashboardAdmin() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_ADMIN);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar el dashboard de administrador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega a la vista de registro
     */
    @FXML
    private void irARegistro() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_REGISTRO);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de registro: " + e.getMessage());
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
