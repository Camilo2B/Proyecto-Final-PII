package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoCuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Pruebas unitarias para el patrón Command de Operaciones Financieras
 */
public class CommandTest {
    
    private Usuario usuario;
    private Usuario usuarioDestino;
    private Cuenta cuenta;
    private Cuenta cuentaDestino;
    private Categoria categoria;
    private CommandInvoker invoker;
    
    @BeforeEach
    public void setUp() {
        // Crear usuarios de prueba
        LinkedList<Cuenta> cuentas = new LinkedList<>();
        usuario = new Usuario("Usuario Prueba", "U001", "usuario@test.com", "1234567890", "Dirección de prueba", 1000.0, cuentas);
        
        LinkedList<Cuenta> cuentasDestino = new LinkedList<>();
        usuarioDestino = new Usuario("Usuario Destino", "U002", "destino@test.com", "0987654321", "Otra dirección", 500.0, cuentasDestino);
        
        // Crear cuentas de prueba
        cuenta = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789", 1000.0);
        cuentaDestino = new Cuenta("C002", "Otro Banco", TipoCuenta.CORRIENTE, "987654321", 500.0);
        
        // Agregar cuentas a los usuarios
        usuario.agregarCuenta(cuenta);
        usuarioDestino.agregarCuenta(cuentaDestino);
        
        // Crear categoría de prueba
        categoria = new Categoria("Prueba", "Categoría de prueba");
        
        // Obtener el invoker
        invoker = CommandInvoker.getInstance();
    }
    
    @Test
    public void testDepositoCommand() {
        // Crear comando de depósito
        Command depositoCommand = new DepositoCommand(usuario, cuenta, 500.0, "Depósito de prueba", categoria);
        
        // Ejecutar el comando
        assertTrue(invoker.ejecutarComando(depositoCommand));
        
        // Verificar que el saldo se actualizó correctamente
        assertEquals(1500.0, usuario.getSaldoTotal());
        assertEquals(1500.0, cuenta.getSaldo());
        
        // Verificar que el comando se agregó al historial
        List<Command> historial = invoker.getHistorialComandos();
        assertFalse(historial.isEmpty());
        assertEquals(depositoCommand, historial.get(historial.size() - 1));
        
        // Deshacer el comando
        assertTrue(invoker.deshacer());
        
        // Verificar que el saldo volvió al valor original
        assertEquals(1000.0, usuario.getSaldoTotal());
        assertEquals(1000.0, cuenta.getSaldo());
        
        // Verificar que se puede rehacer el comando
        assertTrue(invoker.rehacer());
        
        // Verificar que el saldo se actualizó nuevamente
        assertEquals(1500.0, usuario.getSaldoTotal());
        assertEquals(1500.0, cuenta.getSaldo());
    }
    
    @Test
    public void testRetiroCommand() {
        // Crear comando de retiro
        Command retiroCommand = new RetiroCommand(usuario, cuenta, 300.0, "Retiro de prueba", categoria);
        
        // Ejecutar el comando
        assertTrue(invoker.ejecutarComando(retiroCommand));
        
        // Verificar que el saldo se actualizó correctamente
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
        
        // Verificar que el comando se agregó al historial
        List<Command> historial = invoker.getHistorialComandos();
        assertFalse(historial.isEmpty());
        assertEquals(retiroCommand, historial.get(historial.size() - 1));
        
        // Deshacer el comando
        assertTrue(invoker.deshacer());
        
        // Verificar que el saldo volvió al valor original
        assertEquals(1000.0, usuario.getSaldoTotal());
        assertEquals(1000.0, cuenta.getSaldo());
        
        // Verificar que se puede rehacer el comando
        assertTrue(invoker.rehacer());
        
        // Verificar que el saldo se actualizó nuevamente
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
    }
    
    @Test
    public void testTransferenciaCommand() {
        // Crear comando de transferencia
        Command transferenciaCommand = new TransferenciaCommand(
            usuario, cuenta, usuarioDestino, cuentaDestino, 300.0, "Transferencia de prueba", categoria);
        
        // Ejecutar el comando
        assertTrue(invoker.ejecutarComando(transferenciaCommand));
        
        // Verificar que los saldos se actualizaron correctamente
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, usuarioDestino.getSaldoTotal());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Verificar que el comando se agregó al historial
        List<Command> historial = invoker.getHistorialComandos();
        assertFalse(historial.isEmpty());
        assertEquals(transferenciaCommand, historial.get(historial.size() - 1));
        
        // Deshacer el comando
        assertTrue(invoker.deshacer());
        
        // Verificar que los saldos volvieron a los valores originales
        assertEquals(1000.0, usuario.getSaldoTotal());
        assertEquals(1000.0, cuenta.getSaldo());
        assertEquals(500.0, usuarioDestino.getSaldoTotal());
        assertEquals(500.0, cuentaDestino.getSaldo());
        
        // Verificar que se puede rehacer el comando
        assertTrue(invoker.rehacer());
        
        // Verificar que los saldos se actualizaron nuevamente
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, usuarioDestino.getSaldoTotal());
        assertEquals(800.0, cuentaDestino.getSaldo());
    }
    
    @Test
    public void testCommandInvoker() {
        // Verificar estado inicial del invoker
        assertFalse(invoker.puedeDeshacer());
        assertFalse(invoker.puedeRehacer());
        assertTrue(invoker.getHistorialComandos().isEmpty());
        
        // Ejecutar un comando
        Command comando1 = new DepositoCommand(usuario, cuenta, 100.0, "Depósito 1", null);
        assertTrue(invoker.ejecutarComando(comando1));
        
        // Verificar que se puede deshacer pero no rehacer
        assertTrue(invoker.puedeDeshacer());
        assertFalse(invoker.puedeRehacer());
        assertEquals(1, invoker.getHistorialComandos().size());
        
        // Ejecutar otro comando
        Command comando2 = new RetiroCommand(usuario, cuenta, 50.0, "Retiro 1", null);
        assertTrue(invoker.ejecutarComando(comando2));
        
        // Verificar que se puede deshacer pero no rehacer
        assertTrue(invoker.puedeDeshacer());
        assertFalse(invoker.puedeRehacer());
        assertEquals(2, invoker.getHistorialComandos().size());
        
        // Deshacer el último comando
        assertTrue(invoker.deshacer());
        
        // Verificar que se puede deshacer y rehacer
        assertTrue(invoker.puedeDeshacer());
        assertTrue(invoker.puedeRehacer());
        assertEquals(1, invoker.getHistorialComandos().size());
        
        // Deshacer el primer comando
        assertTrue(invoker.deshacer());
        
        // Verificar que no se puede deshacer pero sí rehacer
        assertFalse(invoker.puedeDeshacer());
        assertTrue(invoker.puedeRehacer());
        assertTrue(invoker.getHistorialComandos().isEmpty());
        
        // Rehacer el primer comando
        assertTrue(invoker.rehacer());
        
        // Verificar que se puede deshacer y rehacer
        assertTrue(invoker.puedeDeshacer());
        assertTrue(invoker.puedeRehacer());
        assertEquals(1, invoker.getHistorialComandos().size());
        
        // Ejecutar un nuevo comando (debe borrar la historia de rehacer)
        Command comando3 = new DepositoCommand(usuario, cuenta, 200.0, "Depósito 2", null);
        assertTrue(invoker.ejecutarComando(comando3));
        
        // Verificar que se puede deshacer pero no rehacer
        assertTrue(invoker.puedeDeshacer());
        assertFalse(invoker.puedeRehacer());
        assertEquals(2, invoker.getHistorialComandos().size());
    }
    
    @Test
    public void testCommandListener() {
        // Crear un contador para verificar las notificaciones
        final int[] contador = {0};
        
        // Crear un listener
        CommandInvoker.CommandListener listener = () -> contador[0]++;
        
        // Agregar el listener al invoker
        invoker.agregarListener(listener);
        
        // Ejecutar un comando (debe notificar)
        Command comando = new DepositoCommand(usuario, cuenta, 100.0, "Depósito", null);
        invoker.ejecutarComando(comando);
        assertEquals(1, contador[0]);
        
        // Deshacer el comando (debe notificar)
        invoker.deshacer();
        assertEquals(2, contador[0]);
        
        // Rehacer el comando (debe notificar)
        invoker.rehacer();
        assertEquals(3, contador[0]);
        
        // Eliminar el listener
        invoker.eliminarListener(listener);
        
        // Ejecutar otro comando (no debe notificar)
        invoker.ejecutarComando(comando);
        assertEquals(3, contador[0]);
    }
}
