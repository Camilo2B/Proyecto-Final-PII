package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación de una categoría simple (hoja) en el patrón Composite
 */
public class CategoriaSimple implements ComponenteCategoria {
    private String id;
    private String nombre;
    private String descripcion;
    private ComponenteCategoria padre;
    
    /**
     * Constructor con nombre
     * @param nombre Nombre de la categoría
     */
    public CategoriaSimple(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = "";
    }
    
    /**
     * Constructor con nombre y descripción
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     */
    public CategoriaSimple(String nombre, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public boolean agregarComponente(ComponenteCategoria componente) {
        // No se pueden agregar componentes a una hoja
        return false;
    }
    
    @Override
    public boolean eliminarComponente(String id) {
        // No se pueden eliminar componentes de una hoja
        return false;
    }
    
    @Override
    public ComponenteCategoria obtenerComponente(String id) {
        // Una hoja no tiene componentes
        return null;
    }
    
    @Override
    public boolean esHoja() {
        return true;
    }
    
    @Override
    public List<ComponenteCategoria> getComponentes() {
        // Una hoja no tiene componentes
        return new ArrayList<>();
    }
    
    @Override
    public double calcularTotal(List<TransaccionFactory> transacciones) {
        // Filtrar transacciones que pertenecen a esta categoría
        return transacciones.stream()
                .filter(t -> t.getCategoria() != null && 
                             t.getCategoria().getNombre().equals(this.nombre))
                .mapToDouble(TransaccionFactory::getMonto)
                .sum();
    }
    
    @Override
    public int contarTransacciones(List<TransaccionFactory> transacciones) {
        // Contar transacciones que pertenecen a esta categoría
        return (int) transacciones.stream()
                .filter(t -> t.getCategoria() != null && 
                             t.getCategoria().getNombre().equals(this.nombre))
                .count();
    }
    
    @Override
    public String getRutaCompleta() {
        if (padre == null) {
            return nombre;
        } else {
            return padre.getRutaCompleta() + " > " + nombre;
        }
    }
    
    @Override
    public void setPadre(ComponenteCategoria padre) {
        this.padre = padre;
    }
    
    @Override
    public ComponenteCategoria getPadre() {
        return padre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CategoriaSimple that = (CategoriaSimple) obj;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
