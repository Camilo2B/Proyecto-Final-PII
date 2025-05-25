package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

/**
 * Clase que implementa el patrón Factory para crear transacciones de tipo Retiro
 */
public class RetiroFactory extends TransaccionFactory {

    public RetiroFactory(String idTransaccion, LocalDate fechaTransaccion, TipoTransaccion tipoTransaccion, double monto, String descripcion, String empresaDestido, Categoria categoria) {
        super(idTransaccion, fechaTransaccion, tipoTransaccion, monto, descripcion, empresaDestido, categoria);
    }

    @Override
    public void CrearTransaccion() {
        System.out.println("Retiro Creado");
    }
}
