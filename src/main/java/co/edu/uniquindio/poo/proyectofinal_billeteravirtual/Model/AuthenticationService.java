package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Clase que gestiona la autenticación de usuarios y administradores
 * Implementa el patrón Singleton
 */
public class AuthenticationService {

    private static AuthenticationService instance;
    private DataManager dataManager;
    private Usuario usuarioAutenticado;
    private Administrador adminAutenticado;
    private final LoggerService logger;

    /**
     * Constructor privado para implementar Singleton
     */
    private AuthenticationService() {
        this.dataManager = DataManager.getInstance();
        this.logger = LoggerService.getInstance();
        logger.info("AuthenticationService inicializado correctamente");
    }

    /**
     * Método para obtener la instancia única de AuthenticationService
     * @return instancia de AuthenticationService
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Autentica a un usuario
     * @param idUsuario ID del usuario
     * @param password Contraseña
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean autenticarUsuario(String idUsuario, String password) {
        logger.info("Intento de autenticación de usuario: {}", idUsuario);

        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            logger.warn("Autenticación fallida para usuario: {} - Credenciales inválidas", idUsuario);
            logger.auditLogin(idUsuario, false, "USER", null);
            return false;
        }

        this.usuarioAutenticado = usuario;
        this.adminAutenticado = null;
        logger.setUserContext(idUsuario, "USER");
        logger.info("Autenticación exitosa para usuario: {}", idUsuario);
        logger.auditLogin(idUsuario, true, "USER", null);
        return true;
    }

    /**
     * Autentica a un administrador
     * @param idAdmin ID del administrador
     * @param password Contraseña
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean autenticarAdmin(String idAdmin, String password) {
        Administrador admin = dataManager.buscarAdministrador(idAdmin);
        if (admin == null || !admin.getPassword().equals(password)) {
            return false;
        }

        this.adminAutenticado = admin;
        this.usuarioAutenticado = null;
        return true;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioAutenticado = null;
        this.adminAutenticado = null;
    }

    /**
     * Verifica si hay un usuario autenticado
     * @return true si hay un usuario autenticado
     */
    public boolean hayUsuarioAutenticado() {
        return usuarioAutenticado != null;
    }

    /**
     * Verifica si hay un administrador autenticado
     * @return true si hay un administrador autenticado
     */
    public boolean hayAdminAutenticado() {
        return adminAutenticado != null;
    }

    /**
     * Obtiene el usuario autenticado
     * @return Usuario autenticado o null si no hay sesión iniciada
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    /**
     * Obtiene el administrador autenticado
     * @return Administrador autenticado o null si no hay sesión iniciada
     */
    public Administrador getAdminAutenticado() {
        return adminAutenticado;
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
     * Registra un nuevo administrador
     * @param nombre Nombre del administrador
     * @param idGeneral ID del administrador
     * @param email Email del administrador
     * @param telefono Teléfono del administrador
     * @param direccion Dirección del administrador
     * @param password Contraseña
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarAdmin(String nombre, String idGeneral, String email, String telefono, String direccion, String password) {
        if (dataManager.buscarAdministrador(idGeneral) != null) {
            return false;
        }

        Administrador admin = new Administrador(nombre, idGeneral, email, telefono, direccion, null, password);
        dataManager.agregarAdministrador(admin);
        return true;
    }
}
