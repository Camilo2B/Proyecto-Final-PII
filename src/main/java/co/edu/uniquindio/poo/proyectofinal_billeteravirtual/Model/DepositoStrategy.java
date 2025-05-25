package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Implementación concreta de la estrategia para realizar depósitos
 */
public class DepositoStrategy implements OperacionStrategy {

    private DataManager dataManager;
    
    public DepositoStrategy() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public boolean ejecutar(Usuario usuario, double monto, String cuentaOrigen, String cuentaDestino) {
        if (monto <= 0) {
            return false;
        }
        
        // Actualizar el saldo del usuario
        usuario.setSaldoTotal(usuario.getSaldoTotal() + monto);
        
        // Crear la transacción
        Categoria categoria = dataManager.getCategorias().getFirst(); // Categoría por defecto
        String idTransaccion = dataManager.generarId();
        
        TransaccionFactory transaccion = TransaccionCreator.crearTransaccion(
            TipoTransaccion.DEPOSITO, 
            idTransaccion, 
            monto, 
            "Depósito a cuenta", 
            "", 
            categoria, 
            null
        );
        
        // Registrar la transacción
        dataManager.agregarTransaccion(transaccion);
        
        return true;
    }
}
