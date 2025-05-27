package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Pruebas unitarias para la clase Presupuesto
 */
public class PresupuestoTest {
    
    private Presupuesto presupuesto;
    
    @BeforeEach
    public void setUp() {
        presupuesto = new Presupuesto("P001", "Presupuesto de prueba", 1000.0, "0", "Alimentación");
    }
    
    @Test
    public void testRegistrarGasto() {
        // Registrar un gasto válido
        assertTrue(presupuesto.registrarGasto(300.0));
        assertEquals(300.0, presupuesto.getMontoGastado());
        
        // Registrar otro gasto válido
        assertTrue(presupuesto.registrarGasto(200.0));
        assertEquals(500.0, presupuesto.getMontoGastado());
        
        // Intentar registrar un gasto inválido
        assertFalse(presupuesto.registrarGasto(-100.0));
        assertEquals(500.0, presupuesto.getMontoGastado());
        
        // Intentar registrar un gasto en un presupuesto inactivo
        presupuesto.setActivo(false);
        assertFalse(presupuesto.registrarGasto(200.0));
        assertEquals(500.0, presupuesto.getMontoGastado());
    }
    
    @Test
    public void testIsExcedido() {
        // Inicialmente no está excedido
        assertFalse(presupuesto.isExcedido());
        
        // Registrar gastos que no exceden el presupuesto
        presupuesto.registrarGasto(500.0);
        assertFalse(presupuesto.isExcedido());
        
        // Registrar gastos que exceden el presupuesto
        presupuesto.registrarGasto(600.0);
        assertTrue(presupuesto.isExcedido());
    }
    
    @Test
    public void testGetPorcentajeUso() {
        // Inicialmente 0%
        assertEquals(0.0, presupuesto.getPorcentajeUso());
        
        // 30% de uso
        presupuesto.registrarGasto(300.0);
        assertEquals(30.0, presupuesto.getPorcentajeUso());
        
        // 80% de uso
        presupuesto.registrarGasto(500.0);
        assertEquals(80.0, presupuesto.getPorcentajeUso());
        
        // 120% de uso (excedido)
        presupuesto.registrarGasto(400.0);
        assertEquals(120.0, presupuesto.getPorcentajeUso());
    }
    
    @Test
    public void testGetMontoDisponible() {
        // Inicialmente todo disponible
        assertEquals(1000.0, presupuesto.getMontoDisponible());
        
        // Después de gastar 300
        presupuesto.registrarGasto(300.0);
        assertEquals(700.0, presupuesto.getMontoDisponible());
        
        // Después de gastar 500 más
        presupuesto.registrarGasto(500.0);
        assertEquals(200.0, presupuesto.getMontoDisponible());
        
        // Después de exceder el presupuesto
        presupuesto.registrarGasto(400.0);
        assertEquals(-200.0, presupuesto.getMontoDisponible());
    }
}
