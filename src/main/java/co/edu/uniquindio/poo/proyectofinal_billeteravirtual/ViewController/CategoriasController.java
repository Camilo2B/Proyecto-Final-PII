package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.ComponenteCategoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.GestorCategorias;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para la vista de categorías
 */
public class CategoriasController {

    @FXML
    private TableView<Categoria> tblCategorias;
    
    @FXML
    private TableColumn<Categoria, String> colNombre;
    
    @FXML
    private TableColumn<Categoria, String> colDescripcion;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private ComboBox<String> cmbTipoCategoria;
    
    @FXML
    private ComboBox<String> cmbCategoriaPadre;
    
    @FXML
    private Label lblSaldo;
    
    @FXML
    private Label lblUsuario;
    
    private SceneController sceneController;
    private BilleteraService billeteraService;
    private AuthService authService;
    private Usuario usuarioActual;
    private GestorCategorias gestorCategorias;
    
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
            gestorCategorias = GestorCategorias.getInstance();
            
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
            
            // Configurar la tabla de categorías
            configurarTablaCategorias();
            
            // Configurar los combo box
            configurarComboBoxes();
            
            // Cargar las categorías
            cargarCategorias();
            
            // Actualizar la interfaz con los datos del usuario
            actualizarInterfaz();
            
        } catch (Exception e) {
            System.err.println("Error al inicializar CategoriasController: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura la tabla de categorías
     */
    private void configurarTablaCategorias() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }
    
    /**
     * Configura los combo boxes
     */
    private void configurarComboBoxes() {
        // Configurar combo box de tipos de categoría
        ObservableList<String> tiposCategorias = FXCollections.observableArrayList(
                "Categoría Simple", "Grupo de Categorías"
        );
        cmbTipoCategoria.setItems(tiposCategorias);
        cmbTipoCategoria.getSelectionModel().selectFirst();
        
        // Configurar combo box de categorías padre
        List<ComponenteCategoria> categoriasPadre = gestorCategorias.obtenerTodasLasCategorias();
        ObservableList<String> nombresCategoriasPadre = FXCollections.observableArrayList();
        nombresCategoriasPadre.add("Ninguna (Categoría Raíz)");
        
        for (ComponenteCategoria categoria : categoriasPadre) {
            nombresCategoriasPadre.add(categoria.getNombre());
        }
        
        cmbCategoriaPadre.setItems(nombresCategoriasPadre);
        cmbCategoriaPadre.getSelectionModel().selectFirst();
        
        // Configurar listener para el tipo de categoría
        cmbTipoCategoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Categoría Simple")) {
                cmbCategoriaPadre.setDisable(false);
            } else {
                cmbCategoriaPadre.getSelectionModel().selectFirst();
            }
        });
    }
    
    /**
     * Carga las categorías
     */
    private void cargarCategorias() {
        try {
            List<Categoria> categorias = billeteraService.obtenerTodasLasCategorias();
            tblCategorias.setItems(FXCollections.observableArrayList(categorias));
            
            // Si no hay categorías, mostrar un mensaje
            if (categorias.isEmpty()) {
                sceneController.mostrarInformacion("Sin categorías", "No hay categorías disponibles. Por favor, cree una categoría.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudieron cargar las categorías: " + e.getMessage());
            tblCategorias.setItems(FXCollections.observableArrayList());
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
     * Crea una nueva categoría
     */
    @FXML
    private void crearCategoria() {
        try {
            // Validar campos
            if (txtNombre.getText().isEmpty()) {
                sceneController.mostrarError("Error", "El nombre de la categoría es obligatorio");
                return;
            }
            
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            String tipoCategoria = cmbTipoCategoria.getValue();
            String categoriaPadre = cmbCategoriaPadre.getValue();
            
            // Crear la categoría
            if (tipoCategoria.equals("Categoría Simple")) {
                if (categoriaPadre.equals("Ninguna (Categoría Raíz)")) {
                    gestorCategorias.crearCategoriaSimple(nombre, descripcion);
                } else {
                    gestorCategorias.crearCategoriaSimpleEnGrupo(categoriaPadre, nombre, descripcion);
                }
            } else {
                gestorCategorias.crearGrupoCategoria(nombre, descripcion);
            }
            
            // Actualizar la tabla de categorías
            cargarCategorias();
            
            // Limpiar los campos
            txtNombre.clear();
            txtDescripcion.clear();
            cmbTipoCategoria.getSelectionModel().selectFirst();
            cmbCategoriaPadre.getSelectionModel().selectFirst();
            
            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Categoría creada correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo crear la categoría: " + e.getMessage());
        }
    }
    
    /**
     * Elimina la categoría seleccionada
     */
    @FXML
    private void eliminarCategoria() {
        try {
            // Obtener la categoría seleccionada
            Categoria categoriaSeleccionada = tblCategorias.getSelectionModel().getSelectedItem();
            
            // Validar que se haya seleccionado una categoría
            if (categoriaSeleccionada == null) {
                sceneController.mostrarError("Error", "Debe seleccionar una categoría para eliminar");
                return;
            }
            
            // Confirmar la eliminación
            boolean confirmacion = sceneController.mostrarConfirmacion("Confirmar eliminación", 
                    "¿Está seguro de que desea eliminar la categoría " + categoriaSeleccionada.getNombre() + "?");
            
            if (!confirmacion) {
                return;
            }
            
            // Eliminar la categoría
            gestorCategorias.eliminarCategoria(categoriaSeleccionada.getNombre());
            
            // Actualizar la tabla de categorías
            cargarCategorias();
            
            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Categoría eliminada correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo eliminar la categoría: " + e.getMessage());
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
