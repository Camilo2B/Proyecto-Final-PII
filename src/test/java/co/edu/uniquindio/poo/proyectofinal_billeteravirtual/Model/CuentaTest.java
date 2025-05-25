package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Cuenta
 */
public class CuentaTest {
    
    private Cuenta cuenta;
    private Cuenta cuentaDestino;
    
    @BeforeEach
    public void setUp() {
        cuenta = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789", 1000.0);
        cuentaDestino = new Cuenta("C002", "Otro Banco", TipoCuenta.CORRIENTE, "987654321", 500.0);
    }
    
    @Test
    public void testDepositar() {
        // Depositar un monto válido
        assertTrue(cuenta.depositar(500.0));
        assertEquals(1500.0, cuenta.getSaldo());
        
        // Intentar depositar un monto inválido
        assertFalse(cuenta.depositar(-100.0));
        assertEquals(1500.0, cuenta.getSaldo());
        
        // Intentar depositar en una cuenta inactiva
        cuenta.setActiva(false);
        assertFalse(cuenta.depositar(200.0));
        assertEquals(1500.0, cuenta.getSaldo());
    }
    
    @Test
    public void testRetirar() {
        // Retirar un monto válido
        assertTrue(cuenta.retirar(300.0));
        assertEquals(700.0, cuenta.getSaldo());
        
        // Intentar retirar un monto inválido
        assertFalse(cuenta.retirar(-100.0));
        assertEquals(700.0, cuenta.getSaldo());
        
        // Intentar retirar más del saldo disponible
        assertFalse(cuenta.retirar(1000.0));
        assertEquals(700.0, cuenta.getSaldo());
        
        // Intentar retirar de una cuenta inactiva
        cuenta.setActiva(false);
        assertFalse(cuenta.retirar(200.0));
        assertEquals(700.0, cuenta.getSaldo());
    }
    
    @Test
    public void testTransferir() {
        // Transferir un monto válido
        assertTrue(cuenta.transferir(cuentaDestino, 300.0));
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Intentar transferir un monto inválido
        assertFalse(cuenta.transferir(cuentaDestino, -100.0));
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Intentar transferir más del saldo disponible
        assertFalse(cuenta.transferir(cuentaDestino, 1000.0));
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Intentar transferir desde una cuenta inactiva
        cuenta.setActiva(false);
        assertFalse(cuenta.transferir(cuentaDestino, 200.0));
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        
        // Intentar transferir a una cuenta inactiva
        cuenta.setActiva(true);
        cuentaDestino.setActiva(false);
        assertFalse(cuenta.transferir(cuentaDestino, 200.0));
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
    }
}
