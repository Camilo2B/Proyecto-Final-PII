package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.util;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Clase utilitaria para optimizar el rendimiento de operaciones costosas.
 * Implementa técnicas como procesamiento asíncrono, caché y procesamiento paralelo
 * para mejorar el rendimiento de la aplicación, especialmente con grandes conjuntos de datos.
 */
public class PerformanceOptimizer {

    // Caché para resultados de operaciones costosas
    private static final Map<String, Object> cache = new HashMap<>();

    // Servicio de ejecución para procesamiento asíncrono
    private static final ExecutorService executorService = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() - 1)
    );

    /**
     * Filtra transacciones por fecha de manera optimizada.
     * Utiliza procesamiento paralelo para grandes conjuntos de datos.
     *
     * @param transacciones Lista de transacciones a filtrar
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Lista filtrada de transacciones
     */
    public static List<TransaccionFactory> filtrarTransaccionesPorFecha(
            List<TransaccionFactory> transacciones, LocalDate fechaInicio, LocalDate fechaFin) {

        // Clave para la caché
        String cacheKey = "transacciones_" + fechaInicio + "_" + fechaFin + "_" + transacciones.size();

        // Verificar si el resultado está en caché
        if (cache.containsKey(cacheKey)) {
            return (List<TransaccionFactory>) cache.get(cacheKey);
        }

        // Usar procesamiento paralelo para conjuntos grandes
        List<TransaccionFactory> resultado;
        if (transacciones.size() > 1000) {
            resultado = transacciones.parallelStream()
                    .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) &&
                                !t.getFechaTransaccion().isAfter(fechaFin))
                    .collect(Collectors.toList());
        } else {
            resultado = transacciones.stream()
                    .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) &&
                                !t.getFechaTransaccion().isAfter(fechaFin))
                    .collect(Collectors.toList());
        }

        // Guardar en caché
        cache.put(cacheKey, resultado);

        return resultado;
    }

    /**
     * Calcula estadísticas por categoría de manera asíncrona.
     *
     * @param transacciones Lista de transacciones
     * @param categorias Lista de categorías
     * @return CompletableFuture con un mapa de estadísticas por categoría
     */
    public static CompletableFuture<Map<String, Double>> calcularEstadisticasPorCategoriaAsync(
            List<TransaccionFactory> transacciones, List<Categoria> categorias) {

        return CompletableFuture.supplyAsync(() -> {
            Map<String, Double> resultado = new HashMap<>();

            for (Categoria categoria : categorias) {
                String nombreCategoria = categoria.getNombre();
                double total = transacciones.stream()
                        .filter(t -> t.getCategoria() != null &&
                                    t.getCategoria().getNombre().equals(nombreCategoria))
                        .mapToDouble(TransaccionFactory::getMonto)
                        .sum();

                resultado.put(nombreCategoria, total);
            }

            return resultado;
        }, executorService);
    }

    /**
     * Procesa datos para gráficos de manera asíncrona.
     *
     * @param transacciones Lista de transacciones
     * @param tipoGrafico Tipo de gráfico ("barras", "circular", "linea")
     * @return CompletableFuture con los datos procesados para el gráfico
     */
    public static CompletableFuture<Map<String, Object>> procesarDatosGraficoAsync(
            List<TransaccionFactory> transacciones, String tipoGrafico) {

        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> datos = new HashMap<>();

            switch (tipoGrafico.toLowerCase()) {
                case "barras":
                    // Procesar datos para gráfico de barras
                    Map<String, Double> datosPorCategoria = new HashMap<>();
                    for (TransaccionFactory transaccion : transacciones) {
                        if (transaccion.getCategoria() != null) {
                            String categoria = transaccion.getCategoria().getNombre();
                            double montoActual = datosPorCategoria.getOrDefault(categoria, 0.0);
                            datosPorCategoria.put(categoria, montoActual + transaccion.getMonto());
                        }
                    }
                    datos.put("datos", datosPorCategoria);
                    break;

                case "circular":
                    // Procesar datos para gráfico circular
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
                    break;

                case "linea":
                    // Procesar datos para gráfico de línea
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
                    break;

                default:
                    // Tipo de gráfico no soportado
                    break;
            }

            return datos;
        }, executorService);
    }

    /**
     * Limpia la caché para liberar memoria.
     */
    public static void limpiarCache() {
        cache.clear();
    }

    /**
     * Cierra el servicio de ejecución.
     * Debe llamarse al cerrar la aplicación.
     */
    public static void cerrarExecutorService() {
        try {
            // Primero intentamos un cierre normal
            executorService.shutdown();

            // Esperamos hasta 5 segundos para que terminen las tareas pendientes
            if (!executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                // Si no terminan, forzamos el cierre
                executorService.shutdownNow();

                // Esperamos de nuevo por si acaso
                if (!executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    System.err.println("El ExecutorService no pudo terminar correctamente");
                }
            }
        } catch (InterruptedException e) {
            // Restauramos el flag de interrupción
            Thread.currentThread().interrupt();
            // Forzamos el cierre si hay interrupción
            executorService.shutdownNow();
        }
    }

    /**
     * Divide una lista grande en sublistas más pequeñas para procesamiento por lotes.
     *
     * @param <T> Tipo de elementos en la lista
     * @param lista Lista original
     * @param tamanoLote Tamaño de cada lote
     * @return Lista de sublistas
     */
    public static <T> List<List<T>> dividirEnLotes(List<T> lista, int tamanoLote) {
        List<List<T>> lotes = new ArrayList<>();

        for (int i = 0; i < lista.size(); i += tamanoLote) {
            int fin = Math.min(i + tamanoLote, lista.size());
            lotes.add(lista.subList(i, fin));
        }

        return lotes;
    }
}
