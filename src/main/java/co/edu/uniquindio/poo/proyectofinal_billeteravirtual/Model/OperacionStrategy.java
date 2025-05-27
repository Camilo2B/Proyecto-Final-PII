package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Interfaz que define la estrategia para realizar operaciones financieras
 * Implementa el patrón Strategy
 */
public interface OperacionStrategy {
    
    /**
     * Ejecuta la operación financiera
     * @param usuario Usuario que realiza la operación
     * @param monto Monto de la operación
     * @param cuentaOrigen ID de la cuenta origen
     * @param cuentaDestino ID de la cuenta destino (opcional, puede ser null)
     * @return true si la operación fue exitosa, false en caso contrario
     */
    boolean ejecutar(Usuario usuario, double monto, String cuentaOrigen, String cuentaDestino);
}
