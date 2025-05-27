package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz componente para el patrón Composite de categorías financieras.
 *
 * <p>Define la estructura común para categorías simples (hojas) y grupos de categorías (compuestos),
 * permitiendo tratar ambos tipos de objetos de manera uniforme. Este patrón facilita la creación
 * de estructuras jerárquicas de categorías para organizar y analizar transacciones financieras.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>Permite crear jerarquías de categorías de cualquier profundidad</li>
 *   <li>Facilita operaciones recursivas como calcular totales por categoría</li>
 *   <li>Proporciona una interfaz uniforme para categorías simples y grupos</li>
 *   <li>Soporta navegación bidireccional entre categorías padre e hijas</li>
 * </ul>
 *
 * <p>Implementaciones concretas:</p>
 * <ul>
 *   <li>{@link CategoriaSimple}: Implementación para categorías individuales (hojas)</li>
 *   <li>{@link GrupoCategoria}: Implementación para grupos de categorías (compuestos)</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 * // Crear estructura jerárquica
 * GrupoCategoria gastos = new GrupoCategoria("Gastos", "Categorías de gastos");
 * GrupoCategoria alimentacion = new GrupoCategoria("Alimentación", "Gastos en comida");
 * CategoriaSimple restaurantes = new CategoriaSimple("Restaurantes", "Comidas fuera");
 * CategoriaSimple supermercado = new CategoriaSimple("Supermercado", "Compras de alimentos");
 *
 * // Construir jerarquía
 * gastos.agregarComponente(alimentacion);
 * alimentacion.agregarComponente(restaurantes);
 * alimentacion.agregarComponente(supermercado);
 *
 * // Calcular total (funciona igual para hojas y compuestos)
 * double totalGastos = gastos.calcularTotal(transacciones);
 * double totalAlimentacion = alimentacion.calcularTotal(transacciones);
 * double totalRestaurantes = restaurantes.calcularTotal(transacciones);
 * </pre>
 *
 * @see CategoriaSimple
 * @see GrupoCategoria
 * @see GestorCategorias
 */
public interface ComponenteCategoria extends Serializable {

    /**
     * Obtiene el ID del componente
     * @return ID del componente
     */
    String getId();

    /**
     * Obtiene el nombre del componente
     * @return Nombre del componente
     */
    String getNombre();

    /**
     * Establece el nombre del componente
     * @param nombre Nuevo nombre
     */
    void setNombre(String nombre);

    /**
     * Obtiene la descripción del componente
     * @return Descripción del componente
     */
    String getDescripcion();

    /**
     * Establece la descripción del componente
     * @param descripcion Nueva descripción
     */
    void setDescripcion(String descripcion);

    /**
     * Agrega un componente hijo (solo aplicable a compuestos)
     * @param componente Componente a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarComponente(ComponenteCategoria componente);

    /**
     * Elimina un componente hijo (solo aplicable a compuestos)
     * @param id ID del componente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarComponente(String id);

    /**
     * Obtiene un componente hijo por su ID (solo aplicable a compuestos)
     * @param id ID del componente a obtener
     * @return Componente encontrado o null si no existe
     */
    ComponenteCategoria obtenerComponente(String id);

    /**
     * Verifica si el componente es una hoja (categoría simple)
     * @return true si es una hoja, false si es un compuesto
     */
    boolean esHoja();

    /**
     * Obtiene la lista de componentes hijos (solo aplicable a compuestos)
     * @return Lista de componentes hijos
     */
    List<ComponenteCategoria> getComponentes();

    /**
     * Calcula el total de transacciones asociadas a esta categoría
     * @param transacciones Lista de transacciones a considerar
     * @return Monto total
     */
    double calcularTotal(List<TransaccionFactory> transacciones);

    /**
     * Obtiene la cantidad de transacciones asociadas a esta categoría
     * @param transacciones Lista de transacciones a considerar
     * @return Cantidad de transacciones
     */
    int contarTransacciones(List<TransaccionFactory> transacciones);

    /**
     * Obtiene la ruta completa de la categoría (incluyendo categorías padre)
     * @return Ruta completa
     */
    String getRutaCompleta();

    /**
     * Establece la categoría padre de este componente
     * @param padre Categoría padre
     */
    void setPadre(ComponenteCategoria padre);

    /**
     * Obtiene la categoría padre de este componente
     * @return Categoría padre
     */
    ComponenteCategoria getPadre();
}
