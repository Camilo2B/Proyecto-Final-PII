package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.*;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.util.PerformanceOptimizer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Controlador optimizado para la vista de estadísticas
 * Implementa técnicas de optimización de rendimiento para mejorar la experiencia del usuario
 */
public class EstadisticasControllerOptimizado {

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
    private ProgressIndicator progressIndicator;

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
    private BilleteraService billeteraService;
    private DataManager dataManager;
    private Usuario usuarioActual;

    // Caché para evitar recálculos innecesarios
    private Map<String, Object> cacheResultados = new HashMap<>();

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        sceneController = SceneController.getInstance();
        billeteraService = new BilleteraService();
        dataManager = DataManager.getInstance();

        // Verificar si hay un usuario logueado
        if (!billeteraService.hayUsuarioLogueado()) {
            // Si no hay usuario autenticado, redirigir a la vista de inicio de sesión
            try {
                sceneController.cambiarEscena(SceneController.VISTA_SESION);
                return;
            } catch (IOException e) {
                mostrarError("Error", "No se pudo cargar la vista de inicio de sesión: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Configurar la información del usuario
        usuarioActual = billeteraService.getUsuarioActual();
        if (usuarioActual != null) {
            lblNombreUsuario.setText(usuarioActual.getNombre());
            lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
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

        // Ocultar el indicador de progreso inicialmente
        progressIndicator.setVisible(false);

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
     * Actualiza los gráficos con los datos actuales de manera asíncrona
     */
    @FXML
    public void actualizarGraficos() {
        // Mostrar indicador de progreso
        progressIndicator.setVisible(true);

        // Crear una tarea en segundo plano para no bloquear la interfaz de usuario
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Obtener transacciones filtradas
                    List<TransaccionFactory> transacciones = obtenerTransaccionesFiltradas();

                    // Procesar datos para gráficos
                    Map<String, Object> datosBarras;
                    Map<String, Object> datosPie;
                    Map<String, Object> datosLinea;

                    try {
                        // Intentar usar procesamiento asíncrono
                        CompletableFuture<Map<String, Object>> futuroBarras =
                                PerformanceOptimizer.procesarDatosGraficoAsync(transacciones, "barras");

                        CompletableFuture<Map<String, Object>> futuroPie =
                                PerformanceOptimizer.procesarDatosGraficoAsync(transacciones, "circular");

                        CompletableFuture<Map<String, Object>> futuroLinea =
                                PerformanceOptimizer.procesarDatosGraficoAsync(transacciones, "linea");

                        // Esperar a que todos los futuros se completen
                        CompletableFuture.allOf(futuroBarras, futuroPie, futuroLinea).join();

                        // Obtener resultados
                        datosBarras = futuroBarras.get();
                        datosPie = futuroPie.get();
                        datosLinea = futuroLinea.get();
                    } catch (Exception e) {
                        // Fallback: procesar datos de manera síncrona
                        datosBarras = procesarDatosGraficoBarras(transacciones);
                        datosPie = procesarDatosGraficoPie(transacciones);
                        datosLinea = procesarDatosGraficoLinea(transacciones);
                    }

                    // Resultados finales para usar en el runLater
                    final Map<String, Object> finalDatosBarras = datosBarras;
                    final Map<String, Object> finalDatosPie = datosPie;
                    final Map<String, Object> finalDatosLinea = datosLinea;

                    // Actualizar la interfaz de usuario en el hilo de JavaFX
                    Platform.runLater(() -> {
                        try {
                            actualizarGraficoBarras((Map<String, Double>) finalDatosBarras.get("datos"));
                            actualizarGraficoPie((Map<String, Double>) finalDatosPie.get("datos"));
                            actualizarGraficoLinea((Map<LocalDate, Map<String, Double>>) finalDatosLinea.get("datos"));
                        } catch (Exception e) {
                            mostrarError("Error", "Error al actualizar gráficos: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            // Ocultar indicador de progreso
                            progressIndicator.setVisible(false);
                        }
                    });
                } catch (Exception e) {
                    // Capturar cualquier excepción no manejada
                    Platform.runLater(() -> {
                        mostrarError("Error", "Error inesperado: " + e.getMessage());
                        e.printStackTrace();
                        progressIndicator.setVisible(false);
                    });
                }

                return null;
            }
        };

        // Manejar errores
        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            mostrarError("Error", "Error al actualizar gráficos: " + exception.getMessage());
            exception.printStackTrace();
            progressIndicator.setVisible(false);
        });

        // Ejecutar la tarea en un hilo separado
        new Thread(task).start();
    }

    /**
     * Procesa los datos para el gráfico de barras de manera síncrona
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos procesados
     */
    private Map<String, Object> procesarDatosGraficoBarras(List<TransaccionFactory> transacciones) {
        Map<String, Object> datos = new HashMap<>();
        Map<String, Double> datosPorCategoria = new HashMap<>();

        for (TransaccionFactory transaccion : transacciones) {
            if (transaccion.getCategoria() != null) {
                String categoria = transaccion.getCategoria().getNombre();
                double montoActual = datosPorCategoria.getOrDefault(categoria, 0.0);
                datosPorCategoria.put(categoria, montoActual + transaccion.getMonto());
            }
        }

        datos.put("datos", datosPorCategoria);
        return datos;
    }

    /**
     * Procesa los datos para el gráfico de pie de manera síncrona
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos procesados
     */
    private Map<String, Object> procesarDatosGraficoPie(List<TransaccionFactory> transacciones) {
        Map<String, Object> datos = new HashMap<>();
        Map<String, Double> porcentajes = new HashMap<>();

        double total = transacciones.stream()
                .mapToDouble(TransaccionFactory::getMonto)
                .sum();

        for (TransaccionFactory transaccion : transacciones) {
            if (transaccion.getCategoria() != null) {
                String categoria = transaccion.getCategoria().getNombre();
                double montoActual = porcentajes.getOrDefault(categoria, 0.0);
                porcentajes.put(categoria, montoActual + transaccion.getMonto());
            }
        }

        // Convertir montos a porcentajes
        for (Map.Entry<String, Double> entry : porcentajes.entrySet()) {
            porcentajes.put(entry.getKey(), (entry.getValue() / total) * 100);
        }

        datos.put("datos", porcentajes);
        return datos;
    }

    /**
     * Procesa los datos para el gráfico de línea de manera síncrona
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos procesados
     */
    private Map<String, Object> procesarDatosGraficoLinea(List<TransaccionFactory> transacciones) {
        Map<String, Object> datos = new HashMap<>();
        Map<LocalDate, Map<String, Double>> datosPorFecha = new HashMap<>();

        for (TransaccionFactory transaccion : transacciones) {
            LocalDate fecha = transaccion.getFechaTransaccion();

            if (!datosPorFecha.containsKey(fecha)) {
                Map<String, Double> valores = new HashMap<>();
                valores.put("ingresos", 0.0);
                valores.put("gastos", 0.0);
                datosPorFecha.put(fecha, valores);
            }

            Map<String, Double> valores = datosPorFecha.get(fecha);

            switch (transaccion.getTipoTransaccion()) {
                case DEPOSITO:
                    valores.put("ingresos", valores.get("ingresos") + transaccion.getMonto());
                    break;
                case RETIRO:
                    valores.put("gastos", valores.get("gastos") + transaccion.getMonto());
                    break;
                default:
                    // Otros tipos de transacción
                    break;
            }
        }

        datos.put("datos", datosPorFecha);
        return datos;
    }

    /**
     * Actualiza el gráfico de barras con los datos proporcionados
     * @param gastosPorCategoria Mapa de gastos por categoría
     */
    private void actualizarGraficoBarras(Map<String, Double> gastosPorCategoria) {
        // Limpiar datos anteriores
        barChartCategorias.getData().clear();

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
     * Actualiza el gráfico de pie con los datos proporcionados
     * @param gastosPorCategoria Mapa de gastos por categoría
     */
    private void actualizarGraficoPie(Map<String, Double> gastosPorCategoria) {
        // Limpiar datos anteriores
        pieChartGastos.getData().clear();

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
     * Actualiza el gráfico de línea con los datos proporcionados
     * @param datosPorFecha Mapa de datos por fecha
     */
    private void actualizarGraficoLinea(Map<LocalDate, Map<String, Double>> datosPorFecha) {
        // Limpiar datos anteriores
        lineChartTendencia.getData().clear();

        // Crear series de datos
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos");

        XYChart.Series<String, Number> seriesGastos = new XYChart.Series<>();
        seriesGastos.setName("Gastos");

        // Formato para las fechas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        // Agregar datos a las series
        for (Map.Entry<LocalDate, Map<String, Double>> entry : datosPorFecha.entrySet()) {
            String fechaStr = entry.getKey().format(formatter);
            Map<String, Double> valores = entry.getValue();

            // Agregar ingresos
            double ingresos = valores.getOrDefault("ingresos", 0.0);
            if (ingresos > 0) {
                seriesIngresos.getData().add(new XYChart.Data<>(fechaStr, ingresos));
            }

            // Agregar gastos
            double gastos = valores.getOrDefault("gastos", 0.0);
            if (gastos > 0) {
                seriesGastos.getData().add(new XYChart.Data<>(fechaStr, gastos));
            }
        }

        // Agregar series al gráfico
        lineChartTendencia.getData().addAll(seriesIngresos, seriesGastos);
    }

    /**
     * Obtiene las transacciones filtradas por fecha de manera optimizada
     * @return Lista de transacciones filtradas
     */
    private List<TransaccionFactory> obtenerTransaccionesFiltradas() {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        // Clave para la caché
        String cacheKey = "transacciones_" + fechaInicio + "_" + fechaFin;

        // Verificar si el resultado está en caché
        if (cacheResultados.containsKey(cacheKey)) {
            return (List<TransaccionFactory>) cacheResultados.get(cacheKey);
        }

        // Obtener todas las transacciones
        List<TransaccionFactory> todasLasTransacciones = billeteraService.obtenerTransaccionesUsuario();

        // Filtrar manualmente si PerformanceOptimizer no está disponible
        List<TransaccionFactory> resultado;
        try {
            // Intentar usar PerformanceOptimizer
            resultado = PerformanceOptimizer.filtrarTransaccionesPorFecha(
                    todasLasTransacciones, fechaInicio, fechaFin);
        } catch (Exception e) {
            // Fallback: filtrar manualmente
            resultado = todasLasTransacciones.stream()
                    .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) &&
                                !t.getFechaTransaccion().isAfter(fechaFin))
                    .collect(Collectors.toList());
        }

        // Guardar en caché
        cacheResultados.put(cacheKey, resultado);

        return resultado;
    }

    /**
     * Muestra un diálogo de error
     * @param titulo Título del diálogo
     * @param mensaje Mensaje de error
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Navega al dashboard
     */
    @FXML
    private void irADashboard() {
        try {
            sceneController.cambiarEscena(SceneController.VISTA_DASHBOARD);
        } catch (IOException e) {
            mostrarError("Error", "No se pudo cargar el dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión actual y navega a la vista de inicio de sesión
     */
    @FXML
    private void cerrarSesion() {
        try {
            // Limpiar caché antes de cerrar sesión
            limpiarCache();

            // Cerrar sesión
            billeteraService.cerrarSesion();

            // Navegar a la vista de inicio de sesión
            sceneController.cambiarEscena(SceneController.VISTA_SESION);
        } catch (IOException e) {
            mostrarError("Error", "No se pudo cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Limpia la caché al cerrar la vista
     */
    public void limpiarCache() {
        cacheResultados.clear();
        try {
            PerformanceOptimizer.limpiarCache();
        } catch (Exception e) {
            // Ignorar errores al limpiar la caché del optimizador
            System.err.println("Error al limpiar la caché del optimizador: " + e.getMessage());
        }
    }

    /**
     * Establece el servicio de billetera (para pruebas)
     * @param billeteraService Servicio de billetera
     */
    public void setBilleteraService(BilleteraService billeteraService) {
        this.billeteraService = billeteraService;
    }

    /**
     * Establece el controlador de escenas (para pruebas)
     * @param sceneController Controlador de escenas
     */
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
