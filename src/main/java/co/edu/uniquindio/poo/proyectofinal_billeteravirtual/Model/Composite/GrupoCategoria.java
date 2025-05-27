package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementación de un grupo de categorías (compuesto) en el patrón Composite
 */
public class GrupoCategoria implements ComponenteCategoria {
    private String id;
    private String nombre;
    private String descripcion;
    private List<ComponenteCategoria> componentes;
    private ComponenteCategoria padre;
    
    /**
     * Constructor con nombre
     * @param nombre Nombre del grupo
     */
    public GrupoCategoria(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = "";
        this.componentes = new ArrayList<>();
    }
    
    /**
     * Constructor con nombre y descripción
     * @param nombre Nombre del grupo
     * @param descripcion Descripción del grupo
     */
    public GrupoCategoria(String nombre, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.componentes = new ArrayList<>();
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
        if (componente == null) {
            return false;
        }
        
        // Verificar que no se agregue a sí mismo o a un ancestro
        ComponenteCategoria actual = this;
        while (actual != null) {
            if (actual.equals(componente)) {
                return false;
            }
            actual = actual.getPadre();
        }
        
        // Establecer este grupo como padre del componente
        componente.setPadre(this);
        
        // Agregar el componente
        return componentes.add(componente);
    }
    
    @Override
    public boolean eliminarComponente(String id) {
        // Buscar el componente por su ID
        ComponenteCategoria componente = obtenerComponente(id);
        if (componente != null) {
            // Eliminar la relación padre-hijo
            componente.setPadre(null);
            // Eliminar el componente
            return componentes.remove(componente);
        }
        return false;
    }
    
    @Override
    public ComponenteCategoria obtenerComponente(String id) {
        // Buscar directamente en los hijos
        for (ComponenteCategoria componente : componentes) {
            if (componente.getId().equals(id)) {
                return componente;
            }
        }
        
        // Buscar recursivamente en los hijos compuestos
        for (ComponenteCategoria componente : componentes) {
            if (!componente.esHoja()) {
                ComponenteCategoria encontrado = componente.obtenerComponente(id);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public boolean esHoja() {
        return false;
    }
    
    @Override
    public List<ComponenteCategoria> getComponentes() {
        return new ArrayList<>(componentes);
    }
    
    @Override
    public double calcularTotal(List<TransaccionFactory> transacciones) {
        // Sumar los totales de todos los componentes hijos
        return componentes.stream()
                .mapToDouble(c -> c.calcularTotal(transacciones))
                .sum();
    }
    
    @Override
    public int contarTransacciones(List<TransaccionFactory> transacciones) {
        // Sumar la cantidad de transacciones de todos los componentes hijos
        return componentes.stream()
                .mapToInt(c -> c.contarTransacciones(transacciones))
                .sum();
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
    
    /**
     * Obtiene todos los componentes hoja (categorías simples) de este grupo
     * @return Lista de categorías simples
     */
    public List<ComponenteCategoria> obtenerTodasLasHojas() {
        List<ComponenteCategoria> hojas = new ArrayList<>();
        
        for (ComponenteCategoria componente : componentes) {
            if (componente.esHoja()) {
                hojas.add(componente);
            } else {
                hojas.addAll(((GrupoCategoria) componente).obtenerTodasLasHojas());
            }
        }
        
        return hojas;
    }
    
    @Override
    public String toString() {
        return nombre + " (Grupo)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        GrupoCategoria that = (GrupoCategoria) obj;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
