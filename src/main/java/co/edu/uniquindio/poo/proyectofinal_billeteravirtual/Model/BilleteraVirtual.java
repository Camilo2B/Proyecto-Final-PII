package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;
import java.util.Map;

/**
 * Interfaz que define las operaciones principales de la billetera virtual
 */
public interface BilleteraVirtual {

    /**
     * Inicia sesión en la billetera virtual
     * @param id Identificador del usuario o administrador
     * @param password Contraseña
     * @param esAdmin Indica si es un administrador
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    boolean iniciarSesion(String id, String password, boolean esAdmin);

    /**
     * Cierra la sesión actual
     */
    void cerrarSesion();

    /**
     * Crea una nueva cuenta bancaria
     * @param nombreBanco Nombre del banco
     * @param tipoCuenta Tipo de cuenta
     * @param numeroCuenta Número de cuenta
     * @return true si la cuenta se creó correctamente, false en caso contrario
     */
    boolean crearCuenta(String nombreBanco, TipoCuenta tipoCuenta, String numeroCuenta);

    /**
     * Realiza un depósito en la cuenta del usuario
     * @param monto Monto a depositar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el depósito fue exitoso, false en caso contrario
     */
    boolean realizarDeposito(double monto, String idCuenta);

    /**
     * Realiza un retiro de la cuenta del usuario
     * @param monto Monto a retirar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el retiro fue exitoso, false en caso contrario
     */
    boolean realizarRetiro(double monto, String idCuenta);

    /**
     * Realiza una transferencia entre cuentas
     * @param monto Monto a transferir
     * @param idCuentaOrigen ID de la cuenta origen
     * @param idCuentaDestino ID de la cuenta destino
     * @return true si la transferencia fue exitosa, false en caso contrario
     */
    boolean realizarTransferencia(double monto, String idCuentaOrigen, String idCuentaDestino);

    /**
     * Crea un nuevo presupuesto
     * @param nombre Nombre del presupuesto
     * @param montoTotal Monto total asignado
     * @param categoriaEspecifica Categoría específica (opcional)
     * @return true si el presupuesto se creó correctamente, false en caso contrario
     */
    boolean crearPresupuesto(String nombre, double montoTotal, String categoriaEspecifica);

    /**
     * Obtiene las transacciones del usuario actual
     * @return Lista de transacciones
     */
    LinkedList<TransaccionFactory> obtenerTransacciones();

    /**
     * Obtiene los presupuestos del usuario actual
     * @return Lista de presupuestos
     */
    LinkedList<Presupuesto> obtenerPresupuestos();

    /**
     * Obtiene las categorías disponibles
     * @return Lista de categorías
     */
    LinkedList<Categoria> obtenerCategorias();

    /**
     * Obtiene estadísticas de la billetera virtual (solo para administradores)
     * @return Mapa con las estadísticas o null si no es administrador
     */
    Map<String, Object> obtenerEstadisticas();
}
