package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase que gestiona la persistencia de datos en la aplicación.
 * Implementa el patrón Singleton para asegurar una única instancia.
 */
public class DataManager {
    private static DataManager instance;

    private LinkedList<Usuario> usuarios;
    private LinkedList<Administrador> administradores;
    private LinkedList<Cuenta> cuentas;
    private LinkedList<TransaccionFactory> transacciones;
    private LinkedList<Presupuesto> presupuestos;
    private LinkedList<Categoria> categorias;

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + "/billetera_data.ser";

    /**
     * Constructor privado para implementar Singleton
     */
    private DataManager() {
        // Inicializar listas
        this.usuarios = new LinkedList<>();
        this.administradores = new LinkedList<>();
        this.cuentas = new LinkedList<>();
        this.transacciones = new LinkedList<>();
        this.presupuestos = new LinkedList<>();
        this.categorias = new LinkedList<>();

        // Crear directorio de datos si no existe
        crearDirectorioSiNoExiste();

        // Cargar datos desde archivo o inicializar datos de prueba si no existe
        if (!cargarDatos()) {
            inicializarDatosPrueba();
            guardarDatos();
        }
    }

    /**
     * Crea el directorio de datos si no existe
     */
    private void crearDirectorioSiNoExiste() {
        Path path = Paths.get(DATA_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directorio de datos creado: " + path.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error al crear directorio de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para obtener la instancia única de DataManager
     * @return instancia de DataManager
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Carga los datos desde archivo serializado
     * @return true si se cargaron datos, false si no hay datos o hubo error
     */
    @SuppressWarnings("unchecked")
    private boolean cargarDatos() {
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists()) {
            System.out.println("No se encontró el archivo de datos. Inicializando datos de prueba.");
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            // Leer todos los datos en el orden correcto
            categorias = (LinkedList<Categoria>) ois.readObject();
            cuentas = (LinkedList<Cuenta>) ois.readObject();
            presupuestos = (LinkedList<Presupuesto>) ois.readObject();
            usuarios = (LinkedList<Usuario>) ois.readObject();
            administradores = (LinkedList<Administrador>) ois.readObject();
            transacciones = (LinkedList<TransaccionFactory>) ois.readObject();

            System.out.println("Datos cargados correctamente:");
            System.out.println("Categorías: " + categorias.size());
            System.out.println("Cuentas: " + cuentas.size());
            System.out.println("Presupuestos: " + presupuestos.size());
            System.out.println("Usuarios: " + usuarios.size());
            System.out.println("Administradores: " + administradores.size());
            System.out.println("Transacciones: " + transacciones.size());

            return true;
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
            // Si hay error en la deserialización, eliminar el archivo corrupto
            dataFile.delete();
            System.out.println("Archivo de datos eliminado debido a corrupción. Se inicializarán datos de prueba.");
            return false;
        }
    }

    /**
     * Guarda los datos en archivo serializado
     */
    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            // Guardar todos los datos en el orden correcto
            oos.writeObject(categorias);
            oos.writeObject(cuentas);
            oos.writeObject(presupuestos);
            oos.writeObject(usuarios);
            oos.writeObject(administradores);
            oos.writeObject(transacciones);

            System.out.println("Datos guardados correctamente");
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicializa datos de prueba para la aplicación
     */
    private void inicializarDatosPrueba() {
        try {
            // Crear categorías
            Categoria alimentacion = new Categoria(generarId(), "Alimentación", "Gastos relacionados con comida");
            Categoria transporte = new Categoria(generarId(), "Transporte", "Gastos de movilidad");
            Categoria entretenimiento = new Categoria(generarId(), "Entretenimiento", "Gastos de ocio");
            Categoria servicios = new Categoria(generarId(), "Servicios", "Pagos de servicios públicos");

            categorias.add(alimentacion);
            categorias.add(transporte);
            categorias.add(entretenimiento);
            categorias.add(servicios);

            // Crear cuentas
            Cuenta cuenta1 = new Cuenta(generarId(), "Banco de Bogotá", TipoCuenta.AHORRO, "1234567890", 1000000);
            Cuenta cuenta2 = new Cuenta(generarId(), "Bancolombia", TipoCuenta.CORRIENTE, "0987654321", 500000);

            cuentas.add(cuenta1);
            cuentas.add(cuenta2);

            // Crear usuarios
            LinkedList<Cuenta> cuentasUsuario1 = new LinkedList<>();
            cuentasUsuario1.add(cuenta1);

            Usuario usuario1 = new Usuario("Juan Pérez", "1234567890", "juan@example.com", "3001234567", "Calle 123", 1000000, cuentasUsuario1, "password123");
            usuarios.add(usuario1);

            // Crear presupuestos
            Presupuesto presupuesto1 = new Presupuesto(generarId(), "Presupuesto mensual", 500000, "0", alimentacion.getNombre());
            presupuestos.add(presupuesto1);

            // Crear administrador (sin usar el constructor que inicializa dataManager)
            Administrador admin = new Administrador();
            admin.setNombre("Admin Sistema");
            admin.setIdGeneral("9876543210");
            admin.setEmail("admin@sistema.com");
            admin.setTelefono("3009876543");
            admin.setDireccion("Avenida Principal");
            admin.setPassword("admin123");

            LinkedList<Cuenta> cuentasAdmin = new LinkedList<>();
            cuentasAdmin.add(cuenta2);
            admin.setCuentas(cuentasAdmin);

            administradores.add(admin);
        } catch (Exception e) {
            System.err.println("Error al inicializar datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un ID único usando UUID
     * @return String con ID único
     */
    public String generarId() {
        return UUID.randomUUID().toString();
    }

    // Métodos para gestionar usuarios

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        guardarDatos();
    }

    public void eliminarUsuario(String idUsuario) {
        usuarios.removeIf(u -> u.getIdGeneral().equals(idUsuario));
        guardarDatos();
    }

    public Usuario buscarUsuario(String idUsuario) {
        return usuarios.stream()
                .filter(u -> u.getIdGeneral().equals(idUsuario))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<Usuario> getUsuarios() {
        return usuarios;
    }

    // Métodos para gestionar administradores

    public void agregarAdministrador(Administrador admin) {
        administradores.add(admin);
        guardarDatos();
    }

    public Administrador buscarAdministrador(String idAdmin) {
        return administradores.stream()
                .filter(a -> a.getIdGeneral().equals(idAdmin))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<Administrador> getAdministradores() {
        return administradores;
    }

    // Métodos para gestionar cuentas

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        guardarDatos();
    }

    public void eliminarCuenta(String idCuenta) {
        cuentas.removeIf(c -> c.getIdCuenta().equals(idCuenta));
        guardarDatos();
    }

    public Cuenta buscarCuenta(String idCuenta) {
        return cuentas.stream()
                .filter(c -> c.getIdCuenta().equals(idCuenta))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }

    // Métodos para gestionar transacciones

    public void agregarTransaccion(TransaccionFactory transaccion) {
        transacciones.add(transaccion);
        guardarDatos();
    }

    public TransaccionFactory buscarTransaccion(String idTransaccion) {
        return transacciones.stream()
                .filter(t -> t.getIdTransaccion().equals(idTransaccion))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<TransaccionFactory> getTransacciones() {
        return transacciones;
    }

    public LinkedList<TransaccionFactory> getTransaccionesPorTipo(TipoTransaccion tipo) {
        return transacciones.stream()
                .filter(t -> t.getTipoTransaccion() == tipo)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // Métodos para gestionar presupuestos

    public void agregarPresupuesto(Presupuesto presupuesto) {
        presupuestos.add(presupuesto);
        guardarDatos();
    }

    public void eliminarPresupuesto(String idPresupuesto) {
        presupuestos.removeIf(p -> p.getIdPresupuesto().equals(idPresupuesto));
        guardarDatos();
    }

    public Presupuesto buscarPresupuesto(String idPresupuesto) {
        return presupuestos.stream()
                .filter(p -> p.getIdPresupuesto().equals(idPresupuesto))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    // Métodos para gestionar categorías

    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
        guardarDatos();
    }

    public void eliminarCategoria(String idCategoria) {
        categorias.removeIf(c -> c.getIdCategoria().equals(idCategoria));
        guardarDatos();
    }

    public Categoria buscarCategoria(String idCategoria) {
        return categorias.stream()
                .filter(c -> c.getIdCategoria().equals(idCategoria))
                .findFirst()
                .orElse(null);
    }

    public LinkedList<Categoria> getCategorias() {
        return categorias;
    }
}
