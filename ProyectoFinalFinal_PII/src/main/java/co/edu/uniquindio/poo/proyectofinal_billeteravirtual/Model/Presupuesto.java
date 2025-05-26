package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Clase que representa un presupuesto en la billetera virtual
 */
public class Presupuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idPresupuesto;
    private String nombre;
    private double montoTotal;
    private double montoGastado;
    private String categoriaEspecifica;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;

    /**
     * Constructor sin parámetros para serialización
     */
    public Presupuesto() {
        this.idPresupuesto = "";
        this.nombre = "";
        this.montoTotal = 0.0;
        this.montoGastado = 0.0;
        this.categoriaEspecifica = "";
        this.fechaInicio = LocalDate.now();
        this.fechaFin = LocalDate.now().plusMonths(1);
        this.activo = true;
    }

    /**
     * Constructor de la clase Presupuesto
     * @param idPresupuesto ID único del presupuesto
     * @param nombre Nombre del presupuesto
     * @param montoTotal Monto total asignado
     * @param montoGastado Monto ya gastado
     * @param categoriaEspecifica Categoría específica (opcional)
     */
    public Presupuesto(String idPresupuesto, String nombre, double montoTotal, String montoGastado, String categoriaEspecifica) {
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoTotal = montoTotal;
        this.montoGastado = Double.parseDouble(montoGastado);
        this.categoriaEspecifica = categoriaEspecifica;
        this.fechaInicio = LocalDate.now();

        // Por defecto, el presupuesto es para el mes actual
        YearMonth mesActual = YearMonth.now();
        this.fechaFin = mesActual.atEndOfMonth();
        this.activo = true;
    }

    /**
     * Constructor completo
     */
    public Presupuesto(String idPresupuesto, String nombre, double montoTotal, double montoGastado,
                      String categoriaEspecifica, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoTotal = montoTotal;
        this.montoGastado = montoGastado;
        this.categoriaEspecifica = categoriaEspecifica;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = true;
    }

    public String getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(String idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoGastado() {
        return montoGastado;
    }

    public void setMontoGastado(double montoGastado) {
        this.montoGastado = montoGastado;
    }

    public void setMontoGastado(String montoGastado) {
        this.montoGastado = Double.parseDouble(montoGastado);
    }

    public String getCategoriaEspecifica() {
        return categoriaEspecifica;
    }

    public void setCategoriaEspecifica(String categoriaEspecifica) {
        this.categoriaEspecifica = categoriaEspecifica;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Registra un gasto en el presupuesto
     * @param monto Monto del gasto
     * @return true si se registró correctamente, false si excede el presupuesto
     */
    public boolean registrarGasto(double monto) {
        if (!activo || monto <= 0) {
            return false;
        }

        montoGastado += monto;
        return true;
    }

    /**
     * Verifica si el presupuesto ha sido excedido
     * @return true si el monto gastado supera el monto total
     */
    public boolean isExcedido() {
        return montoGastado > montoTotal;
    }

    /**
     * Calcula el porcentaje de uso del presupuesto
     * @return Porcentaje de uso (0-100)
     */
    public double getPorcentajeUso() {
        if (montoTotal == 0) {
            return 0;
        }
        return (montoGastado / montoTotal) * 100;
    }

    /**
     * Calcula el monto disponible en el presupuesto
     * @return Monto disponible
     */
    public double getMontoDisponible() {
        return montoTotal - montoGastado;
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
                "idPresupuesto='" + idPresupuesto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", montoTotal=" + montoTotal +
                ", montoGastado=" + montoGastado +
                ", categoriaEspecifica='" + categoriaEspecifica + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", activo=" + activo +
                '}';
    }
}
