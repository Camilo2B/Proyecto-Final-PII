package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

/**
 * Implementación concreta de la estrategia para realizar transferencias
 */
public class TransferenciaStrategy implements OperacionStrategy {

    private DataManager dataManager;
    
    public TransferenciaStrategy() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public boolean ejecutar(Usuario usuario, double monto, String cuentaOrigen, String cuentaDestino) {
        if (monto <= 0 || usuario.getSaldoTotal() < monto || cuentaDestino == null) {
            return false;
        }
        
        // Verificar que la cuenta destino existe
        Cuenta cuentaDestinoObj = dataManager.buscarCuenta(cuentaDestino);
        if (cuentaDestinoObj == null) {
            return false;
        }
        
        // Actualizar el saldo del usuario
        usuario.setSaldoTotal(usuario.getSaldoTotal() - monto);
        
        // Crear la transacción
        Categoria categoria = dataManager.getCategorias().getFirst(); // Categoría por defecto
        String idTransaccion = dataManager.generarId();
        
        TransaccionFactory transaccion = TransaccionCreator.crearTransaccion(
            TipoTransaccion.TRANSFERENCIA, 
            idTransaccion, 
            monto, 
            "Transferencia a cuenta " + cuentaDestinoObj.getNumeroCuenta(), 
            cuentaDestinoObj.getNombreBanco(), 
            categoria, 
            cuentaDestino
        );
        
        // Registrar la transacción
        dataManager.agregarTransaccion(transaccion);
        
        return true;
    }
}
