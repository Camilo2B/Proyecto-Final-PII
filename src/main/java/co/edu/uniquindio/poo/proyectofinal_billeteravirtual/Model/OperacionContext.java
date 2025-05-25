package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Contexto para el patrón Strategy que maneja las operaciones financieras
 */
public class OperacionContext {
    
    private OperacionStrategy strategy;
    
    /**
     * Constructor que recibe la estrategia a utilizar
     * @param strategy Estrategia de operación
     */
    public OperacionContext(OperacionStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Cambia la estrategia actual
     * @param strategy Nueva estrategia a utilizar
     */
    public void setStrategy(OperacionStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Ejecuta la operación financiera utilizando la estrategia actual
     * @param usuario Usuario que realiza la operación
     * @param monto Monto de la operación
     * @param cuentaOrigen ID de la cuenta origen
     * @param cuentaDestino ID de la cuenta destino (opcional, puede ser null)
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean ejecutarOperacion(Usuario usuario, double monto, String cuentaOrigen, String cuentaDestino) {
        return strategy.ejecutar(usuario, monto, cuentaOrigen, cuentaDestino);
    }
}
