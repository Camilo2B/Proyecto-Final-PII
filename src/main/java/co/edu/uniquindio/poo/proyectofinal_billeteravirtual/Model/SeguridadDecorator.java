package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public abstract class SeguridadDecorator implements ICuenta{
     private ICuenta cuenta;

     public SeguridadDecorator(ICuenta cuenta) {
         this.cuenta = cuenta;
     }

    public ICuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(ICuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public void CrearCuenta() {

    }

    @Override
    public void ModificarCuenta() {

    }

    @Override
    public void EliminarCuenta() {

    }
}
