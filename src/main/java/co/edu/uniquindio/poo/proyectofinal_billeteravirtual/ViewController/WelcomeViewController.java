package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Controlador para la vista de bienvenida
 */
public class WelcomeViewController {

    @FXML
    private Button btnBienvenida;

    private SceneController sceneController;

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        sceneController = SceneController.getInstance();
    }

    /**
     * Cambia a la vista de inicio de sesión
     */
    @FXML
    protected void cambiarVista2() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_SESION);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
