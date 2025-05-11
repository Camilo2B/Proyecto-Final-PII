package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;

public class Administrador extends Persona{

    private LinkedList<Cuenta> cuentas;

    public Administrador(String nombre, String idGeneral, String email, String telefono, String direccion) {
        super(nombre, idGeneral, email, telefono, direccion);
    }

    public Administrador(String nombre, String idGeneral, String email, String telefono, String direccion, LinkedList<Cuenta> cuentas) {
        super(nombre, idGeneral, email, telefono, direccion);
        this.cuentas = cuentas;
    }

    public LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(LinkedList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "cuentas=" + cuentas +
                '}';
    }
}
