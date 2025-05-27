package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

public class TransaccionDeposito extends TransaccionFactory {

    public TransaccionDeposito() {
        super(null, LocalDate.now(), TipoTransaccion.DEPOSITO, 0.0, "Depósito", null, null);
    }

    public TransaccionDeposito(String idTransaccion, LocalDate fechaTransaccion, double monto, String descripcion, Categoria categoria, Usuario usuario) {
        super(idTransaccion, fechaTransaccion, TipoTransaccion.DEPOSITO, monto, descripcion, null, categoria, usuario);
    }

    @Override
    public void CrearTransaccion() {
        // Lógica para crear una transacción de depósito
        System.out.println("Creando transacción de depósito");
    }

    @Override
    public void llenarDatos() {
        System.out.println("Llenando datos de transacción de depósito");
    }
}
