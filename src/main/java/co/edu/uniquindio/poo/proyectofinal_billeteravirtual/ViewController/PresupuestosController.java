package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Presupuesto;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para la vista de presupuestos
 */
public class PresupuestosController {

    @FXML
    private TableView<Presupuesto> tblPresupuestos;

    @FXML
    private TableColumn<Presupuesto, String> colNombre;

    @FXML
    private TableColumn<Presupuesto, String> colCategoria;

    @FXML
    private TableColumn<Presupuesto, Double> colMontoTotal;

    @FXML
    private TableColumn<Presupuesto, Double> colMontoGastado;

    @FXML
    private TableColumn<Presupuesto, Double> colPorcentaje;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtMontoTotal;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private Label lblSaldo;

    @FXML
    private Label lblUsuario;

    private SceneController sceneController;
    private BilleteraService billeteraService;
    private AuthService authService;
    private Usuario usuarioActual;

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

            // Configurar la tabla de presupuestos
            configurarTablaPresupuestos();

            // Configurar el combo box de categorías
            configurarComboBoxCategorias();

            // Cargar los presupuestos
            cargarPresupuestos();

            // Actualizar la interfaz con los datos del usuario
            actualizarInterfaz();

        } catch (Exception e) {
            System.err.println("Error al inicializar PresupuestosController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configura la tabla de presupuestos
     */
    private void configurarTablaPresupuestos() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCategoria() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getCategoria().getNombre());
            } else {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Sin categoría");
            }
        });
        colMontoTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        colMontoGastado.setCellValueFactory(new PropertyValueFactory<>("montoGastado"));
        colPorcentaje.setCellValueFactory(cellData -> {
            return javafx.beans.binding.Bindings.createObjectBinding(() -> {
                double porcentaje = 0;
                if (cellData.getValue().getMontoTotal() > 0) {
                    porcentaje = (cellData.getValue().getMontoGastado() / cellData.getValue().getMontoTotal()) * 100;
                }
                return porcentaje;
            });
        });
    }

    /**
     * Configura el combo box de categorías
     */
    private void configurarComboBoxCategorias() {
        try {
            List<Categoria> categorias = billeteraService.obtenerTodasLasCategorias();
            cmbCategoria.setItems(FXCollections.observableArrayList(categorias));

            // Configurar el convertidor para mostrar el nombre de la categoría
            cmbCategoria.setConverter(new javafx.util.StringConverter<Categoria>() {
                @Override
                public String toString(Categoria categoria) {
                    return categoria != null ? categoria.getNombre() : "";
                }

                @Override
                public Categoria fromString(String string) {
                    return null; // No se usa
                }
            });
        } catch (Exception e) {
            System.err.println("Error al configurar combo box de categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga los presupuestos
     */
    private void cargarPresupuestos() {
        try {
            List<Presupuesto> presupuestos = billeteraService.obtenerPresupuestosUsuario(usuarioActual);
            tblPresupuestos.setItems(FXCollections.observableArrayList(presupuestos));

            // Si no hay presupuestos, mostrar un mensaje
            if (presupuestos.isEmpty()) {
                sceneController.mostrarInformacion("Sin presupuestos", "No tiene presupuestos creados. Por favor, cree un presupuesto.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar presupuestos: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudieron cargar los presupuestos: " + e.getMessage());
            tblPresupuestos.setItems(FXCollections.observableArrayList());
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
     * Crea un nuevo presupuesto
     */
    @FXML
    private void crearPresupuesto() {
        try {
            // Validar campos
            if (txtNombre.getText().isEmpty() || txtMontoTotal.getText().isEmpty() || cmbCategoria.getValue() == null) {
                sceneController.mostrarError("Error", "Todos los campos son obligatorios");
                return;
            }

            // Validar que el monto total sea un número válido
            double montoTotal;
            try {
                montoTotal = Double.parseDouble(txtMontoTotal.getText());
                if (montoTotal <= 0) {
                    sceneController.mostrarError("Error", "El monto total debe ser mayor que cero");
                    return;
                }
            } catch (NumberFormatException e) {
                sceneController.mostrarError("Error", "El monto total debe ser un número válido");
                return;
            }

            // Crear el presupuesto
            String nombre = txtNombre.getText();
            Categoria categoria = cmbCategoria.getValue();

            Presupuesto nuevoPresupuesto = new Presupuesto(nombre, categoria, montoTotal);

            // Agregar el presupuesto al usuario
            billeteraService.agregarPresupuestoUsuario(usuarioActual, nuevoPresupuesto);

            // Actualizar la tabla de presupuestos
            cargarPresupuestos();

            // Limpiar los campos
            txtNombre.clear();
            txtMontoTotal.clear();
            cmbCategoria.getSelectionModel().clearSelection();

            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Presupuesto creado correctamente");

        } catch (Exception e) {
            System.err.println("Error al crear presupuesto: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo crear el presupuesto: " + e.getMessage());
        }
    }

    /**
     * Elimina el presupuesto seleccionado
     */
    @FXML
    private void eliminarPresupuesto() {
        try {
            // Obtener el presupuesto seleccionado
            Presupuesto presupuestoSeleccionado = tblPresupuestos.getSelectionModel().getSelectedItem();

            // Validar que se haya seleccionado un presupuesto
            if (presupuestoSeleccionado == null) {
                sceneController.mostrarError("Error", "Debe seleccionar un presupuesto para eliminar");
                return;
            }

            // Confirmar la eliminación
            boolean confirmacion = sceneController.mostrarConfirmacion("Confirmar eliminación",
                    "¿Está seguro de que desea eliminar el presupuesto " + presupuestoSeleccionado.getNombre() + "?");

            if (!confirmacion) {
                return;
            }

            // Eliminar el presupuesto
            billeteraService.eliminarPresupuestoUsuario(usuarioActual, presupuestoSeleccionado);

            // Actualizar la tabla de presupuestos
            cargarPresupuestos();

            // Mostrar mensaje de éxito
            sceneController.mostrarInformacion("Éxito", "Presupuesto eliminado correctamente");

        } catch (Exception e) {
            System.err.println("Error al eliminar presupuesto: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudo eliminar el presupuesto: " + e.getMessage());
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
