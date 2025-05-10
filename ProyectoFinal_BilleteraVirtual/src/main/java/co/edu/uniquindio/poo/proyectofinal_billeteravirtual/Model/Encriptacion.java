package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class Encriptacion extends SeguridadDecorator{

    public Encriptacion(ICuenta cuenta) {
        super(cuenta);
    }
}
