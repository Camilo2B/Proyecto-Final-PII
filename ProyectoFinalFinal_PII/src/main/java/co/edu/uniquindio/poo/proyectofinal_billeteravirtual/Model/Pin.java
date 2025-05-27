package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class Pin extends SeguridadDecorator{

    public Pin(ICuenta cuenta) {
        super(cuenta);
    }
}
