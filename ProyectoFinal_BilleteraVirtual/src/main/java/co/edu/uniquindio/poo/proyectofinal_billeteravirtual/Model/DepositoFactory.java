package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

public class DepositoFactory extends TransaccionFactory{

    public DepositoFactory(String idTransaccion, LocalDate fechaTransaccion, TipoTransaccion tipoTransaccion, double monto, String descripcion, String empresaDestido, Categoria categoria) {
        super(idTransaccion, fechaTransaccion, tipoTransaccion, monto, descripcion, empresaDestido, categoria);
    }

    @Override
    public void CrearTransaccion() {
        System.out.println("Deposito Creado");
    }
}
