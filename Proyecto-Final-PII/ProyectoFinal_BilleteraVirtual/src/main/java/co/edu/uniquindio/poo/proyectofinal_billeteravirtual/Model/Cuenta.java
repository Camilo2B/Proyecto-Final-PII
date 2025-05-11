package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class Cuenta implements ICuenta{

    private String IdCuenta;
    private String nombreBanco;
    private TipoCuenta tipoCuenta;
    private String numeroCuenta;

    public Cuenta(String idCuenta, String nombreBanco, TipoCuenta tipoCuenta, String numeroCuenta) {
        IdCuenta = idCuenta;
        this.nombreBanco = nombreBanco;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
    }

    public String getIdCuenta() {
        return IdCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        IdCuenta = idCuenta;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    @Override
    public String toString() {
        return "Cuentas{" +
                "IdCuenta='" + IdCuenta + '\'' +
                ", nombreBanco='" + nombreBanco + '\'' +
                ", tipoCuenta=" + tipoCuenta +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                '}';
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
