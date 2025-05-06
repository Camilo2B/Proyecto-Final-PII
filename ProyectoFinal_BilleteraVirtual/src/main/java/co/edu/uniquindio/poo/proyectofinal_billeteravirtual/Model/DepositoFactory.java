package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class DepositoFactory extends TransaccionFactory{

    @Override
    public void CrearTransaccion() {
        System.out.println("Deposito Creado");
    }
}
