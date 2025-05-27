package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

/**
 * Enumeración que define los formatos disponibles para los reportes
 */
public enum FormatoReporte {
    PDF("Documento PDF", ".pdf"),
    CSV("Archivo CSV", ".csv"),
    HTML("Documento HTML", ".html"),
    EXCEL("Hoja de cálculo Excel", ".xlsx"),
    TXT("Archivo de texto", ".txt");
    
    private final String descripcion;
    private final String extension;
    
    /**
     * Constructor de la enumeración
     * @param descripcion Descripción del formato
     * @param extension Extensión del archivo
     */
    FormatoReporte(String descripcion, String extension) {
        this.descripcion = descripcion;
        this.extension = extension;
    }
    
    /**
     * Obtiene la descripción del formato
     * @return Descripción del formato
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene la extensión del archivo
     * @return Extensión del archivo
     */
    public String getExtension() {
        return extension;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
