package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;

/**
 * Clase que representa a un usuario de la billetera virtual
 */
public class Usuario extends Persona {

    private double saldoTotal;
    private LinkedList<Cuenta> cuentasAsociadas;
    private LinkedList<Presupuesto> presupuestos;
    private String password;

    /**
     * Constructor sin parámetros para serialización
     */
    public Usuario() {
        super();
        this.saldoTotal = 0.0;
        this.cuentasAsociadas = new LinkedList<>();
        this.presupuestos = new LinkedList<>();
        this.password = "";
    }

    /**
     * Constructor de la clase Usuario
     * @param nombre Nombre del usuario
     * @param idGeneral ID del usuario
     * @param email Email del usuario
     * @param telefono Teléfono del usuario
     * @param direccion Dirección del usuario
     * @param saldoTotal Saldo inicial del usuario
     * @param cuentasAsociadas Cuentas asociadas al usuario
     */
    public Usuario(String nombre, String idGeneral, String email, String telefono, String direccion, double saldoTotal, LinkedList<Cuenta> cuentasAsociadas) {
        super(nombre, idGeneral, email, telefono, direccion);
        this.saldoTotal = saldoTotal;
        this.cuentasAsociadas = new LinkedList<>();
        this.presupuestos = new LinkedList<>();

        if (cuentasAsociadas != null) {
            this.cuentasAsociadas.addAll(cuentasAsociadas);
        }
    }

    /**
     * Constructor con password
     */
    public Usuario(String nombre, String idGeneral, String email, String telefono, String direccion, double saldoTotal, LinkedList<Cuenta> cuentasAsociadas, String password) {
        this(nombre, idGeneral, email, telefono, direccion, saldoTotal, cuentasAsociadas);
        this.password = password;
    }

    public double getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public LinkedList<Cuenta> getCuentasAsociadas() {
        return cuentasAsociadas;
    }

    public void setCuentasAsociadas(LinkedList<Cuenta> cuentasAsociadas) {
        this.cuentasAsociadas = cuentasAsociadas;
    }

    public LinkedList<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(LinkedList<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Agrega una cuenta al usuario
     * @param cuenta Cuenta a agregar
     * @return true si se agregó correctamente, false si ya existe
     */
    public boolean agregarCuenta(Cuenta cuenta) {
        if (buscarCuentaPorId(cuenta.getIdCuenta()) != null) {
            return false;
        }
        return cuentasAsociadas.add(cuenta);
    }

    /**
     * Elimina una cuenta del usuario
     * @param idCuenta ID de la cuenta a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    public boolean eliminarCuenta(String idCuenta) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) {
            return false;
        }
        return cuentasAsociadas.remove(cuenta);
    }

    /**
     * Busca una cuenta por su ID
     * @param idCuenta ID de la cuenta
     * @return La cuenta encontrada o null si no existe
     */
    public Cuenta buscarCuentaPorId(String idCuenta) {
        return cuentasAsociadas.stream()
                .filter(c -> c.getIdCuenta().equals(idCuenta))
                .findFirst()
                .orElse(null);
    }

    /**
     * Agrega un presupuesto al usuario
     * @param presupuesto Presupuesto a agregar
     * @return true si se agregó correctamente
     */
    public boolean agregarPresupuesto(Presupuesto presupuesto) {
        return presupuestos.add(presupuesto);
    }

    /**
     * Elimina un presupuesto del usuario
     * @param idPresupuesto ID del presupuesto a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    public boolean eliminarPresupuesto(String idPresupuesto) {
        Presupuesto presupuesto = buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto == null) {
            return false;
        }
        return presupuestos.remove(presupuesto);
    }

    /**
     * Busca un presupuesto por su ID
     * @param idPresupuesto ID del presupuesto
     * @return El presupuesto encontrado o null si no existe
     */
    public Presupuesto buscarPresupuestoPorId(String idPresupuesto) {
        return presupuestos.stream()
                .filter(p -> p.getIdPresupuesto().equals(idPresupuesto))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + getNombre() + '\'' +
                ", idGeneral='" + getIdGeneral() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", saldoTotal=" + saldoTotal +
                ", cuentasAsociadas=" + cuentasAsociadas +
                '}';
    }
}
