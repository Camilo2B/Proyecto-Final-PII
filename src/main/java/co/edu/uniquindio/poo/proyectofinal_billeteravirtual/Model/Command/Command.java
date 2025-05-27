package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command;

/**
 * Interfaz Command para el patrón Command de operaciones financieras.
 *
 * <p>Define los métodos necesarios para encapsular operaciones financieras como objetos,
 * permitiendo parametrizar clientes con diferentes solicitudes, hacer cola o registrar
 * las solicitudes, y soportar operaciones que se pueden deshacer. Este patrón es fundamental
 * para implementar funcionalidades como historial de transacciones y capacidad de deshacer/rehacer.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>Encapsula una solicitud como un objeto</li>
 *   <li>Permite parametrizar clientes con diferentes solicitudes</li>
 *   <li>Permite encolar o registrar solicitudes</li>
 *   <li>Soporta operaciones que se pueden deshacer</li>
 * </ul>
 *
 * <p>Implementaciones concretas:</p>
 * <ul>
 *   <li>{@link DepositoCommand}: Comando para realizar depósitos</li>
 *   <li>{@link RetiroCommand}: Comando para realizar retiros</li>
 *   <li>{@link TransferenciaCommand}: Comando para realizar transferencias</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 * // Crear un comando
 * Command comando = new DepositoCommand(usuario, cuenta, 500.0, "Depósito inicial", null);
 *
 * // Ejecutar el comando
 * CommandInvoker.getInstance().ejecutarComando(comando);
 *
 * // Deshacer el comando
 * CommandInvoker.getInstance().deshacer();
 *
 * // Rehacer el comando
 * CommandInvoker.getInstance().rehacer();
 * </pre>
 *
 * @see CommandInvoker
 * @see DepositoCommand
 * @see RetiroCommand
 * @see TransferenciaCommand
 */
public interface Command {

    /**
     * Ejecuta el comando
     * @return true si se ejecutó correctamente, false en caso contrario
     */
    boolean ejecutar();

    /**
     * Deshace el comando
     * @return true si se deshizo correctamente, false en caso contrario
     */
    boolean deshacer();

    /**
     * Obtiene una descripción del comando
     * @return Descripción del comando
     */
    String getDescripcion();
}
