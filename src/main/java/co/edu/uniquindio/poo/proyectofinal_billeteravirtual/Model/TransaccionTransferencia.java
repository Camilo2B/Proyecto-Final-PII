package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.time.LocalDate;

public class TransaccionTransferencia extends TransaccionFactory {

    private Usuario usuarioDestino;
    private Cuenta cuentaDestino;

    public TransaccionTransferencia() {
        super(null, LocalDate.now(), TipoTransaccion.TRANSFERENCIA, 0.0, "Transferencia", null, null);
    }

    public TransaccionTransferencia(String idTransaccion, LocalDate fechaTransaccion, double monto, String descripcion, Categoria categoria, Usuario usuario, Usuario usuarioDestino, Cuenta cuentaDestino) {
        super(idTransaccion, fechaTransaccion, TipoTransaccion.TRANSFERENCIA, monto, descripcion, null, categoria, usuario);
        this.usuarioDestino = usuarioDestino;
        this.cuentaDestino = cuentaDestino;
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    @Override
    public void CrearTransaccion() {
        // Lógica para crear una transacción de transferencia
        System.out.println("Creando transacción de transferencia");
    }

    @Override
    public void llenarDatos() {
        System.out.println("Llenando datos de transacción de transferencia");
    }
}
