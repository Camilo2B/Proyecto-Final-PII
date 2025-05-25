package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

/**
 * Pruebas unitarias para la clase Usuario
 */
public class UsuarioTest {
    
    private Usuario usuario;
    private Cuenta cuenta1;
    private Cuenta cuenta2;
    private Presupuesto presupuesto;
    
    @BeforeEach
    public void setUp() {
        // Crear cuentas de prueba
        cuenta1 = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789");
        cuenta2 = new Cuenta("C002", "Otro Banco", TipoCuenta.CORRIENTE, "987654321");
        
        // Crear usuario de prueba
        LinkedList<Cuenta> cuentas = new LinkedList<>();
        cuentas.add(cuenta1);
        usuario = new Usuario("Usuario Prueba", "U001", "usuario@test.com", "1234567890", "Dirección de prueba", 1000.0, cuentas);
        
        // Crear presupuesto de prueba
        presupuesto = new Presupuesto("P001", "Presupuesto de prueba", 500.0, "0", "Alimentación");
    }
    
    @Test
    public void testAgregarCuenta() {
        // Verificar que la cuenta1 ya está asociada
        assertNotNull(usuario.buscarCuentaPorId("C001"));
        
        // Agregar cuenta2
        assertTrue(usuario.agregarCuenta(cuenta2));
        
        // Verificar que la cuenta2 se agregó correctamente
        assertNotNull(usuario.buscarCuentaPorId("C002"));
        
        // Intentar agregar la misma cuenta de nuevo
        assertFalse(usuario.agregarCuenta(cuenta2));
    }
    
    @Test
    public void testEliminarCuenta() {
        // Verificar que la cuenta1 existe
        assertNotNull(usuario.buscarCuentaPorId("C001"));
        
        // Eliminar cuenta1
        assertTrue(usuario.eliminarCuenta("C001"));
        
        // Verificar que la cuenta1 ya no existe
        assertNull(usuario.buscarCuentaPorId("C001"));
        
        // Intentar eliminar una cuenta que no existe
        assertFalse(usuario.eliminarCuenta("C999"));
    }
    
    @Test
    public void testAgregarPresupuesto() {
        // Agregar presupuesto
        assertTrue(usuario.agregarPresupuesto(presupuesto));
        
        // Verificar que el presupuesto se agregó correctamente
        assertNotNull(usuario.buscarPresupuestoPorId("P001"));
    }
    
    @Test
    public void testEliminarPresupuesto() {
        // Agregar presupuesto
        usuario.agregarPresupuesto(presupuesto);
        
        // Verificar que el presupuesto existe
        assertNotNull(usuario.buscarPresupuestoPorId("P001"));
        
        // Eliminar presupuesto
        assertTrue(usuario.eliminarPresupuesto("P001"));
        
        // Verificar que el presupuesto ya no existe
        assertNull(usuario.buscarPresupuestoPorId("P001"));
        
        // Intentar eliminar un presupuesto que no existe
        assertFalse(usuario.eliminarPresupuesto("P999"));
    }
}
