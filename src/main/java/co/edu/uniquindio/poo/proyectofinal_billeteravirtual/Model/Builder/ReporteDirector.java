package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Director para el patrón Builder de Reportes
 * Se encarga de dirigir la construcción de diferentes tipos de reportes
 */
public class ReporteDirector {
    private final ReporteBuilder builder;
    private final BilleteraService billeteraService;
    private final DataManager dataManager;

    /**
     * Constructor que recibe un builder específico
     * @param builder Builder a utilizar
     */
    public ReporteDirector(ReporteBuilder builder) {
        this.builder = builder;
        this.billeteraService = BilleteraService.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Construye un reporte completo con todos los detalles
     * @param usuario Usuario para el reporte
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @param formato Formato del reporte
     * @return Reporte construido
     */
    public Reporte construirReporteCompleto(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, FormatoReporte formato) {
        List<TransaccionFactory> transacciones = filtrarTransaccionesPorFecha(
                billeteraService.obtenerTransaccionesUsuario(usuario), fechaInicio, fechaFin);

        List<Categoria> categorias = dataManager.getCategorias();

        return builder
                .setTitulo("Reporte Financiero Completo")
                .setUsuario(usuario)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setTransacciones(transacciones)
                .setCategorias(categorias)
                .incluirResumenTransacciones(true)
                .incluirAnalisisPorCategoria(true)
                .incluirGraficos(true)
                .setFormato(formato)
                .build();
    }

    /**
     * Construye un reporte resumido con información básica
     * @param usuario Usuario para el reporte
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @param formato Formato del reporte
     * @return Reporte construido
     */
    public Reporte construirReporteResumido(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, FormatoReporte formato) {
        List<TransaccionFactory> transacciones = filtrarTransaccionesPorFecha(
                billeteraService.obtenerTransaccionesUsuario(usuario), fechaInicio, fechaFin);

        return builder
                .setTitulo("Resumen Financiero")
                .setUsuario(usuario)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setTransacciones(transacciones)
                .incluirResumenTransacciones(true)
                .incluirAnalisisPorCategoria(false)
                .incluirGraficos(false)
                .setFormato(formato)
                .build();
    }

    /**
     * Construye un reporte por categorías
     * @param usuario Usuario para el reporte
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @param formato Formato del reporte
     * @return Reporte construido
     */
    public Reporte construirReportePorCategorias(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, FormatoReporte formato) {
        List<TransaccionFactory> transacciones = filtrarTransaccionesPorFecha(
                billeteraService.obtenerTransaccionesUsuario(usuario), fechaInicio, fechaFin);

        List<Categoria> categorias = dataManager.getCategorias();

        return builder
                .setTitulo("Análisis por Categorías")
                .setUsuario(usuario)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setTransacciones(transacciones)
                .setCategorias(categorias)
                .incluirResumenTransacciones(false)
                .incluirAnalisisPorCategoria(true)
                .incluirGraficos(true)
                .setFormato(formato)
                .build();
    }

    /**
     * Filtra las transacciones por fecha
     * @param transacciones Lista de transacciones a filtrar
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de transacciones filtradas
     */
    private List<TransaccionFactory> filtrarTransaccionesPorFecha(List<TransaccionFactory> transacciones,
                                                                 LocalDate fechaInicio,
                                                                 LocalDate fechaFin) {
        return transacciones.stream()
                .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) &&
                             !t.getFechaTransaccion().isAfter(fechaFin))
                .collect(Collectors.toList());
    }
}
