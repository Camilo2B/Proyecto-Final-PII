package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

/**
 * Comando concreto para realizar un depósito
 */
public class DepositoCommand implements Command {
    private final BilleteraService billeteraService;
    private final Usuario usuario;
    private final Cuenta cuenta;
    private final double monto;
    private final String descripcion;
    private final Categoria categoria;
    private TransaccionFactory transaccion;

    /**
     * Constructor con todos los parámetros necesarios para un depósito
     * @param usuario Usuario que realiza el depósito
     * @param cuenta Cuenta donde se realiza el depósito
     * @param monto Monto del depósito
     * @param descripcion Descripción del depósito
     * @param categoria Categoría del depósito
     */
    public DepositoCommand(Usuario usuario, Cuenta cuenta, double monto, String descripcion, Categoria categoria) {
        this.billeteraService = BilleteraService.getInstance();
        this.usuario = usuario;
        this.cuenta = cuenta;
        this.monto = monto;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    @Override
    public boolean ejecutar() {
        try {
            // Realizar el depósito
            transaccion = billeteraService.realizarDeposito(usuario, cuenta, monto, descripcion, categoria);

            // Guardar los cambios
            DataManager.getInstance().guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deshacer() {
        try {
            if (transaccion == null) {
                return false;
            }

            // Revertir el depósito
            usuario.setSaldoTotal(usuario.getSaldoTotal() - monto);

            // Revertir el saldo de la cuenta
            if (cuenta != null) {
                cuenta.setSaldo(cuenta.getSaldo() - monto);
            }

            // Eliminar la transacción
            billeteraService.eliminarTransaccion(transaccion);

            // Guardar los cambios
            DataManager.getInstance().guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getDescripcion() {
        return "Depósito de $" + String.format("%.2f", monto) +
               (cuenta != null ? " en cuenta " + cuenta.getNumeroCuenta() : "") +
               ": " + descripcion;
    }
}
