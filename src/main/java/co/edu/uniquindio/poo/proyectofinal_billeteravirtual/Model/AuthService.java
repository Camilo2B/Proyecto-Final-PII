package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Servicio de autenticación para la aplicación
 * Implementa el patrón Singleton
 */
public class AuthService {

    private static AuthService instance;
    private DataManager dataManager;
    private Usuario usuarioAutenticado;
    private Administrador adminAutenticado;

    /**
     * Constructor privado para implementar Singleton
     */
    private AuthService() {
        dataManager = DataManager.getInstance();
    }

    /**
     * Obtiene la instancia única del servicio
     * @return Instancia del servicio
     */
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Autentica a un usuario
     * @param id ID del usuario
     * @param password Contraseña del usuario
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean autenticarUsuario(String id, String password) {
        Usuario usuario = dataManager.buscarUsuario(id);

        if (usuario != null && usuario.getPassword().equals(password)) {
            usuarioAutenticado = usuario;
            adminAutenticado = null;
            return true;
        }

        return false;
    }

    /**
     * Autentica a un administrador
     * @param id ID del administrador
     * @param password Contraseña del administrador
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean autenticarAdmin(String id, String password) {
        Administrador admin = dataManager.buscarAdministrador(id);

        if (admin != null && admin.getPassword().equals(password)) {
            adminAutenticado = admin;
            usuarioAutenticado = null;
            return true;
        }

        return false;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        usuarioAutenticado = null;
        adminAutenticado = null;
    }

    /**
     * Obtiene el usuario autenticado
     * @return Usuario autenticado o null si no hay usuario autenticado
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    /**
     * Obtiene el administrador autenticado
     * @return Administrador autenticado o null si no hay administrador autenticado
     */
    public Administrador getAdminAutenticado() {
        return adminAutenticado;
    }

    /**
     * Verifica si hay un usuario autenticado
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public boolean hayUsuarioAutenticado() {
        return usuarioAutenticado != null;
    }

    /**
     * Verifica si hay un administrador autenticado
     * @return true si hay un administrador autenticado, false en caso contrario
     */
    public boolean hayAdminAutenticado() {
        return adminAutenticado != null;
    }

    /**
     * Registra un nuevo usuario
     * @param usuario Usuario a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null || dataManager.buscarUsuario(usuario.getIdGeneral()) != null) {
            return false;
        }

        dataManager.agregarUsuario(usuario);
        return true;
    }

    /**
     * Registra un nuevo administrador
     * @param admin Administrador a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarAdmin(Administrador admin) {
        if (admin == null || dataManager.buscarAdministrador(admin.getIdGeneral()) != null) {
            return false;
        }

        dataManager.agregarAdministrador(admin);
        return true;
    }
}
