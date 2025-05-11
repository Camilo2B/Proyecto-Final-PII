package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class WelcomeViewController {

    @FXML
    private Button btnBienvenida;

    @FXML
    protected void cambiarVista2(){
        try {
            // Cargar la segunda vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Registro.fxml"));
            Parent root = loader.load();

            // Obtener la escena actual
            Scene scene = btnBienvenida.getScene();

            // Cambiar la escena actual por la nueva
            scene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

