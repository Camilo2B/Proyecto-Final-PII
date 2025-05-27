package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.integration;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController.DashboardController;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController.EstadisticasController;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController.SceneController;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController.SesionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Pruebas de integración para la interfaz de usuario.
 * Estas pruebas verifican la correcta interacción entre los controladores de vista y el modelo.
 * 
 * Nota: Estas pruebas requieren TestFX y Mockito para simular la interacción del usuario
 * con la interfaz gráfica y para crear mocks de los componentes del modelo.
 */
@ExtendWith(ApplicationExtension.class)
public class UIIntegrationTest {
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @Mock
    private BilleteraService billeteraService;
    
    @Mock
    private SceneController sceneController;
    
    /**
     * Configuración inicial para las pruebas de UI.
     * @param stage El escenario principal para las pruebas
     * @throws IOException Si hay un error al cargar los archivos FXML
     */
    @Start
    public void start(Stage stage) throws IOException {
        MockitoAnnotations.openMocks(this);
        this.stage = stage;
        
        // Configurar el mock de BilleteraService
        when(billeteraService.iniciarSesionUsuario(anyString(), anyString())).thenReturn(true);
        
        // Cargar la vista de inicio de sesión
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Sesion.fxml"));
        root = loader.load();
        
        // Obtener el controlador y configurar el mock
        SesionController controller = loader.getController();
        controller.setBilleteraService(billeteraService);
        controller.setSceneController(sceneController);
        
        // Configurar la escena
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Prueba la integración del controlador de inicio de sesión con el modelo.
     * @param robot Robot para simular interacciones del usuario
     */
    @Test
    public void testIntegracionInicioSesion(FxRobot robot) {
        // Simular entrada de datos
        robot.clickOn("#txtUsuario").write("U001");
        robot.clickOn("#txtPassword").write("password123");
        robot.clickOn("#btnIngresar");
        
        // Verificar que se llamó al método de inicio de sesión
        verify(billeteraService).iniciarSesionUsuario("U001", "password123");
        
        // Verificar que se intentó cambiar a la vista del dashboard
        verify(sceneController).cambiarAVista(SceneController.VISTA_DASHBOARD);
    }
    
    /**
     * Prueba la integración del controlador del dashboard con el modelo.
     * @throws IOException Si hay un error al cargar los archivos FXML
     */
    @Test
    public void testIntegracionDashboard() throws IOException {
        // Configurar el mock de BilleteraService para el dashboard
        when(billeteraService.hayUsuarioLogueado()).thenReturn(true);
        
        // Cargar la vista del dashboard
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Dashboard.fxml"));
        root = loader.load();
        
        // Obtener el controlador y configurar el mock
        DashboardController controller = loader.getController();
        controller.setBilleteraService(billeteraService);
        controller.setSceneController(sceneController);
        
        // Inicializar el controlador
        controller.initialize();
        
        // Verificar que se consultó si hay un usuario logueado
        verify(billeteraService).hayUsuarioLogueado();
    }
    
    /**
     * Prueba la integración del controlador de estadísticas con el modelo.
     * @throws IOException Si hay un error al cargar los archivos FXML
     */
    @Test
    public void testIntegracionEstadisticas() throws IOException {
        // Configurar el mock de BilleteraService para estadísticas
        when(billeteraService.hayUsuarioLogueado()).thenReturn(true);
        when(billeteraService.obtenerTransaccionesUsuario()).thenReturn(new java.util.LinkedList<>());
        
        // Cargar la vista de estadísticas
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/proyectofinal_billeteravirtual/Estadisticas.fxml"));
        root = loader.load();
        
        // Obtener el controlador y configurar el mock
        EstadisticasController controller = loader.getController();
        controller.setBilleteraService(billeteraService);
        controller.setSceneController(sceneController);
        
        // Inicializar el controlador
        controller.initialize();
        
        // Verificar que se consultaron las transacciones
        verify(billeteraService).obtenerTransaccionesUsuario();
    }
}
