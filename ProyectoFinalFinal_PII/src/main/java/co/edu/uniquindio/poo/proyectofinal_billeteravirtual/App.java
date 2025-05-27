package co.edu.uniquindio.poo.proyectofinal_billeteravirtual;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que inicia la aplicación de Billetera Virtual
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Inicializar el DataManager para cargar los datos iniciales
        DataManager.getInstance();

        // Cargar la vista de bienvenida
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Bienvenida.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1126, 764);

        stage.setTitle("Billetera Virtual");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Obtiene la ventana principal de la aplicación
     * @return Stage principal
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}