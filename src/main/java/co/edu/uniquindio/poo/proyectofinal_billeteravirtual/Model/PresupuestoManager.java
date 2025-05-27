package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Clase que gestiona los presupuestos de la aplicación
 * Implementa el patrón Singleton
 */
public class PresupuestoManager {

    private static PresupuestoManager instance;
    private DataManager dataManager;

    /**
     * Constructor privado para implementar Singleton
     */
    private PresupuestoManager() {
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Obtiene la instancia única del gestor de presupuestos
     * @return Instancia del gestor de presupuestos
     */
    public static PresupuestoManager getInstance() {
        if (instance == null) {
            instance = new PresupuestoManager();
        }
        return instance;
    }

    /**
     * Crea un nuevo presupuesto
     * @param nombre Nombre del presupuesto
     * @param montoTotal Monto total asignado
     * @param categoriaEspecifica Categoría específica (opcional)
     * @return El presupuesto creado
     */
    public Presupuesto crearPresupuesto(String nombre, double montoTotal, String categoriaEspecifica) {
        String idPresupuesto = dataManager.generarId();
        Presupuesto presupuesto = new Presupuesto(idPresupuesto, nombre, montoTotal, "0", categoriaEspecifica);
        dataManager.agregarPresupuesto(presupuesto);
        return presupuesto;
    }

    /**
     * Actualiza un presupuesto existente
     * @param idPresupuesto ID del presupuesto a actualizar
     * @param nombre Nuevo nombre
     * @param montoTotal Nuevo monto total
     * @param categoriaEspecifica Nueva categoría específica
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarPresupuesto(String idPresupuesto, String nombre, double montoTotal, String categoriaEspecifica) {
        Presupuesto presupuesto = dataManager.buscarPresupuesto(idPresupuesto);
        if (presupuesto == null) {
            return false;
        }

        presupuesto.setNombre(nombre);
        presupuesto.setMontoTotal(montoTotal);
        presupuesto.setCategoriaEspecifica(categoriaEspecifica);
        return true;
    }

    /**
     * Elimina un presupuesto
     * @param idPresupuesto ID del presupuesto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarPresupuesto(String idPresupuesto) {
        Presupuesto presupuesto = dataManager.buscarPresupuesto(idPresupuesto);
        if (presupuesto == null) {
            return false;
        }

        dataManager.eliminarPresupuesto(idPresupuesto);
        return true;
    }

    /**
     * Obtiene todos los presupuestos
     * @return Lista de presupuestos
     */
    public LinkedList<Presupuesto> obtenerPresupuestos() {
        return dataManager.getPresupuestos();
    }

    /**
     * Obtiene los presupuestos por categoría
     * @param categoria Categoría a filtrar
     * @return Lista de presupuestos de la categoría especificada
     */
    public LinkedList<Presupuesto> obtenerPresupuestosPorCategoria(String categoria) {
        return dataManager.getPresupuestos().stream()
                .filter(p -> p.getCategoriaEspecifica().equals(categoria))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Actualiza el monto gastado de un presupuesto
     * @param idPresupuesto ID del presupuesto
     * @param montoGastado Nuevo monto gastado
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarMontoGastado(String idPresupuesto, String montoGastado) {
        Presupuesto presupuesto = dataManager.buscarPresupuesto(idPresupuesto);
        if (presupuesto == null) {
            return false;
        }

        presupuesto.setMontoGastado(montoGastado);
        return true;
    }
}
