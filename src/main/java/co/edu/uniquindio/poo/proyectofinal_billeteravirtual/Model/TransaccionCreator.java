package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

/**
 * Clase que implementa el patrón Factory para crear diferentes tipos de transacciones
 */
public class TransaccionCreator {

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
