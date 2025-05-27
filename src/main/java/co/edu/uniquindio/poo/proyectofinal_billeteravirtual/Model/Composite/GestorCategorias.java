package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestor de categorías que implementa el patrón Singleton
 * Permite gestionar la jerarquía de categorías utilizando el patrón Composite
 */
public class GestorCategorias implements Serializable {
    private static GestorCategorias instancia;
    private final GrupoCategoria raiz;
    private final DataManager dataManager;

    /**
     * Constructor privado para el patrón Singleton
     */
    private GestorCategorias() {
        this.raiz = new GrupoCategoria("Todas las Categorías", "Categoría raíz que contiene todas las demás categorías");
        this.dataManager = DataManager.getInstance();
        inicializarCategorias();
    }

    /**
     * Obtiene la instancia única del gestor (patrón Singleton)
     * @return Instancia del gestor
     */
    public static synchronized GestorCategorias getInstance() {
        if (instancia == null) {
            instancia = new GestorCategorias();
        }
        return instancia;
    }

    /**
     * Inicializa las categorías a partir de las categorías existentes
     */
    private void inicializarCategorias() {
        // Convertir las categorías existentes a categorías simples
        List<Categoria> categoriasExistentes = dataManager.getCategorias();

        // Crear categorías principales
        GrupoCategoria gastos = new GrupoCategoria("Gastos", "Categorías de gastos");
        GrupoCategoria ingresos = new GrupoCategoria("Ingresos", "Categorías de ingresos");
        GrupoCategoria inversiones = new GrupoCategoria("Inversiones", "Categorías de inversiones");

        // Agregar las categorías principales a la raíz
        raiz.agregarComponente(gastos);
        raiz.agregarComponente(ingresos);
        raiz.agregarComponente(inversiones);

        // Crear subcategorías de gastos
        GrupoCategoria alimentacion = new GrupoCategoria("Alimentación", "Gastos relacionados con alimentación");
        gastos.agregarComponente(alimentacion);
        alimentacion.agregarComponente(new CategoriaSimple("Supermercado", "Compras en supermercados"));
        alimentacion.agregarComponente(new CategoriaSimple("Restaurantes", "Comidas en restaurantes"));

        GrupoCategoria vivienda = new GrupoCategoria("Vivienda", "Gastos relacionados con la vivienda");
        gastos.agregarComponente(vivienda);
        vivienda.agregarComponente(new CategoriaSimple("Alquiler", "Pago de alquiler"));
        vivienda.agregarComponente(new CategoriaSimple("Servicios", "Pago de servicios públicos"));

        GrupoCategoria transporte = new GrupoCategoria("Transporte", "Gastos relacionados con transporte");
        gastos.agregarComponente(transporte);
        transporte.agregarComponente(new CategoriaSimple("Combustible", "Gasto en combustible"));
        transporte.agregarComponente(new CategoriaSimple("Transporte Público", "Gasto en transporte público"));

        // Crear subcategorías de ingresos
        ingresos.agregarComponente(new CategoriaSimple("Salario", "Ingresos por salario"));
        ingresos.agregarComponente(new CategoriaSimple("Freelance", "Ingresos por trabajos freelance"));
        ingresos.agregarComponente(new CategoriaSimple("Regalos", "Ingresos por regalos"));

        // Crear subcategorías de inversiones
        inversiones.agregarComponente(new CategoriaSimple("Acciones", "Inversiones en acciones"));
        inversiones.agregarComponente(new CategoriaSimple("Fondos", "Inversiones en fondos"));
        inversiones.agregarComponente(new CategoriaSimple("Criptomonedas", "Inversiones en criptomonedas"));

        // Agregar las categorías existentes que no estén ya incluidas
        for (Categoria categoria : categoriasExistentes) {
            // Verificar si la categoría ya existe
            boolean existe = false;
            List<ComponenteCategoria> todasLasHojas = obtenerTodasLasCategorias();

            for (ComponenteCategoria hoja : todasLasHojas) {
                if (hoja.getNombre().equals(categoria.getNombre())) {
                    existe = true;
                    break;
                }
            }

            // Si no existe, agregarla a la raíz
            if (!existe) {
                raiz.agregarComponente(new CategoriaSimple(categoria.getNombre(), categoria.getDescripcion()));
            }
        }
    }

    /**
     * Obtiene la categoría raíz
     * @return Categoría raíz
     */
    public GrupoCategoria getRaiz() {
        return raiz;
    }

    /**
     * Obtiene todas las categorías (hojas) del sistema
     * @return Lista de categorías
     */
    public List<ComponenteCategoria> obtenerTodasLasCategorias() {
        return raiz.obtenerTodasLasHojas();
    }

    /**
     * Obtiene todas las categorías como objetos Categoria tradicionales
     * @return Lista de categorías tradicionales
     */
    public List<Categoria> obtenerCategoriasTradicionales() {
        List<ComponenteCategoria> categorias = obtenerTodasLasCategorias();
        List<Categoria> categoriasTradicionales = new ArrayList<>();

        for (ComponenteCategoria componente : categorias) {
            Categoria categoria = new Categoria(componente.getNombre(), componente.getDescripcion());
            categoriasTradicionales.add(categoria);
        }

        return categoriasTradicionales;
    }

    /**
     * Agrega una nueva categoría simple al grupo especificado
     * @param nombreCategoria Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @param idGrupo ID del grupo al que se agregará (null para agregar a la raíz)
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarCategoria(String nombreCategoria, String descripcion, String idGrupo) {
        ComponenteCategoria nuevaCategoria = new CategoriaSimple(nombreCategoria, descripcion);

        if (idGrupo == null) {
            return raiz.agregarComponente(nuevaCategoria);
        } else {
            ComponenteCategoria grupo = raiz.obtenerComponente(idGrupo);
            if (grupo != null && !grupo.esHoja()) {
                return grupo.agregarComponente(nuevaCategoria);
            }
            return false;
        }
    }

    /**
     * Agrega un nuevo grupo de categorías al grupo especificado
     * @param nombreGrupo Nombre del grupo
     * @param descripcion Descripción del grupo
     * @param idGrupoPadre ID del grupo padre (null para agregar a la raíz)
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarGrupo(String nombreGrupo, String descripcion, String idGrupoPadre) {
        ComponenteCategoria nuevoGrupo = new GrupoCategoria(nombreGrupo, descripcion);

        if (idGrupoPadre == null) {
            return raiz.agregarComponente(nuevoGrupo);
        } else {
            ComponenteCategoria grupoPadre = raiz.obtenerComponente(idGrupoPadre);
            if (grupoPadre != null && !grupoPadre.esHoja()) {
                return grupoPadre.agregarComponente(nuevoGrupo);
            }
            return false;
        }
    }

    /**
     * Elimina una categoría o grupo por su ID
     * @param id ID de la categoría o grupo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarComponente(String id) {
        // Buscar el componente
        ComponenteCategoria componente = raiz.obtenerComponente(id);
        if (componente == null) {
            return false;
        }

        // Obtener el padre
        ComponenteCategoria padre = componente.getPadre();
        if (padre == null) {
            return false; // No se puede eliminar la raíz
        }

        // Eliminar el componente
        return padre.eliminarComponente(id);
    }

    /**
     * Calcula el total de transacciones para una categoría específica
     * @param idCategoria ID de la categoría
     * @param transacciones Lista de transacciones
     * @return Monto total
     */
    public double calcularTotalCategoria(String idCategoria, List<TransaccionFactory> transacciones) {
        ComponenteCategoria categoria = raiz.obtenerComponente(idCategoria);
        if (categoria == null) {
            return 0;
        }

        return categoria.calcularTotal(transacciones);
    }

    /**
     * Sincroniza las categorías tradicionales con las categorías del gestor
     */
    public void sincronizarCategorias() {
        // Obtener todas las categorías como objetos Categoria tradicionales
        List<Categoria> categoriasTradicionales = obtenerCategoriasTradicionales();

        // Actualizar las categorías en el DataManager
        dataManager.setCategorias(categoriasTradicionales);
        dataManager.guardarDatos();
    }

    /**
     * Crea una categoría simple y la agrega a la raíz
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearCategoriaSimple(String nombre, String descripcion) {
        ComponenteCategoria nuevaCategoria = new CategoriaSimple(nombre, descripcion);
        boolean resultado = raiz.agregarComponente(nuevaCategoria);
        if (resultado) {
            sincronizarCategorias();
        }
        return resultado;
    }

    /**
     * Crea una categoría simple y la agrega a un grupo específico
     * @param nombreGrupo Nombre del grupo al que se agregará la categoría
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearCategoriaSimpleEnGrupo(String nombreGrupo, String nombre, String descripcion) {
        // Buscar el grupo por nombre
        List<ComponenteCategoria> componentes = obtenerTodasLasCategorias();
        ComponenteCategoria grupo = null;

        for (ComponenteCategoria componente : componentes) {
            if (componente.getNombre().equals(nombreGrupo) && !componente.esHoja()) {
                grupo = componente;
                break;
            }
        }

        if (grupo == null) {
            // Si no se encuentra el grupo, intentar buscar en la raíz
            for (ComponenteCategoria componente : raiz.getComponentes()) {
                if (componente.getNombre().equals(nombreGrupo) && !componente.esHoja()) {
                    grupo = componente;
                    break;
                }
            }
        }

        if (grupo == null) {
            return false; // No se encontró el grupo
        }

        ComponenteCategoria nuevaCategoria = new CategoriaSimple(nombre, descripcion);
        boolean resultado = grupo.agregarComponente(nuevaCategoria);
        if (resultado) {
            sincronizarCategorias();
        }
        return resultado;
    }

    /**
     * Crea un grupo de categorías y lo agrega a la raíz
     * @param nombre Nombre del grupo
     * @param descripcion Descripción del grupo
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearGrupoCategoria(String nombre, String descripcion) {
        ComponenteCategoria nuevoGrupo = new GrupoCategoria(nombre, descripcion);
        boolean resultado = raiz.agregarComponente(nuevoGrupo);
        if (resultado) {
            sincronizarCategorias();
        }
        return resultado;
    }

    /**
     * Elimina una categoría por su nombre
     * @param nombre Nombre de la categoría a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCategoria(String nombre) {
        // Buscar la categoría por nombre
        List<ComponenteCategoria> componentes = obtenerTodasLasCategorias();
        ComponenteCategoria categoriaAEliminar = null;

        for (ComponenteCategoria componente : componentes) {
            if (componente.getNombre().equals(nombre)) {
                categoriaAEliminar = componente;
                break;
            }
        }

        if (categoriaAEliminar == null) {
            return false; // No se encontró la categoría
        }

        // Obtener el padre
        ComponenteCategoria padre = categoriaAEliminar.getPadre();
        if (padre == null) {
            return false; // No se puede eliminar la raíz
        }

        // Eliminar la categoría
        boolean resultado = padre.eliminarComponente(categoriaAEliminar.getId());
        if (resultado) {
            sincronizarCategorias();
        }
        return resultado;
    }
}
