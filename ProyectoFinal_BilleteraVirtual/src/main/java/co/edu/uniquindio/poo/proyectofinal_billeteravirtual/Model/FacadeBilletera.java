package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;

public class FacadeBilletera implements BilleteraVirtual {

    private String nombre;
    private LinkedList<Cuenta> cuentas;
    private Administrador administrador;
    private LinkedList<TipoTransaccion> transacciones;
    private LinkedList<Presupuesto> presupuestos;
    protected ConfiguracionBilletera configuracionBilletera;

    public FacadeBilletera(String nombre, LinkedList<Cuenta> cuentas, Administrador administrador, LinkedList<TipoTransaccion> transacciones, LinkedList<Presupuesto> presupuestos) {
        this.nombre = nombre;
        this.cuentas = cuentas;
        this.administrador = administrador;
        this.transacciones = transacciones;
        this.presupuestos = presupuestos;
        this.configuracionBilletera = ConfiguracionBilletera.getInstance();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(LinkedList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public LinkedList<TipoTransaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(LinkedList<TipoTransaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public LinkedList<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(LinkedList<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    @Override
    public String toString() {
        return "BilleteraVirtual{" +
                "nombre='" + nombre + '\'' +
                ", cuentas=" + cuentas +
                ", administrador=" + administrador +
                ", transacciones=" + transacciones +
                ", presupuestos=" + presupuestos +
                '}';
    }

    @Override
    public void IniciarSesion() {

    }

    @Override
    public void CrearCuenta() {

    }
}
