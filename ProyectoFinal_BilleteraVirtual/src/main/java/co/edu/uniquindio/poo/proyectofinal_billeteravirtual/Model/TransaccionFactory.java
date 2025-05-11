package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;
import java.util.LinkedList;

public abstract class TransaccionFactory implements Transaccion {

    private String idTransaccion;
    private LocalDate fechaTransaccion;
    private TipoTransaccion tipoTransaccion;
    private double monto;
    private String descripcion;
    private LinkedList<Cuenta> cuentasAsociadas;
    private String empresaDestido;
    private Categoria categoria;

    public TransaccionFactory(String idTransaccion, LocalDate fechaTransaccion, TipoTransaccion tipoTransaccion, double monto, String descripcion, String empresaDestido, Categoria categoria) {
        this.idTransaccion = idTransaccion;
        this.fechaTransaccion = fechaTransaccion;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.empresaDestido = empresaDestido;
        this.categoria = categoria;

    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LinkedList<Cuenta> getCuentasAsociadas() {
        return cuentasAsociadas;
    }

    public void setCuentasAsociadas(LinkedList<Cuenta> cuentasAsociadas) {
        this.cuentasAsociadas = cuentasAsociadas;
    }

    public String getEmpresaDestido() {
        return empresaDestido;
    }

    public void setEmpresaDestido(String empresaDestido) {
        this.empresaDestido = empresaDestido;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public abstract void CrearTransaccion();

    @Override
    public void llenarDatos() {
        System.out.println("Datos llenados");
    }
}


