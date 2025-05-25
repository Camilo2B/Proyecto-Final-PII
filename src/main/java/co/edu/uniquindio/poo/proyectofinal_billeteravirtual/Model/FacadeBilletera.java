package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;
import java.util.Map;

/**
 * Implementación del patrón Facade para la billetera virtual
 * Proporciona una interfaz unificada para todas las operaciones de la billetera
 */
public class FacadeBilletera implements BilleteraVirtual {

    private String nombre;
    private LinkedList<Cuenta> cuentas;
    private Administrador administrador;
    private LinkedList<Presupuesto> presupuestos;
    private ConfiguracionBilletera configuracionBilletera;
    private BilleteraService billeteraService;
    private AuthenticationService authService;
    private DataManager dataManager;

    /**
     * Constructor de la clase FacadeBilletera
     */
    public FacadeBilletera() {
        this.nombre = "Billetera Virtual";
        this.cuentas = new LinkedList<>();
        this.presupuestos = new LinkedList<>();
        this.configuracionBilletera = ConfiguracionBilletera.getInstance();
        this.billeteraService = new BilleteraService();
        this.authService = AuthenticationService.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Constructor completo
     */
    public FacadeBilletera(String nombre, LinkedList<Cuenta> cuentas, Administrador administrador, LinkedList<Presupuesto> presupuestos) {
        this();
        this.nombre = nombre;
        this.cuentas = cuentas;
        this.administrador = administrador;
        this.presupuestos = presupuestos;
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
                ", presupuestos=" + presupuestos +
                '}';
    }

    @Override
    public boolean iniciarSesion(String id, String password, boolean esAdmin) {
        if (esAdmin) {
            return authService.autenticarAdmin(id, password);
        } else {
            return authService.autenticarUsuario(id, password);
        }
    }

    @Override
    public void cerrarSesion() {
        authService.cerrarSesion();
    }

    @Override
    public boolean crearCuenta(String nombreBanco, TipoCuenta tipoCuenta, String numeroCuenta) {
        if (authService.hayUsuarioAutenticado()) {
            String idCuenta = dataManager.generarId();
            Cuenta cuenta = new Cuenta(idCuenta, nombreBanco, tipoCuenta, numeroCuenta);
            return authService.getUsuarioAutenticado().agregarCuenta(cuenta);
        } else if (authService.hayAdminAutenticado()) {
            return authService.getAdminAutenticado().crearCuenta(nombreBanco, tipoCuenta, numeroCuenta) != null;
        }
        return false;
    }

    @Override
    public boolean realizarDeposito(double monto, String idCuenta) {
        return billeteraService.realizarDeposito(monto, idCuenta);
    }

    @Override
    public boolean realizarRetiro(double monto, String idCuenta) {
        return billeteraService.realizarRetiro(monto, idCuenta);
    }

    @Override
    public boolean realizarTransferencia(double monto, String idCuentaOrigen, String idCuentaDestino) {
        return billeteraService.realizarTransferencia(monto, idCuentaOrigen, idCuentaDestino);
    }

    @Override
    public boolean crearPresupuesto(String nombre, double montoTotal, String categoriaEspecifica) {
        return billeteraService.crearPresupuesto(nombre, montoTotal, categoriaEspecifica);
    }

    @Override
    public LinkedList<TransaccionFactory> obtenerTransacciones() {
        return billeteraService.obtenerTransaccionesUsuario();
    }

    @Override
    public LinkedList<Presupuesto> obtenerPresupuestos() {
        return billeteraService.obtenerPresupuestosUsuario();
    }

    @Override
    public LinkedList<Categoria> obtenerCategorias() {
        return billeteraService.obtenerCategorias();
    }

    @Override
    public Map<String, Object> obtenerEstadisticas() {
        return billeteraService.obtenerEstadisticas();
    }
}
