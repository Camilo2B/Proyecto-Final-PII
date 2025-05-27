package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.App;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Administrador;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar la navegación entre pantallas
 * Implementa el patrón Singleton
 */
public class SceneController {

    private static SceneController instance;
    private Stage primaryStage;
    private Map<String, Object> dataCache;

    // Constantes para las rutas de las vistas
    public static final String VISTA_BIENVENIDA = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Bienvenida.fxml";
    public static final String VISTA_REGISTRO = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Registro.fxml";
    public static final String VISTA_SESION = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Sesion.fxml";
    public static final String VISTA_ROL = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Rol.fxml";
    public static final String VISTA_USUARIO = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Usuario.fxml";
    public static final String VISTA_ADMIN = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Admin.fxml";
    public static final String VISTA_DASHBOARD = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Dashboard.fxml";
    public static final String VISTA_TRANSACCIONES = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Transacciones.fxml";
    public static final String VISTA_PRESUPUESTOS = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Presupuestos.fxml";
    public static final String VISTA_CUENTAS = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Cuentas.fxml";
    public static final String VISTA_CATEGORIAS = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Categorias.fxml";
    public static final String VISTA_REPORTES = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Reportes.fxml";
    public static final String VISTA_ESTADISTICAS = "/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Estadisticas.fxml";

    // Constantes para las claves de datos
    public static final String USUARIO_ACTUAL = "usuarioActual";
    public static final String ADMIN_ACTUAL = "adminActual";

    /**
     * Constructor privado para implementar Singleton
     */
    private SceneController() {
        this.primaryStage = App.getPrimaryStage();
        this.dataCache = new HashMap<>();
    }

    /**
     * Obtiene la instancia única del SceneController
     * @return instancia de SceneController
     */
    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    /**
     * Cambia a la vista especificada
     * @param fxmlPath Ruta del archivo FXML
     * @throws IOException Si ocurre un error al cargar la vista
     */
    public void cambiarEscena(String fxmlPath) throws IOException {
        try {
            // Crear un nuevo FXMLLoader con configuración para manejar caracteres especiales
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Cargar la vista
            Parent root = loader.load();

            // Crear y establecer la escena
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Imprimir información de depuración
            System.out.println("Cambiando a escena: " + fxmlPath);
            if (getUsuarioActual() != null) {
                System.out.println("Usuario actual: " + getUsuarioActual().getNombre() + ", ID: " + getUsuarioActual().getIdGeneral());
            } else {
                System.out.println("No hay usuario actual");
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar escena a " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();

            // Mostrar un diálogo de error al usuario
            mostrarError("Error", "No se pudo cargar la vista: " + e.getMessage());

            // No propagar la excepción para evitar que la aplicación se cierre
            // En su lugar, intentar cargar una vista de respaldo
            try {
                // Intentar cargar la vista de inicio de sesión como respaldo
                if (!fxmlPath.equals(VISTA_SESION)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(VISTA_SESION));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            } catch (Exception ex) {
                // Si también falla el respaldo, ahora sí lanzar la excepción original
                System.err.println("Error al cargar vista de respaldo: " + ex.getMessage());
                throw e;
            }
        }
    }

    /**
     * Cambia a la vista especificada y obtiene el controlador
     * @param fxmlPath Ruta del archivo FXML
     * @param <T> Tipo del controlador
     * @return Controlador de la vista
     * @throws IOException Si ocurre un error al cargar la vista
     */
    public <T> T cambiarEscenaYObtenerControlador(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        return loader.getController();
    }

    /**
     * Almacena un dato en la caché
     * @param key Clave del dato
     * @param value Valor del dato
     */
    public void setData(String key, Object value) {
        dataCache.put(key, value);
    }

    /**
     * Obtiene un dato de la caché
     * @param key Clave del dato
     * @param <T> Tipo del dato
     * @return Valor del dato
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) dataCache.get(key);
    }

    /**
     * Elimina un dato de la caché
     * @param key Clave del dato
     */
    public void removeData(String key) {
        dataCache.remove(key);
    }

    /**
     * Limpia la caché
     */
    public void clearData() {
        dataCache.clear();
    }

    /**
     * Almacena el usuario actual en la caché
     * @param usuario Usuario actual
     */
    public void setUsuarioActual(Usuario usuario) {
        setData(USUARIO_ACTUAL, usuario);
    }

    /**
     * Obtiene el usuario actual de la caché
     * @return Usuario actual
     */
    public Usuario getUsuarioActual() {
        return getData(USUARIO_ACTUAL);
    }

    /**
     * Almacena el administrador actual en la caché
     * @param admin Administrador actual
     */
    public void setAdminActual(Administrador admin) {
        setData(ADMIN_ACTUAL, admin);
    }

    /**
     * Obtiene el administrador actual de la caché
     * @return Administrador actual
     */
    public Administrador getAdminActual() {
        return getData(ADMIN_ACTUAL);
    }

    /**
     * Muestra un mensaje de error
     * @param titulo Título del mensaje
     * @param mensaje Contenido del mensaje
     */
    public void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de información
     * @param titulo Título del mensaje
     * @param mensaje Contenido del mensaje
     */
    public void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de confirmación
     * @param titulo Título del mensaje
     * @param mensaje Contenido del mensaje
     * @return true si el usuario confirma, false en caso contrario
     */
    public boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
