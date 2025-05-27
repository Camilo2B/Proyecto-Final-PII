package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Implementación concreta de la estrategia para realizar retiros
 */
public class RetiroStrategy implements OperacionStrategy {

    private DataManager dataManager;
    
    public RetiroStrategy() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public boolean ejecutar(Usuario usuario, double monto, String cuentaOrigen, String cuentaDestino) {
        if (monto <= 0 || usuario.getSaldoTotal() < monto) {
            return false;
        }
        
        // Actualizar el saldo del usuario
        usuario.setSaldoTotal(usuario.getSaldoTotal() - monto);
        
        // Crear la transacción
        Categoria categoria = dataManager.getCategorias().getFirst(); // Categoría por defecto
        String idTransaccion = dataManager.generarId();
        
        TransaccionFactory transaccion = TransaccionCreator.crearTransaccion(
            TipoTransaccion.RETIRO, 
            idTransaccion, 
            monto, 
            "Retiro de cuenta", 
            "", 
            categoria, 
            null
        );
        
        // Registrar la transacción
        dataManager.agregarTransaccion(transaccion);
        
        return true;
    }
}
