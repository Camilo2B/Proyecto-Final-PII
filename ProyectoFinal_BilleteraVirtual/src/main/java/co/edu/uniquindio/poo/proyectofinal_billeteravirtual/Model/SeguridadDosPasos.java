package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class SeguridadDosPasos extends SeguridadDecorator{

    public SeguridadDosPasos(ICuenta cuenta) {
        super(cuenta);
    }
}
