package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class Presupuesto {

    private String idPresupuesto;
    private String nombre;
    private double montoTotal;
    private String montoGastado;
    private String categoriaEspecifica;

    public Presupuesto(String idPresupuesto, String nombre, double montoTotal, String montoGastado, String categoriaEspecifica) {
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoTotal = montoTotal;
        this.montoGastado = montoGastado;
        this.categoriaEspecifica = categoriaEspecifica;
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

    public String getMontoGastado() {
        return montoGastado;
    }

    public void setMontoGastado(String montoGastado) {
        this.montoGastado = montoGastado;
    }

    public String getCategoriaEspecifica() {
        return categoriaEspecifica;
    }

    public void setCategoriaEspecifica(String categoriaEspecifica) {
        this.categoriaEspecifica = categoriaEspecifica;
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
                "idPresupuesto='" + idPresupuesto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", montoTotal=" + montoTotal +
                ", montoGastado='" + montoGastado + '\'' +
                ", categoriaEspecifica='" + categoriaEspecifica + '\'' +
                '}';
    }
}
