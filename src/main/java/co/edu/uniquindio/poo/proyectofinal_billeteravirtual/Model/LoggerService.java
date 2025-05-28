package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Servicio de registro de logs para la aplicación
 * Implementa el patrón Singleton
 */
public class LoggerService {
    
    private static LoggerService instance;
    private String userContext;
    private String userRole;
    
    /**
     * Constructor privado para implementar Singleton
     */
    private LoggerService() {
        // Inicialización del servicio de logs
    }
    
    /**
     * Método para obtener la instancia única de LoggerService
     * @return instancia de LoggerService
     */
    public static LoggerService getInstance() {
        if (instance == null) {
            instance = new LoggerService();
        }
        return instance;
    }
    
    /**
     * Registra un mensaje informativo
     * @param message Mensaje a registrar
     */
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }
    
    /**
     * Registra un mensaje informativo con formato
     * @param format Formato del mensaje
     * @param args Argumentos para el formato
     */
    public void info(String format, Object... args) {
        System.out.println("[INFO] " + String.format(format, args));
    }
    
    /**
     * Registra un mensaje de advertencia
     * @param message Mensaje a registrar
     */
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }
    
    /**
     * Registra un mensaje de advertencia con formato
     * @param format Formato del mensaje
     * @param args Argumentos para el formato
     */
    public void warn(String format, Object... args) {
        System.out.println("[WARN] " + String.format(format, args));
    }
    
    /**
     * Registra un mensaje de error
     * @param message Mensaje a registrar
     */
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }
    
    /**
     * Registra un mensaje de error con formato
     * @param format Formato del mensaje
     * @param args Argumentos para el formato
     */
    public void error(String format, Object... args) {
        if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
            Throwable throwable = (Throwable) args[args.length - 1];
            Object[] newArgs = new Object[args.length - 1];
            System.arraycopy(args, 0, newArgs, 0, args.length - 1);
            
            System.err.println("[ERROR] " + String.format(format, newArgs));
            throwable.printStackTrace();
        } else {
            System.err.println("[ERROR] " + String.format(format, args));
        }
    }
    
    /**
     * Establece el contexto del usuario actual
     * @param userId ID del usuario
     * @param role Rol del usuario
     */
    public void setUserContext(String userId, String role) {
        this.userContext = userId;
        this.userRole = role;
    }
    
    /**
     * Limpia el contexto del usuario actual
     */
    public void clearUserContext() {
        this.userContext = null;
        this.userRole = null;
        info("Contexto de usuario limpiado");
    }
    
    /**
     * Registra un intento de inicio de sesión
     * @param userId ID del usuario
     * @param success Éxito del inicio de sesión
     * @param role Rol del usuario
     * @param details Detalles adicionales
     */
    public void auditLogin(String userId, boolean success, String role, String details) {
        String message = String.format("Login %s para usuario %s con rol %s", 
                success ? "exitoso" : "fallido", userId, role);
        if (details != null) {
            message += " - " + details;
        }
        System.out.println("[AUDIT] " + message);
    }
    
    /**
     * Registra un cierre de sesión
     * @param userId ID del usuario
     * @param userType Tipo de usuario (USER/ADMIN)
     */
    public void auditLogout(String userId, String userType) {
        System.out.println("[AUDIT] Cierre de sesión para usuario " + userId + " con rol " + userType);
    }
    
    /**
     * Registra una transacción financiera
     * @param userId ID del usuario
     * @param tipoTransaccion Tipo de transacción
     * @param monto Monto de la transacción
     * @param idCuenta ID de la cuenta
     * @param descripcion Descripción de la transacción
     */
    public void auditTransaction(String userId, String tipoTransaccion, double monto, 
                                String idCuenta, String descripcion) {
        String message = String.format("Transacción %s - Usuario: %s - Monto: %.2f - Cuenta: %s - Descripción: %s",
                tipoTransaccion, userId, monto, idCuenta, descripcion);
        System.out.println("[AUDIT] " + message);
    }
}
