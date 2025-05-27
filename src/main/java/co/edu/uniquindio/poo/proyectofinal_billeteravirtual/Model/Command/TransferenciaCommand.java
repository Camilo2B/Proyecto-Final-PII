package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

/**
 * Comando concreto para realizar una transferencia
 */
public class TransferenciaCommand implements Command {
    private final BilleteraService billeteraService;
    private final Usuario usuarioOrigen;
    private final Cuenta cuentaOrigen;
    private final Usuario usuarioDestino;
    private final Cuenta cuentaDestino;
    private final double monto;
    private final String descripcion;
    private final Categoria categoria;
    private TransaccionFactory transaccion;

    /**
     * Constructor con todos los parámetros necesarios para una transferencia
     * @param usuarioOrigen Usuario que realiza la transferencia
     * @param cuentaOrigen Cuenta de origen
     * @param usuarioDestino Usuario que recibe la transferencia
     * @param cuentaDestino Cuenta de destino
     * @param monto Monto de la transferencia
     * @param descripcion Descripción de la transferencia
     * @param categoria Categoría de la transferencia
     */
    public TransferenciaCommand(Usuario usuarioOrigen, Cuenta cuentaOrigen,
                               Usuario usuarioDestino, Cuenta cuentaDestino,
                               double monto, String descripcion, Categoria categoria) {
        this.billeteraService = BilleteraService.getInstance();
        this.usuarioOrigen = usuarioOrigen;
        this.cuentaOrigen = cuentaOrigen;
        this.usuarioDestino = usuarioDestino;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    @Override
    public boolean ejecutar() {
        try {
            // Verificar si hay saldo suficiente
            if (cuentaOrigen != null && cuentaOrigen.getSaldo() < monto) {
                return false;
            }

            if (usuarioOrigen.getSaldoTotal() < monto) {
                return false;
            }

            // Realizar la transferencia
            transaccion = billeteraService.realizarTransferencia(
                usuarioOrigen, cuentaOrigen, usuarioDestino, cuentaDestino, monto, descripcion, categoria);

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

            // Revertir la transferencia
            usuarioOrigen.setSaldoTotal(usuarioOrigen.getSaldoTotal() + monto);
            usuarioDestino.setSaldoTotal(usuarioDestino.getSaldoTotal() - monto);

            // Revertir el saldo de las cuentas
            if (cuentaOrigen != null) {
                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() + monto);
            }

            if (cuentaDestino != null) {
                cuentaDestino.setSaldo(cuentaDestino.getSaldo() - monto);
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
        return "Transferencia de $" + String.format("%.2f", monto) +
               " de " + usuarioOrigen.getNombre() +
               (cuentaOrigen != null ? " (cuenta " + cuentaOrigen.getNumeroCuenta() + ")" : "") +
               " a " + usuarioDestino.getNombre() +
               (cuentaDestino != null ? " (cuenta " + cuentaDestino.getNumeroCuenta() + ")" : "") +
               ": " + descripcion;
    }
}
