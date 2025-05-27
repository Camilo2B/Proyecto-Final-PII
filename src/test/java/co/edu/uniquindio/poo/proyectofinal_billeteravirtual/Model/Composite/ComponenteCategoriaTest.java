package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas unitarias para el patrón Composite de Categorías
 */
public class ComponenteCategoriaTest {
    
    private CategoriaSimple categoriaAlimentacion;
    private CategoriaSimple categoriaRestaurantes;
    private CategoriaSimple categoriaSupermercado;
    private GrupoCategoria grupoGastos;
    private GrupoCategoria grupoAlimentacion;
    private List<TransaccionFactory> transacciones;
    
    @BeforeEach
    public void setUp() {
        // Crear categorías simples
        categoriaAlimentacion = new CategoriaSimple("Alimentación", "Gastos en comida");
        categoriaRestaurantes = new CategoriaSimple("Restaurantes", "Comidas en restaurantes");
        categoriaSupermercado = new CategoriaSimple("Supermercado", "Compras en supermercados");
        
        // Crear grupos de categorías
        grupoGastos = new GrupoCategoria("Gastos", "Categorías de gastos");
        grupoAlimentacion = new GrupoCategoria("Alimentación", "Categorías de alimentación");
        
        // Crear jerarquía de categorías
        grupoGastos.agregarComponente(grupoAlimentacion);
        grupoAlimentacion.agregarComponente(categoriaRestaurantes);
        grupoAlimentacion.agregarComponente(categoriaSupermercado);
        
        // Crear transacciones de prueba
        transacciones = new ArrayList<>();
        
        // Transacción de Restaurantes
        transacciones.add(new TransaccionFactory() {
            @Override
            public String getIdTransaccion() {
                return "T001";
            }
            
            @Override
            public LocalDate getFechaTransaccion() {
                return LocalDate.now();
            }
            
            @Override
            public TipoTransaccion getTipoTransaccion() {
                return TipoTransaccion.RETIRO;
            }
            
            @Override
            public double getMonto() {
                return 100.0;
            }
            
            @Override
            public String getDescripcion() {
                return "Cena en restaurante";
            }
            
            @Override
            public Categoria getCategoria() {
                return new Categoria("Restaurantes", "");
            }
        });
        
        // Transacción de Supermercado
        transacciones.add(new TransaccionFactory() {
            @Override
            public String getIdTransaccion() {
                return "T002";
            }
            
            @Override
            public LocalDate getFechaTransaccion() {
                return LocalDate.now();
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
                return "Compra en supermercado";
            }
            
            @Override
            public Categoria getCategoria() {
                return new Categoria("Supermercado", "");
            }
        });
    }
    
    @Test
    public void testCategoriaSimple() {
        // Verificar propiedades de la categoría simple
        assertNotNull(categoriaAlimentacion.getId());
        assertEquals("Alimentación", categoriaAlimentacion.getNombre());
        assertEquals("Gastos en comida", categoriaAlimentacion.getDescripcion());
        assertTrue(categoriaAlimentacion.esHoja());
        assertTrue(categoriaAlimentacion.getComponentes().isEmpty());
        
        // Cambiar propiedades
        categoriaAlimentacion.setNombre("Comida");
        categoriaAlimentacion.setDescripcion("Gastos en alimentación");
        assertEquals("Comida", categoriaAlimentacion.getNombre());
        assertEquals("Gastos en alimentación", categoriaAlimentacion.getDescripcion());
        
        // Verificar que no se pueden agregar componentes a una hoja
        CategoriaSimple otraCategoria = new CategoriaSimple("Otra", "");
        assertFalse(categoriaAlimentacion.agregarComponente(otraCategoria));
        assertTrue(categoriaAlimentacion.getComponentes().isEmpty());
        
        // Verificar que no se pueden eliminar componentes de una hoja
        assertFalse(categoriaAlimentacion.eliminarComponente("cualquier-id"));
        
        // Verificar que no se pueden obtener componentes de una hoja
        assertNull(categoriaAlimentacion.obtenerComponente("cualquier-id"));
    }
    
    @Test
    public void testGrupoCategoria() {
        // Verificar propiedades del grupo
        assertNotNull(grupoGastos.getId());
        assertEquals("Gastos", grupoGastos.getNombre());
        assertEquals("Categorías de gastos", grupoGastos.getDescripcion());
        assertFalse(grupoGastos.esHoja());
        assertEquals(1, grupoGastos.getComponentes().size());
        
        // Cambiar propiedades
        grupoGastos.setNombre("Egresos");
        grupoGastos.setDescripcion("Categorías de egresos");
        assertEquals("Egresos", grupoGastos.getNombre());
        assertEquals("Categorías de egresos", grupoGastos.getDescripcion());
        
        // Verificar que se pueden agregar componentes a un grupo
        CategoriaSimple nuevaCategoria = new CategoriaSimple("Nueva", "");
        assertTrue(grupoGastos.agregarComponente(nuevaCategoria));
        assertEquals(2, grupoGastos.getComponentes().size());
        
        // Verificar que se pueden eliminar componentes de un grupo
        assertTrue(grupoGastos.eliminarComponente(nuevaCategoria.getId()));
        assertEquals(1, grupoGastos.getComponentes().size());
        
        // Verificar que se pueden obtener componentes de un grupo
        assertSame(grupoAlimentacion, grupoGastos.obtenerComponente(grupoAlimentacion.getId()));
    }
    
    @Test
    public void testJerarquiaCategoria() {
        // Verificar la jerarquía de categorías
        assertEquals(1, grupoGastos.getComponentes().size());
        assertEquals(2, grupoAlimentacion.getComponentes().size());
        
        // Verificar que se puede navegar por la jerarquía
        ComponenteCategoria alimentacion = grupoGastos.obtenerComponente(grupoAlimentacion.getId());
        assertNotNull(alimentacion);
        assertEquals(2, alimentacion.getComponentes().size());
        
        // Verificar que se pueden obtener todas las hojas
        List<ComponenteCategoria> hojas = ((GrupoCategoria) alimentacion).obtenerTodasLasHojas();
        assertEquals(2, hojas.size());
        assertTrue(hojas.contains(categoriaRestaurantes));
        assertTrue(hojas.contains(categoriaSupermercado));
    }
    
    @Test
    public void testCalcularTotal() {
        // Verificar el cálculo de totales en categorías simples
        assertEquals(100.0, categoriaRestaurantes.calcularTotal(transacciones));
        assertEquals(200.0, categoriaSupermercado.calcularTotal(transacciones));
        
        // Verificar el cálculo de totales en grupos
        assertEquals(300.0, grupoAlimentacion.calcularTotal(transacciones));
        assertEquals(300.0, grupoGastos.calcularTotal(transacciones));
    }
    
    @Test
    public void testContarTransacciones() {
        // Verificar el conteo de transacciones en categorías simples
        assertEquals(1, categoriaRestaurantes.contarTransacciones(transacciones));
        assertEquals(1, categoriaSupermercado.contarTransacciones(transacciones));
        
        // Verificar el conteo de transacciones en grupos
        assertEquals(2, grupoAlimentacion.contarTransacciones(transacciones));
        assertEquals(2, grupoGastos.contarTransacciones(transacciones));
    }
    
    @Test
    public void testRutaCompleta() {
        // Verificar la ruta completa de las categorías
        assertEquals("Restaurantes", categoriaRestaurantes.getRutaCompleta());
        
        // Establecer padres para probar la ruta completa
        categoriaRestaurantes.setPadre(grupoAlimentacion);
        grupoAlimentacion.setPadre(grupoGastos);
        
        assertEquals("Gastos > Alimentación > Restaurantes", categoriaRestaurantes.getRutaCompleta());
    }
    
    @Test
    public void testPrevenirCiclos() {
        // Verificar que no se puede agregar un grupo a sí mismo
        assertFalse(grupoGastos.agregarComponente(grupoGastos));
        
        // Verificar que no se puede agregar un ancestro como hijo
        assertFalse(grupoAlimentacion.agregarComponente(grupoGastos));
    }
}
