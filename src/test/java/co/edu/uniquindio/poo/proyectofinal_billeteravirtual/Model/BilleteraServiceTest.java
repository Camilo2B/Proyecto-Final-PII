package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.FormatoReporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.Reporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.ReporteConcreteBuilder;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.ReporteDirector;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command.CommandInvoker;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.GestorCategorias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Pruebas unitarias para la clase BilleteraService
 */
public class BilleteraServiceTest {
    
    private BilleteraService billeteraService;
    private Usuario usuario;
    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Categoria categoria;
    
    @BeforeEach
    public void setUp() {
        // Crear servicio
        billeteraService = new BilleteraService();
        
        // Crear usuario de prueba
        usuario = new Usuario("Usuario Prueba", "U001", "usuario@test.com", "1234567890", "Dirección de prueba", 1000.0, null, "password");
        
        // Crear cuentas de prueba
        cuenta1 = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789", 1000.0);
        cuenta2 = new Cuenta("C002", "Otro Banco", TipoCuenta.CORRIENTE, "987654321", 500.0);
        
        // Agregar cuentas al usuario
        usuario.agregarCuenta(cuenta1);
        usuario.agregarCuenta(cuenta2);
        
        // Crear categoría de prueba
        categoria = new Categoria("Prueba", "Categoría de prueba");
        
        // Agregar usuario al DataManager
        DataManager.getInstance().agregarUsuario(usuario);
    }
    
    @Test
    public void testIniciarSesion() {
        // Verificar inicio de sesión exitoso
        assertTrue(billeteraService.iniciarSesionUsuario("U001", "password"));
        assertTrue(billeteraService.hayUsuarioLogueado());
        assertEquals(usuario.getIdGeneral(), billeteraService.getUsuarioActual().getIdGeneral());
        
        // Verificar inicio de sesión fallido (contraseña incorrecta)
        billeteraService.cerrarSesion();
        assertFalse(billeteraService.iniciarSesionUsuario("U001", "contraseña_incorrecta"));
        assertFalse(billeteraService.hayUsuarioLogueado());
        
        // Verificar inicio de sesión fallido (usuario no existe)
        assertFalse(billeteraService.iniciarSesionUsuario("U999", "password"));
        assertFalse(billeteraService.hayUsuarioLogueado());
    }
    
    @Test
    public void testRealizarDeposito() {
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Realizar depósito sin especificar cuenta
        assertTrue(billeteraService.realizarDeposito(500.0, null));
        assertEquals(1500.0, billeteraService.getUsuarioActual().getSaldoTotal());
        
        // Realizar depósito en cuenta específica
        assertTrue(billeteraService.realizarDeposito(300.0, "123456789"));
        assertEquals(1800.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(1300.0, cuenta1.getSaldo());
        
        // Realizar depósito con categoría
        assertTrue(billeteraService.realizarDeposito(200.0, "123456789", "Depósito con categoría", categoria));
        assertEquals(2000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(1500.0, cuenta1.getSaldo());
        
        // Verificar que se puede deshacer el último depósito
        assertTrue(CommandInvoker.getInstance().deshacer());
        assertEquals(1800.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(1300.0, cuenta1.getSaldo());
    }
    
    @Test
    public void testRealizarRetiro() {
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Realizar retiro sin especificar cuenta
        assertTrue(billeteraService.realizarRetiro(300.0, null));
        assertEquals(700.0, billeteraService.getUsuarioActual().getSaldoTotal());
        
        // Realizar retiro en cuenta específica
        assertTrue(billeteraService.realizarRetiro(200.0, "123456789"));
        assertEquals(500.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(800.0, cuenta1.getSaldo());
        
        // Realizar retiro con categoría
        assertTrue(billeteraService.realizarRetiro(100.0, "123456789", "Retiro con categoría", categoria));
        assertEquals(400.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(700.0, cuenta1.getSaldo());
        
        // Verificar que se puede deshacer el último retiro
        assertTrue(CommandInvoker.getInstance().deshacer());
        assertEquals(500.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(800.0, cuenta1.getSaldo());
        
        // Intentar retirar más del saldo disponible
        assertFalse(billeteraService.realizarRetiro(2000.0, null));
        assertEquals(500.0, billeteraService.getUsuarioActual().getSaldoTotal());
        
        // Intentar retirar más del saldo de la cuenta
        assertFalse(billeteraService.realizarRetiro(900.0, "123456789"));
        assertEquals(500.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(800.0, cuenta1.getSaldo());
    }
    
    @Test
    public void testRealizarTransferencia() {
        // Crear otro usuario para transferencias
        Usuario usuarioDestino = new Usuario("Usuario Destino", "U002", "destino@test.com", "0987654321", "Otra dirección", 500.0, null, "password");
        Cuenta cuentaDestino = new Cuenta("C003", "Banco Destino", TipoCuenta.AHORRO, "555555555", 500.0);
        usuarioDestino.agregarCuenta(cuentaDestino);
        DataManager.getInstance().agregarUsuario(usuarioDestino);
        
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Realizar transferencia entre cuentas del mismo usuario
        assertTrue(billeteraService.realizarTransferencia(300.0, "123456789", "987654321"));
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(700.0, cuenta1.getSaldo());
        assertEquals(800.0, cuenta2.getSaldo());
        
        // Realizar transferencia a otro usuario
        assertTrue(billeteraService.realizarTransferencia(200.0, "123456789", "555555555"));
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(500.0, cuenta1.getSaldo());
        assertEquals(800.0, cuenta2.getSaldo());
        assertEquals(700.0, cuentaDestino.getSaldo());
        
        // Realizar transferencia con categoría
        assertTrue(billeteraService.realizarTransferencia(100.0, "123456789", "555555555", "Transferencia con categoría", categoria));
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(400.0, cuenta1.getSaldo());
        assertEquals(800.0, cuenta2.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Verificar que se puede deshacer la última transferencia
        assertTrue(CommandInvoker.getInstance().deshacer());
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(500.0, cuenta1.getSaldo());
        assertEquals(800.0, cuenta2.getSaldo());
        assertEquals(700.0, cuentaDestino.getSaldo());
        
        // Intentar transferir más del saldo disponible
        assertFalse(billeteraService.realizarTransferencia(2000.0, "123456789", "555555555"));
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(500.0, cuenta1.getSaldo());
        assertEquals(700.0, cuentaDestino.getSaldo());
        
        // Intentar transferir a una cuenta que no existe
        assertFalse(billeteraService.realizarTransferencia(100.0, "123456789", "999999999"));
        assertEquals(1000.0, billeteraService.getUsuarioActual().getSaldoTotal());
        assertEquals(500.0, cuenta1.getSaldo());
    }
    
    @Test
    public void testCrearPresupuesto() {
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Crear presupuesto sin categoría específica
        assertTrue(billeteraService.crearPresupuesto("Presupuesto General", 500.0, null));
        assertEquals(1, billeteraService.obtenerPresupuestosUsuario().size());
        
        // Crear presupuesto con categoría específica
        assertTrue(billeteraService.crearPresupuesto("Presupuesto Alimentación", 300.0, "Alimentación"));
        assertEquals(2, billeteraService.obtenerPresupuestosUsuario().size());
        
        // Verificar que no se pueden crear presupuestos sin iniciar sesión
        billeteraService.cerrarSesion();
        assertFalse(billeteraService.crearPresupuesto("Presupuesto Inválido", 200.0, null));
    }
    
    @Test
    public void testObtenerTransaccionesUsuario() {
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Verificar que inicialmente no hay transacciones
        assertTrue(billeteraService.obtenerTransaccionesUsuario().isEmpty());
        
        // Realizar algunas transacciones
        billeteraService.realizarDeposito(500.0, null);
        billeteraService.realizarRetiro(200.0, null);
        
        // Verificar que se registraron las transacciones
        List<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        assertFalse(transacciones.isEmpty());
        
        // Verificar que no se pueden obtener transacciones sin iniciar sesión
        billeteraService.cerrarSesion();
        assertTrue(billeteraService.obtenerTransaccionesUsuario().isEmpty());
    }
}
