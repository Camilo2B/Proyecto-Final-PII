package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoCuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Pruebas unitarias para el patrón Builder de Reportes
 */
public class ReporteBuilderTest {
    
    private ReporteBuilder builder;
    private Usuario usuario;
    private List<TransaccionFactory> transacciones;
    private List<Categoria> categorias;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    @BeforeEach
    public void setUp() {
        // Crear builder
        builder = new ReporteConcreteBuilder();
        
        // Crear usuario de prueba
        LinkedList<Cuenta> cuentas = new LinkedList<>();
        Cuenta cuenta = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789", 1000.0);
        cuentas.add(cuenta);
        usuario = new Usuario("Usuario Prueba", "U001", "usuario@test.com", "1234567890", "Dirección de prueba", 1000.0, cuentas);
        
        // Crear categorías de prueba
        categorias = new ArrayList<>();
        Categoria categoria1 = new Categoria("Alimentación", "Gastos en comida");
        Categoria categoria2 = new Categoria("Transporte", "Gastos en transporte");
        categorias.add(categoria1);
        categorias.add(categoria2);
        
        // Crear transacciones de prueba
        transacciones = new ArrayList<>();
        
        // Crear una transacción de depósito
        TransaccionFactory deposito = new TransaccionFactory() {
            @Override
            public String getIdTransaccion() {
                return "T001";
            }
            
            @Override
            public LocalDate getFechaTransaccion() {
                return LocalDate.now().minusDays(5);
            }
            
            @Override
            public TipoTransaccion getTipoTransaccion() {
                return TipoTransaccion.DEPOSITO;
            }
            
            @Override
            public double getMonto() {
                return 500.0;
            }
            
            @Override
            public String getDescripcion() {
                return "Depósito de prueba";
            }
            
            @Override
            public Categoria getCategoria() {
                return categorias.get(0);
            }
        };
        
        // Crear una transacción de retiro
        TransaccionFactory retiro = new TransaccionFactory() {
            @Override
            public String getIdTransaccion() {
                return "T002";
            }
            
            @Override
            public LocalDate getFechaTransaccion() {
                return LocalDate.now().minusDays(3);
            }
            
            @Override
            public TipoTransaccion getTipoTransaccion() {
                return TipoTransaccion.RETIRO;
            }
            
            @Override
            public double getMonto() {
                return 200.0;
            }
            
            @Override
            public String getDescripcion() {
                return "Retiro de prueba";
            }
            
            @Override
            public Categoria getCategoria() {
                return categorias.get(1);
            }
        };
        
        transacciones.add(deposito);
        transacciones.add(retiro);
        
        // Establecer fechas de prueba
        fechaInicio = LocalDate.now().minusDays(10);
        fechaFin = LocalDate.now();
    }
    
    @Test
    public void testBuilderEncadenamiento() {
        // Verificar que los métodos del builder permiten encadenamiento
        ReporteBuilder builderEncadenado = builder
                .setTitulo("Reporte de Prueba")
                .setUsuario(usuario)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setTransacciones(transacciones)
                .setCategorias(categorias)
                .incluirResumenTransacciones(true)
                .incluirAnalisisPorCategoria(true)
                .incluirGraficos(true)
                .setFormato(FormatoReporte.PDF);
        
        // Verificar que el builder encadenado es el mismo objeto
        assertSame(builder, builderEncadenado);
    }
    
    @Test
    public void testBuildReporteCompleto() {
        // Construir un reporte completo
        Reporte reporte = builder
                .setTitulo("Reporte Completo")
                .setUsuario(usuario)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setTransacciones(transacciones)
                .setCategorias(categorias)
                .incluirResumenTransacciones(true)
                .incluirAnalisisPorCategoria(true)
                .incluirGraficos(true)
                .setFormato(FormatoReporte.PDF)
                .build();
        
        // Verificar que el reporte se construyó correctamente
        assertNotNull(reporte);
        assertEquals("Reporte Completo", reporte.getTitulo());
        assertEquals(usuario, reporte.getUsuario());
        assertEquals(fechaInicio, reporte.getFechaInicio());
        assertEquals(fechaFin, reporte.getFechaFin());
        assertEquals(transacciones, reporte.getTransacciones());
        assertEquals(categorias, reporte.getCategorias());
        assertTrue(reporte.isIncluirResumenTransacciones());
        assertTrue(reporte.isIncluirAnalisisPorCategoria());
        assertTrue(reporte.isIncluirGraficos());
        assertEquals(FormatoReporte.PDF, reporte.getFormato());
        assertNotNull(reporte.getContenido());
        assertNotNull(reporte.getRutaArchivo());
    }
    
    @Test
    public void testBuildReporteMinimo() {
        // Construir un reporte mínimo
        Reporte reporte = builder
                .setTitulo("Reporte Mínimo")
                .setUsuario(usuario)
                .setFormato(FormatoReporte.CSV)
                .build();
        
        // Verificar que el reporte se construyó correctamente
        assertNotNull(reporte);
        assertEquals("Reporte Mínimo", reporte.getTitulo());
        assertEquals(usuario, reporte.getUsuario());
        assertEquals(FormatoReporte.CSV, reporte.getFormato());
        assertNotNull(reporte.getContenido());
        assertNotNull(reporte.getRutaArchivo());
    }
    
    @Test
    public void testGenerarNombreArchivo() {
        // Construir un reporte
        Reporte reporte = builder
                .setTitulo("Reporte de Prueba")
                .setUsuario(usuario)
                .setFormato(FormatoReporte.PDF)
                .build();
        
        // Verificar que el nombre del archivo se generó correctamente
        String nombreArchivo = reporte.generarNombreArchivo();
        assertNotNull(nombreArchivo);
        assertTrue(nombreArchivo.startsWith("Reporte_Usuario_Prueba_"));
        assertTrue(nombreArchivo.endsWith(".pdf"));
    }
}
