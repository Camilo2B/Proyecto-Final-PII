package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

/**
 * Clase que implementa el patrón Factory Method para crear diferentes tipos de transacciones
 */
public class TransaccionCreator {
    
    /**
     * Crea una transacción del tipo especificado
     * @param tipo Tipo de transacción a crear
     * @param idTransaccion ID único de la transacción
     * @param monto Monto de la transacción
     * @param descripcion Descripción de la transacción
     * @param empresaDestino Empresa destino (si aplica)
     * @param categoria Categoría de la transacción
     * @param cuentaDestino Cuenta destino (solo para transferencias)
     * @return Una instancia de TransaccionFactory según el tipo especificado
     */
    public static TransaccionFactory crearTransaccion(TipoTransaccion tipo, String idTransaccion, double monto, 
                                                     String descripcion, String empresaDestino, Categoria categoria, 
                                                     String cuentaDestino) {
        LocalDate fechaActual = LocalDate.now();
        
        switch (tipo) {
            case DEPOSITO:
                return new DepositoFactory(idTransaccion, fechaActual, tipo, monto, descripcion, empresaDestino, categoria);
            case RETIRO:
                return new RetiroFactory(idTransaccion, fechaActual, tipo, monto, descripcion, empresaDestino, categoria);
            case TRANSFERENCIA:
                return new TransferenciaFactory(idTransaccion, fechaActual, tipo, monto, descripcion, empresaDestino, categoria, cuentaDestino);
            default:
                throw new IllegalArgumentException("Tipo de transacción no soportado");
        }
    }
}
