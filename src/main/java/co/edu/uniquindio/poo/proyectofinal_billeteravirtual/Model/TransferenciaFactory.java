package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

/**
 * Clase que implementa el patrón Factory para crear transacciones de tipo Transferencia
 */
public class TransferenciaFactory extends TransaccionFactory {
    
    private String cuentaDestino;

    public TransferenciaFactory(String idTransaccion, LocalDate fechaTransaccion, TipoTransaccion tipoTransaccion, double monto, String descripcion, String empresaDestido, Categoria categoria, String cuentaDestino) {
        super(idTransaccion, fechaTransaccion, tipoTransaccion, monto, descripcion, empresaDestido, categoria);
        this.cuentaDestino = cuentaDestino;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    @Override
    public void CrearTransaccion() {
        System.out.println("Transferencia Creada a la cuenta: " + cuentaDestino);
    }
}
