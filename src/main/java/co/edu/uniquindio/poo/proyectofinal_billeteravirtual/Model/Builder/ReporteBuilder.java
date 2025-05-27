package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz Builder para la construcción de reportes financieros.
 *
 * <p>Implementa el patrón Builder para crear reportes personalizados con diferentes
 * configuraciones y contenidos. Este patrón permite construir objetos complejos
 * paso a paso, separando el proceso de construcción de la representación final.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>Permite configurar diferentes aspectos del reporte de manera independiente</li>
 *   <li>Facilita la creación de reportes con diferentes niveles de detalle</li>
 *   <li>Soporta múltiples formatos de salida (PDF, CSV, etc.)</li>
 *   <li>Permite incluir o excluir secciones específicas del reporte</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 * ReporteBuilder builder = new ReporteConcreteBuilder();
 * Reporte reporte = builder
 *     .setTitulo("Reporte Mensual")
 *     .setUsuario(usuario)
 *     .setFechaInicio(LocalDate.now().minusMonths(1))
 *     .setFechaFin(LocalDate.now())
 *     .incluirResumenTransacciones(true)
 *     .incluirGraficos(true)
 *     .setFormato(FormatoReporte.PDF)
 *     .build();
 * </pre>
 *
 * @see Reporte
 * @see ReporteConcreteBuilder
 * @see FormatoReporte
 */
public interface ReporteBuilder {

    /**
     * Establece el título del reporte
     * @param titulo Título del reporte
     * @return Builder para encadenamiento
     */
    ReporteBuilder setTitulo(String titulo);

    /**
     * Establece el usuario para el reporte
     * @param usuario Usuario para el reporte
     * @return Builder para encadenamiento
     */
    ReporteBuilder setUsuario(Usuario usuario);

    /**
     * Establece la fecha de inicio para el reporte
     * @param fechaInicio Fecha de inicio
     * @return Builder para encadenamiento
     */
    ReporteBuilder setFechaInicio(LocalDate fechaInicio);

    /**
     * Establece la fecha de fin para el reporte
     * @param fechaFin Fecha de fin
     * @return Builder para encadenamiento
     */
    ReporteBuilder setFechaFin(LocalDate fechaFin);

    /**
     * Establece las transacciones para el reporte
     * @param transacciones Lista de transacciones
     * @return Builder para encadenamiento
     */
    ReporteBuilder setTransacciones(List<TransaccionFactory> transacciones);

    /**
     * Establece las categorías para el reporte
     * @param categorias Lista de categorías
     * @return Builder para encadenamiento
     */
    ReporteBuilder setCategorias(List<Categoria> categorias);

    /**
     * Incluye un resumen de transacciones en el reporte
     * @param incluir True para incluir, false para no incluir
     * @return Builder para encadenamiento
     */
    ReporteBuilder incluirResumenTransacciones(boolean incluir);

    /**
     * Incluye un análisis por categoría en el reporte
     * @param incluir True para incluir, false para no incluir
     * @return Builder para encadenamiento
     */
    ReporteBuilder incluirAnalisisPorCategoria(boolean incluir);

    /**
     * Incluye gráficos en el reporte
     * @param incluir True para incluir, false para no incluir
     * @return Builder para encadenamiento
     */
    ReporteBuilder incluirGraficos(boolean incluir);

    /**
     * Establece el formato del reporte (PDF, CSV, etc.)
     * @param formato Formato del reporte
     * @return Builder para encadenamiento
     */
    ReporteBuilder setFormato(FormatoReporte formato);

    /**
     * Construye el reporte con los parámetros establecidos
     * @return Reporte construido
     */
    Reporte build();
}
