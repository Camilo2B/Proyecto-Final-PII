package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;

/**
 * Clase que gestiona las categorías de la aplicación
 */
public class CategoriaManager {
    
    private DataManager dataManager;
    
    public CategoriaManager() {
        this.dataManager = DataManager.getInstance();
    }
    
    /**
     * Crea una nueva categoría
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @return La categoría creada
     */
    public Categoria crearCategoria(String nombre, String descripcion) {
        String idCategoria = dataManager.generarId();
        Categoria categoria = new Categoria(idCategoria, nombre, descripcion);
        dataManager.agregarCategoria(categoria);
        return categoria;
    }
    
    /**
     * Actualiza una categoría existente
     * @param idCategoria ID de la categoría a actualizar
     * @param nombre Nuevo nombre
     * @param descripcion Nueva descripción
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarCategoria(String idCategoria, String nombre, String descripcion) {
        Categoria categoria = dataManager.buscarCategoria(idCategoria);
        if (categoria == null) {
            return false;
        }
        
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        return true;
    }
    
    /**
     * Elimina una categoría
     * @param idCategoria ID de la categoría a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCategoria(String idCategoria) {
        Categoria categoria = dataManager.buscarCategoria(idCategoria);
        if (categoria == null) {
            return false;
        }
        
        dataManager.eliminarCategoria(idCategoria);
        return true;
    }
    
    /**
     * Obtiene todas las categorías
     * @return Lista de categorías
     */
    public LinkedList<Categoria> obtenerCategorias() {
        return dataManager.getCategorias();
    }
    
    /**
     * Busca una categoría por su ID
     * @param idCategoria ID de la categoría
     * @return La categoría encontrada o null si no existe
     */
    public Categoria buscarCategoria(String idCategoria) {
        return dataManager.buscarCategoria(idCategoria);
    }
    
    /**
     * Busca una categoría por su nombre
     * @param nombre Nombre de la categoría
     * @return La categoría encontrada o null si no existe
     */
    public Categoria buscarCategoriaPorNombre(String nombre) {
        return dataManager.getCategorias().stream()
                .filter(c -> c.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }
}
