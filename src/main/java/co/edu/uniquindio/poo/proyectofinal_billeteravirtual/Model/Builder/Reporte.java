package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Clase que representa un reporte generado
 * Esta clase es construida a través del patrón Builder
 */
public class Reporte {
    private String titulo;
    private Usuario usuario;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<TransaccionFactory> transacciones;
    private List<Categoria> categorias;
    private boolean incluirResumenTransacciones;
    private boolean incluirAnalisisPorCategoria;
    private boolean incluirGraficos;
    private FormatoReporte formato;
    private String contenido;
    private String rutaArchivo;
    
    /**
     * Constructor privado para ser usado por el Builder
     */
    Reporte() {
    }
    
    /**
     * Establece el título del reporte
     * @param titulo Título del reporte
     */
    void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    /**
     * Establece el usuario del reporte
     * @param usuario Usuario del reporte
     */
    void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Establece la fecha de inicio del reporte
     * @param fechaInicio Fecha de inicio
     */
    void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    /**
     * Establece la fecha de fin del reporte
     * @param fechaFin Fecha de fin
     */
    void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    /**
     * Establece las transacciones del reporte
     * @param transacciones Lista de transacciones
     */
    void setTransacciones(List<TransaccionFactory> transacciones) {
        this.transacciones = transacciones;
    }
    
    /**
     * Establece las categorías del reporte
     * @param categorias Lista de categorías
     */
    void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    
    /**
     * Establece si se debe incluir un resumen de transacciones
     * @param incluirResumenTransacciones True para incluir, false para no incluir
     */
    void setIncluirResumenTransacciones(boolean incluirResumenTransacciones) {
        this.incluirResumenTransacciones = incluirResumenTransacciones;
    }
    
    /**
     * Establece si se debe incluir un análisis por categoría
     * @param incluirAnalisisPorCategoria True para incluir, false para no incluir
     */
    void setIncluirAnalisisPorCategoria(boolean incluirAnalisisPorCategoria) {
        this.incluirAnalisisPorCategoria = incluirAnalisisPorCategoria;
    }
    
    /**
     * Establece si se deben incluir gráficos
     * @param incluirGraficos True para incluir, false para no incluir
     */
    void setIncluirGraficos(boolean incluirGraficos) {
        this.incluirGraficos = incluirGraficos;
    }
    
    /**
     * Establece el formato del reporte
     * @param formato Formato del reporte
     */
    void setFormato(FormatoReporte formato) {
        this.formato = formato;
    }
    
    /**
     * Establece el contenido del reporte
     * @param contenido Contenido del reporte
     */
    void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    /**
     * Establece la ruta del archivo del reporte
     * @param rutaArchivo Ruta del archivo
     */
    void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    /**
     * Obtiene el título del reporte
     * @return Título del reporte
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Obtiene el usuario del reporte
     * @return Usuario del reporte
     */
    public Usuario getUsuario() {
        return usuario;
    }
    
    /**
     * Obtiene la fecha de inicio del reporte
     * @return Fecha de inicio
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    /**
     * Obtiene la fecha de fin del reporte
     * @return Fecha de fin
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    /**
     * Obtiene las transacciones del reporte
     * @return Lista de transacciones
     */
    public List<TransaccionFactory> getTransacciones() {
        return transacciones;
    }
    
    /**
     * Obtiene las categorías del reporte
     * @return Lista de categorías
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }
    
    /**
     * Verifica si se debe incluir un resumen de transacciones
     * @return True si se debe incluir, false si no
     */
    public boolean isIncluirResumenTransacciones() {
        return incluirResumenTransacciones;
    }
    
    /**
     * Verifica si se debe incluir un análisis por categoría
     * @return True si se debe incluir, false si no
     */
    public boolean isIncluirAnalisisPorCategoria() {
        return incluirAnalisisPorCategoria;
    }
    
    /**
     * Verifica si se deben incluir gráficos
     * @return True si se deben incluir, false si no
     */
    public boolean isIncluirGraficos() {
        return incluirGraficos;
    }
    
    /**
     * Obtiene el formato del reporte
     * @return Formato del reporte
     */
    public FormatoReporte getFormato() {
        return formato;
    }
    
    /**
     * Obtiene el contenido del reporte
     * @return Contenido del reporte
     */
    public String getContenido() {
        return contenido;
    }
    
    /**
     * Obtiene la ruta del archivo del reporte
     * @return Ruta del archivo
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    
    /**
     * Genera un nombre de archivo para el reporte basado en sus propiedades
     * @return Nombre del archivo
     */
    public String generarNombreArchivo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fechaStr = LocalDate.now().format(formatter);
        String nombreUsuario = usuario != null ? usuario.getNombre().replaceAll("\\s+", "_") : "general";
        
        return "Reporte_" + nombreUsuario + "_" + fechaStr + formato.getExtension();
    }
    
    @Override
    public String toString() {
        return "Reporte{" +
                "titulo='" + titulo + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "N/A") +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", formato=" + formato +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                '}';
    }
}
