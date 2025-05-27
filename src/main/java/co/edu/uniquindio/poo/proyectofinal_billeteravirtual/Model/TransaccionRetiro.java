package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

public class TransaccionRetiro extends TransaccionFactory {

    public TransaccionRetiro() {
        super(null, LocalDate.now(), TipoTransaccion.RETIRO, 0.0, "Retiro", null, null);
    }

    public TransaccionRetiro(String idTransaccion, LocalDate fechaTransaccion, double monto, String descripcion, Categoria categoria, Usuario usuario) {
        super(idTransaccion, fechaTransaccion, TipoTransaccion.RETIRO, monto, descripcion, null, categoria, usuario);
    }

    @Override
    public void CrearTransaccion() {
        // Lógica para crear una transacción de retiro
        System.out.println("Creando transacción de retiro");
    }

    @Override
    public void llenarDatos() {
        System.out.println("Llenando datos de transacción de retiro");
    }
}
