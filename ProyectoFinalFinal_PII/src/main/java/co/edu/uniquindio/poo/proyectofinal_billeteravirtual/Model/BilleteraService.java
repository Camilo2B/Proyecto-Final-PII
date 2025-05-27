package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase de servicio que implementa la lógica de negocio de la billetera virtual
 * Actúa como intermediario entre la interfaz de usuario y el modelo de datos
 */
public class BilleteraService {
    
    private DataManager dataManager;
    private OperacionContext operacionContext;
    private PresupuestoManager presupuestoManager;
    private CategoriaManager categoriaManager;
    private Usuario usuarioActual;
    private Administrador adminActual;
    
    /**
     * Constructor de la clase BilleteraService
     */
    public BilleteraService() {
        this.dataManager = DataManager.getInstance();
        this.presupuestoManager = new PresupuestoManager();
        this.categoriaManager = new CategoriaManager();
    }
    
    /**
     * Inicia sesión de un usuario
     * @param idUsuario ID del usuario
     * @param password Contraseña
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesionUsuario(String idUsuario, String password) {
        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return false;
        }
        
        this.usuarioActual = usuario;
        return true;
    }
    
    /**
     * Inicia sesión de un administrador
     * @param idAdmin ID del administrador
     * @param password Contraseña
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesionAdmin(String idAdmin, String password) {
        Administrador admin = dataManager.buscarAdministrador(idAdmin);
        if (admin == null || !admin.getPassword().equals(password)) {
            return false;
        }
        
        this.adminActual = admin;
        return true;
    }
    
    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
        this.adminActual = null;
    }
    
    /**
     * Verifica si hay un usuario con sesión iniciada
     * @return true si hay un usuario con sesión iniciada
     */
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }
    
    /**
     * Verifica si hay un administrador con sesión iniciada
     * @return true si hay un administrador con sesión iniciada
     */
    public boolean hayAdminLogueado() {
        return adminActual != null;
    }
    
    /**
     * Obtiene el usuario actual
     * @return Usuario actual o null si no hay sesión iniciada
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Obtiene el administrador actual
     * @return Administrador actual o null si no hay sesión iniciada
     */
    public Administrador getAdminActual() {
        return adminActual;
    }
    
    /**
     * Registra un nuevo usuario
     * @param nombre Nombre del usuario
     * @param idGeneral ID del usuario
     * @param email Email del usuario
     * @param telefono Teléfono del usuario
     * @param direccion Dirección del usuario
     * @param password Contraseña
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarUsuario(String nombre, String idGeneral, String email, String telefono, String direccion, String password) {
        if (dataManager.buscarUsuario(idGeneral) != null) {
            return false;
        }
        
        Usuario usuario = new Usuario(nombre, idGeneral, email, telefono, direccion, 0.0, null, password);
        dataManager.agregarUsuario(usuario);
        return true;
    }
    
    /**
     * Realiza un depósito en la cuenta del usuario actual
     * @param monto Monto a depositar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el depósito fue exitoso, false en caso contrario
     */
    public boolean realizarDeposito(double monto, String idCuenta) {
        if (!hayUsuarioLogueado() || monto <= 0) {
            return false;
        }
        
        operacionContext = new OperacionContext(new DepositoStrategy());
        return operacionContext.ejecutarOperacion(usuarioActual, monto, idCuenta, null);
    }
    
    /**
     * Realiza un retiro de la cuenta del usuario actual
     * @param monto Monto a retirar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el retiro fue exitoso, false en caso contrario
     */
    public boolean realizarRetiro(double monto, String idCuenta) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }
        
        operacionContext = new OperacionContext(new RetiroStrategy());
        return operacionContext.ejecutarOperacion(usuarioActual, monto, idCuenta, null);
    }
    
    /**
     * Realiza una transferencia desde la cuenta del usuario actual
     * @param monto Monto a transferir
     * @param idCuentaOrigen ID de la cuenta origen
     * @param idCuentaDestino ID de la cuenta destino
     * @return true si la transferencia fue exitosa, false en caso contrario
     */
    public boolean realizarTransferencia(double monto, String idCuentaOrigen, String idCuentaDestino) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }
        
        operacionContext = new OperacionContext(new TransferenciaStrategy());
        return operacionContext.ejecutarOperacion(usuarioActual, monto, idCuentaOrigen, idCuentaDestino);
    }
    
    /**
     * Crea un nuevo presupuesto para el usuario actual
     * @param nombre Nombre del presupuesto
     * @param montoTotal Monto total asignado
     * @param categoriaEspecifica Categoría específica (opcional)
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearPresupuesto(String nombre, double montoTotal, String categoriaEspecifica) {
        if (!hayUsuarioLogueado() || montoTotal <= 0) {
            return false;
        }
        
        Presupuesto presupuesto = presupuestoManager.crearPresupuesto(nombre, montoTotal, categoriaEspecifica);
        return usuarioActual.agregarPresupuesto(presupuesto);
    }
    
    /**
     * Obtiene los presupuestos del usuario actual
     * @return Lista de presupuestos
     */
    public LinkedList<Presupuesto> obtenerPresupuestosUsuario() {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }
        
        return usuarioActual.getPresupuestos();
    }
    
    /**
     * Crea una nueva categoría
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearCategoria(String nombre, String descripcion) {
        if (categoriaManager.buscarCategoriaPorNombre(nombre) != null) {
            return false;
        }
        
        categoriaManager.crearCategoria(nombre, descripcion);
        return true;
    }
    
    /**
     * Obtiene todas las categorías
     * @return Lista de categorías
     */
    public LinkedList<Categoria> obtenerCategorias() {
        return categoriaManager.obtenerCategorias();
    }
    
    /**
     * Obtiene las transacciones del usuario actual
     * @return Lista de transacciones
     */
    public LinkedList<TransaccionFactory> obtenerTransaccionesUsuario() {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }
        
        // En una implementación real, se filtrarían las transacciones por usuario
        return dataManager.getTransacciones();
    }
    
    /**
     * Obtiene las transacciones por tipo
     * @param tipo Tipo de transacción
     * @return Lista de transacciones del tipo especificado
     */
    public LinkedList<TransaccionFactory> obtenerTransaccionesPorTipo(TipoTransaccion tipo) {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }
        
        return dataManager.getTransaccionesPorTipo(tipo);
    }
    
    /**
     * Obtiene estadísticas de la billetera virtual (solo para administradores)
     * @return Mapa con las estadísticas o null si no hay un administrador logueado
     */
    public Map<String, Object> obtenerEstadisticas() {
        if (!hayAdminLogueado()) {
            return null;
        }
        
        return adminActual.obtenerEstadisticas();
    }
}
