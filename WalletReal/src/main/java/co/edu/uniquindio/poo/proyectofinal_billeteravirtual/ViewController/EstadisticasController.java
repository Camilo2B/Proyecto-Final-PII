package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de estadísticas
 */
public class EstadisticasController {

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblSaldo;

    @FXML
    private ComboBox<String> cmbPeriodo;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private Button btnActualizar;

    @FXML
    private BarChart<String, Number> barChartCategorias;

    @FXML
    private CategoryAxis xAxisCategorias;

    @FXML
    private NumberAxis yAxisCategorias;

    @FXML
    private PieChart pieChartGastos;

    @FXML
    private LineChart<String, Number> lineChartTendencia;

    @FXML
    private CategoryAxis xAxisTendencia;

    @FXML
    private NumberAxis yAxisTendencia;

    private SceneController sceneController;
    private AuthenticationService authService;
    private BilleteraService billeteraService;
    private DataManager dataManager;
    private Usuario usuarioActual;
    private Administrador adminActual;

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        sceneController = SceneController.getInstance();
        authService = AuthenticationService.getInstance();
        billeteraService = new BilleteraService();
        dataManager = DataManager.getInstance();
        
        // Verificar si hay un usuario o administrador autenticado
        usuarioActual = authService.getUsuarioAutenticado();
        adminActual = authService.getAdminAutenticado();
        
        if (usuarioActual == null && adminActual == null) {
            // Si no hay usuario autenticado, redirigir a la vista de inicio de sesión
            try {
                sceneController.cambiarEscena(SceneController.VISTA_SESION);
                return;
            } catch (IOException e) {
                sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Configurar la información del usuario o administrador
        if (usuarioActual != null) {
            lblNombreUsuario.setText(usuarioActual.getNombre());
            lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
        } else if (adminActual != null) {
            lblNombreUsuario.setText(adminActual.getNombre() + " (Admin)");
            lblSaldo.setText("");
        }
        
        // Configurar el combo box de períodos
        cmbPeriodo.setItems(FXCollections.observableArrayList(
            "Última semana", "Último mes", "Últimos 3 meses", "Último año", "Personalizado"
        ));
        
        cmbPeriodo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                configurarFechasPorPeriodo(newVal);
            }
        });
        
        // Configurar los date pickers
        dpFechaInicio.setValue(LocalDate.now().minusMonths(1));
        dpFechaFin.setValue(LocalDate.now());
        
        // Seleccionar "Último mes" por defecto
        cmbPeriodo.getSelectionModel().select("Último mes");
        
        // Inicializar los gráficos
        inicializarGraficos();
    }
    
    /**
     * Configura las fechas según el período seleccionado
     * @param periodo Período seleccionado
     */
    private void configurarFechasPorPeriodo(String periodo) {
        LocalDate hoy = LocalDate.now();
        
        switch (periodo) {
            case "Última semana":
                dpFechaInicio.setValue(hoy.minusWeeks(1));
                dpFechaFin.setValue(hoy);
                break;
            case "Último mes":
                dpFechaInicio.setValue(hoy.minusMonths(1));
                dpFechaFin.setValue(hoy);
                break;
            case "Últimos 3 meses":
                dpFechaInicio.setValue(hoy.minusMonths(3));
                dpFechaFin.setValue(hoy);
                break;
            case "Último año":
                dpFechaInicio.setValue(hoy.minusYears(1));
                dpFechaFin.setValue(hoy);
                break;
            case "Personalizado":
                // No hacer nada, dejar que el usuario seleccione las fechas
                break;
        }
        
        // Si no es personalizado, deshabilitar los date pickers
        boolean esPersonalizado = "Personalizado".equals(periodo);
        dpFechaInicio.setDisable(!esPersonalizado);
        dpFechaFin.setDisable(!esPersonalizado);
        
        // Actualizar los gráficos
        actualizarGraficos();
    }
    
    /**
     * Inicializa los gráficos
     */
    private void inicializarGraficos() {
        // Configurar ejes
        xAxisCategorias.setLabel("Categoría");
        yAxisCategorias.setLabel("Monto ($)");
        
        xAxisTendencia.setLabel("Fecha");
        yAxisTendencia.setLabel("Monto ($)");
        
        // Actualizar los gráficos
        actualizarGraficos();
    }
    
    /**
     * Actualiza los gráficos con los datos actuales
     */
    @FXML
    public void actualizarGraficos() {
        actualizarGraficoBarras();
        actualizarGraficoPie();
        actualizarGraficoLinea();
    }
    
    /**
     * Actualiza el gráfico de barras (gastos por categoría)
     */
    private void actualizarGraficoBarras() {
        // Limpiar datos anteriores
        barChartCategorias.getData().clear();
        
        // Obtener transacciones filtradas por fecha
        List<TransaccionFactory> transacciones = obtenerTransaccionesFiltradas();
        
        // Agrupar transacciones por categoría y sumar montos
        Map<String, Double> gastosPorCategoria = transacciones.stream()
                .filter(t -> t.getTipoTransaccion() == TipoTransaccion.RETIRO || t.getTipoTransaccion() == TipoTransaccion.TRANSFERENCIA)
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria().getNombre(),
                        Collectors.summingDouble(TransaccionFactory::getMonto)
                ));
        
        // Crear serie de datos
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos");
        
        // Agregar datos a la serie
        gastosPorCategoria.forEach((categoria, monto) -> {
            series.getData().add(new XYChart.Data<>(categoria, monto));
        });
        
        // Agregar serie al gráfico
        barChartCategorias.getData().add(series);
    }
    
    /**
     * Actualiza el gráfico de pie (distribución de gastos)
     */
    private void actualizarGraficoPie() {
        // Limpiar datos anteriores
        pieChartGastos.getData().clear();
        
        // Obtener transacciones filtradas por fecha
        List<TransaccionFactory> transacciones = obtenerTransaccionesFiltradas();
        
        // Agrupar transacciones por categoría y sumar montos
        Map<String, Double> gastosPorCategoria = transacciones.stream()
                .filter(t -> t.getTipoTransaccion() == TipoTransaccion.RETIRO || t.getTipoTransaccion() == TipoTransaccion.TRANSFERENCIA)
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria().getNombre(),
                        Collectors.summingDouble(TransaccionFactory::getMonto)
                ));
        
        // Crear datos para el gráfico de pie
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        // Agregar datos al gráfico
        gastosPorCategoria.forEach((categoria, monto) -> {
            pieChartData.add(new PieChart.Data(categoria + " ($" + String.format("%.2f", monto) + ")", monto));
        });
        
        // Agregar datos al gráfico
        pieChartGastos.setData(pieChartData);
    }
    
    /**
     * Actualiza el gráfico de línea (tendencia de gastos e ingresos)
     */
    private void actualizarGraficoLinea() {
        // Limpiar datos anteriores
        lineChartTendencia.getData().clear();
        
        // Obtener transacciones filtradas por fecha
        List<TransaccionFactory> transacciones = obtenerTransaccionesFiltradas();
        
        // Agrupar transacciones por fecha y tipo, y sumar montos
        Map<LocalDate, Map<TipoTransaccion, Double>> transaccionesPorFecha = new TreeMap<>();
        
        for (TransaccionFactory transaccion : transacciones) {
            LocalDate fecha = transaccion.getFechaTransaccion();
            TipoTransaccion tipo = transaccion.getTipoTransaccion();
            double monto = transaccion.getMonto();
            
            transaccionesPorFecha.putIfAbsent(fecha, new HashMap<>());
            Map<TipoTransaccion, Double> porTipo = transaccionesPorFecha.get(fecha);
            
            porTipo.put(tipo, porTipo.getOrDefault(tipo, 0.0) + monto);
        }
        
        // Crear series de datos
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos");
        
        XYChart.Series<String, Number> seriesGastos = new XYChart.Series<>();
        seriesGastos.setName("Gastos");
        
        // Formato para las fechas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        // Agregar datos a las series
        for (Map.Entry<LocalDate, Map<TipoTransaccion, Double>> entry : transaccionesPorFecha.entrySet()) {
            String fechaStr = entry.getKey().format(formatter);
            Map<TipoTransaccion, Double> porTipo = entry.getValue();
            
            // Sumar ingresos (depósitos)
            double ingresos = porTipo.getOrDefault(TipoTransaccion.DEPOSITO, 0.0);
            if (ingresos > 0) {
                seriesIngresos.getData().add(new XYChart.Data<>(fechaStr, ingresos));
            }
            
            // Sumar gastos (retiros y transferencias)
            double gastos = porTipo.getOrDefault(TipoTransaccion.RETIRO, 0.0) + 
                           porTipo.getOrDefault(TipoTransaccion.TRANSFERENCIA, 0.0);
            if (gastos > 0) {
                seriesGastos.getData().add(new XYChart.Data<>(fechaStr, gastos));
            }
        }
        
        // Agregar series al gráfico
        lineChartTendencia.getData().addAll(seriesIngresos, seriesGastos);
    }
    
    /**
     * Obtiene las transacciones filtradas por fecha
     * @return Lista de transacciones filtradas
     */
    private List<TransaccionFactory> obtenerTransaccionesFiltradas() {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();
        
        // Obtener todas las transacciones
        List<TransaccionFactory> todasLasTransacciones;
        
        if (usuarioActual != null) {
            // Si es usuario, obtener solo sus transacciones
            todasLasTransacciones = billeteraService.obtenerTransacciones();
        } else {
            // Si es administrador, obtener todas las transacciones
            todasLasTransacciones = new ArrayList<>(dataManager.getTransacciones());
        }
        
        // Filtrar por fecha
        return todasLasTransacciones.stream()
                .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) && !t.getFechaTransaccion().isAfter(fechaFin))
                .collect(Collectors.toList());
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
     * Navega a la vista de presupuestos
     */
    @FXML
    private void irAPresupuestos() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_PRESUPUESTOS);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de presupuestos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la vista de estadísticas (actual)
     */
    @FXML
    private void mostrarEstadisticas() {
        // Ya estamos en la vista de estadísticas, no es necesario hacer nada
    }
    
    /**
     * Navega a la vista de reportes
     */
    @FXML
    private void irAReportes() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_REPORTES);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de reportes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    @FXML
    private void cerrarSesion() {
        authService.cerrarSesion();
        try {
            sceneController.cambiarEscena(SceneController.VISTA_SESION);
        } catch (IOException e) {
            sceneController.mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
