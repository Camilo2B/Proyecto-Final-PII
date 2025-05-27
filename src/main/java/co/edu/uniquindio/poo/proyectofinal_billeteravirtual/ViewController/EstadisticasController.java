package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de estadísticas
 */
public class EstadisticasController {

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private PieChart chartGastosPorCategoria;

    @FXML
    private BarChart<String, Number> chartIngresosVsGastos;

    @FXML
    private LineChart<String, Number> chartEvolucionSaldo;

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

            // Configurar los DatePickers
            configurarDatePickers();

            // Actualizar la interfaz con los datos del usuario
            actualizarInterfaz();

        } catch (Exception e) {
            System.err.println("Error al inicializar EstadisticasController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configura los DatePickers
     */
    private void configurarDatePickers() {
        // Configurar fecha inicial (primer día del mes actual)
        LocalDate primerDiaMes = LocalDate.now().withDayOfMonth(1);
        dpFechaInicio.setValue(primerDiaMes);

        // Configurar fecha final (día actual)
        LocalDate hoy = LocalDate.now();
        dpFechaFin.setValue(hoy);
    }

    /**
     * Actualiza la interfaz con los datos del usuario
     */
    private void actualizarInterfaz() {
        lblUsuario.setText(usuarioActual.getNombre());
        lblSaldo.setText("Saldo: $" + String.format("%.2f", usuarioActual.getSaldoTotal()));
    }

    /**
     * Genera las estadísticas
     */
    @FXML
    private void generarEstadisticas() {
        try {
            // Validar fechas
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            if (fechaInicio == null || fechaFin == null) {
                sceneController.mostrarError("Error", "Debe seleccionar fechas de inicio y fin");
                return;
            }

            if (fechaInicio.isAfter(fechaFin)) {
                sceneController.mostrarError("Error", "La fecha de inicio no puede ser posterior a la fecha de fin");
                return;
            }

            // Obtener transacciones en el rango de fechas
            List<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesEnRango(usuarioActual, fechaInicio, fechaFin);

            if (transacciones.isEmpty()) {
                sceneController.mostrarInformacion("Sin datos", "No hay transacciones en el rango de fechas seleccionado");
                limpiarGraficos();
                return;
            }

            // Generar gráficos
            generarGraficoGastosPorCategoria(transacciones);
            generarGraficoIngresosVsGastos(transacciones);
            generarGraficoEvolucionSaldo(transacciones);

        } catch (Exception e) {
            System.err.println("Error al generar estadísticas: " + e.getMessage());
            e.printStackTrace();
            sceneController.mostrarError("Error", "No se pudieron generar las estadísticas: " + e.getMessage());
        }
    }

    /**
     * Limpia los gráficos
     */
    private void limpiarGraficos() {
        chartGastosPorCategoria.getData().clear();
        chartIngresosVsGastos.getData().clear();
        chartEvolucionSaldo.getData().clear();
    }

    /**
     * Genera el gráfico de gastos por categoría
     * @param transacciones Lista de transacciones
     */
    private void generarGraficoGastosPorCategoria(List<TransaccionFactory> transacciones) {
        // Limpiar datos anteriores
        chartGastosPorCategoria.getData().clear();

        // Filtrar solo los gastos
        List<TransaccionFactory> gastos = transacciones.stream()
                .filter(t -> t.getMonto() < 0)
                .collect(Collectors.toList());

        if (gastos.isEmpty()) {
            return;
        }

        // Agrupar por categoría
        Map<String, Double> gastosPorCategoria = new HashMap<>();

        for (TransaccionFactory gasto : gastos) {
            String nombreCategoria = gasto.getCategoria() != null ? gasto.getCategoria().getNombre() : "Sin categoría";
            double montoAbsoluto = Math.abs(gasto.getMonto());

            gastosPorCategoria.put(
                nombreCategoria,
                gastosPorCategoria.getOrDefault(nombreCategoria, 0.0) + montoAbsoluto
            );
        }

        // Crear datos para el gráfico
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()), entry.getValue()));
        }

        // Actualizar el gráfico
        chartGastosPorCategoria.setData(pieChartData);
        chartGastosPorCategoria.setTitle("Gastos por Categoría");
    }

    /**
     * Genera el gráfico de ingresos vs gastos
     * @param transacciones Lista de transacciones
     */
    private void generarGraficoIngresosVsGastos(List<TransaccionFactory> transacciones) {
        // Limpiar datos anteriores
        chartIngresosVsGastos.getData().clear();

        // Calcular ingresos y gastos por mes
        Map<String, Double> ingresosPorMes = new HashMap<>();
        Map<String, Double> gastosPorMes = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (TransaccionFactory transaccion : transacciones) {
            String mes = transaccion.getFechaTransaccion().format(formatter);
            double monto = transaccion.getMonto();

            if (monto > 0) {
                // Es un ingreso
                ingresosPorMes.put(mes, ingresosPorMes.getOrDefault(mes, 0.0) + monto);
            } else {
                // Es un gasto
                gastosPorMes.put(mes, gastosPorMes.getOrDefault(mes, 0.0) + Math.abs(monto));
            }
        }

        // Crear series para ingresos y gastos
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos");

        XYChart.Series<String, Number> seriesGastos = new XYChart.Series<>();
        seriesGastos.setName("Gastos");

        // Obtener todos los meses únicos
        List<String> meses = transacciones.stream()
                .map(t -> t.getFechaTransaccion().format(formatter))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Agregar datos a las series
        for (String mes : meses) {
            seriesIngresos.getData().add(new XYChart.Data<>(mes, ingresosPorMes.getOrDefault(mes, 0.0)));
            seriesGastos.getData().add(new XYChart.Data<>(mes, gastosPorMes.getOrDefault(mes, 0.0)));
        }

        // Actualizar el gráfico
        chartIngresosVsGastos.getData().addAll(seriesIngresos, seriesGastos);
        chartIngresosVsGastos.setTitle("Ingresos vs Gastos por Mes");
    }

    /**
     * Genera el gráfico de evolución del saldo
     * @param transacciones Lista de transacciones
     */
    private void generarGraficoEvolucionSaldo(List<TransaccionFactory> transacciones) {
        // Limpiar datos anteriores
        chartEvolucionSaldo.getData().clear();

        // Ordenar transacciones por fecha
        List<TransaccionFactory> transaccionesOrdenadas = transacciones.stream()
                .sorted((t1, t2) -> t1.getFechaTransaccion().compareTo(t2.getFechaTransaccion()))
                .collect(Collectors.toList());

        if (transaccionesOrdenadas.isEmpty()) {
            return;
        }

        // Crear serie para la evolución del saldo
        XYChart.Series<String, Number> seriesSaldo = new XYChart.Series<>();
        seriesSaldo.setName("Saldo");

        // Calcular saldo acumulado
        double saldoInicial = 0; // Idealmente, deberíamos obtener el saldo al inicio del período
        double saldoAcumulado = saldoInicial;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Agregar punto inicial
        seriesSaldo.getData().add(new XYChart.Data<>(
            transaccionesOrdenadas.get(0).getFechaTransaccion().minusDays(1).format(formatter),
            saldoInicial
        ));

        // Agregar puntos para cada transacción
        for (TransaccionFactory transaccion : transaccionesOrdenadas) {
            saldoAcumulado += transaccion.getMonto();
            seriesSaldo.getData().add(new XYChart.Data<>(
                transaccion.getFechaTransaccion().format(formatter),
                saldoAcumulado
            ));
        }

        // Actualizar el gráfico
        chartEvolucionSaldo.getData().add(seriesSaldo);
        chartEvolucionSaldo.setTitle("Evolución del Saldo");
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
