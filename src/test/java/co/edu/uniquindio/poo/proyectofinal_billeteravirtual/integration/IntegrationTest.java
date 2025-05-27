package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.integration;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.FormatoReporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.Reporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command.Command;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.ComponenteCategoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Presupuesto;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoCuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Pruebas de integración para verificar la interacción entre los diferentes componentes del sistema.
 * Estas pruebas simulan flujos completos de usuario para garantizar que todos los componentes
 * funcionan correctamente en conjunto.
 */
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTest {
    
    private BilleteraService billeteraService;
    private static final String USER_ID = "U001";
    private static final String USER_PASSWORD = "password123";
    private static final String CUENTA_ID = "123456789";
    
    @BeforeEach
    public void setUp() {
        // Inicializar el servicio
        billeteraService = new BilleteraService();
        
        // Limpiar datos existentes para asegurar un entorno de prueba limpio
        DataManager.getInstance().limpiarDatos();
        
        // Crear un usuario de prueba si no existe
        if (DataManager.getInstance().buscarUsuario(USER_ID) == null) {
            billeteraService.registrarUsuario(
                "Usuario Prueba", 
                USER_ID, 
                "usuario@test.com", 
                "1234567890", 
                "Dirección de prueba", 
                USER_PASSWORD
            );
        }
    }
    
    /**
     * Prueba el flujo completo de registro, inicio de sesión, creación de cuenta,
     * realización de transacciones y generación de reportes.
     */
    @Test
    @Order(1)
    public void testFlujoCompletoUsuario() {
        // 1. Iniciar sesión
        assertTrue(billeteraService.iniciarSesionUsuario(USER_ID, USER_PASSWORD));
        assertTrue(billeteraService.hayUsuarioLogueado());
        
        // 2. Crear una cuenta
        Usuario usuario = billeteraService.getUsuarioActual();
        Cuenta cuenta = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, CUENTA_ID, 0.0);
        usuario.agregarCuenta(cuenta);
        
        // 3. Crear categorías
        billeteraService.crearCategoria("Alimentación", "Gastos en comida");
        billeteraService.crearCategoria("Transporte", "Gastos en transporte");
        
        // Verificar que se crearon las categorías
        LinkedList<Categoria> categorias = billeteraService.obtenerCategorias();
        assertFalse(categorias.isEmpty());
        
        Categoria categoriaAlimentacion = categorias.stream()
                .filter(c -> c.getNombre().equals("Alimentación"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(categoriaAlimentacion);
        
        // 4. Realizar un depósito
        assertTrue(billeteraService.realizarDeposito(1000.0, CUENTA_ID));
        assertEquals(1000.0, usuario.getSaldoTotal());
        assertEquals(1000.0, cuenta.getSaldo());
        
        // 5. Realizar un retiro con categoría
        assertTrue(billeteraService.realizarRetiro(300.0, CUENTA_ID, "Compra en supermercado", categoriaAlimentacion));
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
        
        // 6. Verificar que se registraron las transacciones
        LinkedList<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        assertEquals(2, transacciones.size());
        
        // 7. Crear un presupuesto
        assertTrue(billeteraService.crearPresupuesto("Presupuesto Alimentación", 500.0, "Alimentación"));
        
        // Verificar que se creó el presupuesto
        LinkedList<Presupuesto> presupuestos = billeteraService.obtenerPresupuestosUsuario();
        assertEquals(1, presupuestos.size());
        
        // 8. Deshacer la última operación (retiro)
        assertTrue(billeteraService.deshacerOperacion());
        assertEquals(1000.0, usuario.getSaldoTotal());
        assertEquals(1000.0, cuenta.getSaldo());
        
        // 9. Rehacer la operación
        assertTrue(billeteraService.rehacerOperacion());
        assertEquals(700.0, usuario.getSaldoTotal());
        assertEquals(700.0, cuenta.getSaldo());
        
        // 10. Generar un reporte
        Reporte reporte = billeteraService.generarReporte(
            "completo", 
            LocalDate.now().minusMonths(1), 
            LocalDate.now(), 
            FormatoReporte.PDF
        );
        
        assertNotNull(reporte);
        assertEquals("Reporte Financiero Completo", reporte.getTitulo());
        assertEquals(usuario, reporte.getUsuario());
        assertEquals(FormatoReporte.PDF, reporte.getFormato());
        
        // 11. Cerrar sesión
        billeteraService.cerrarSesion();
        assertFalse(billeteraService.hayUsuarioLogueado());
    }
    
    /**
     * Prueba la integración del patrón Composite para categorías jerárquicas.
     */
    @Test
    @Order(2)
    public void testIntegracionCategorias() {
        // 1. Iniciar sesión
        assertTrue(billeteraService.iniciarSesionUsuario(USER_ID, USER_PASSWORD));
        
        // 2. Crear estructura jerárquica de categorías
        assertTrue(billeteraService.agregarGrupoCategoria("Gastos", "Categorías de gastos", null));
        
        List<ComponenteCategoria> categorias = billeteraService.obtenerCategoriasJerarquicas();
        assertFalse(categorias.isEmpty());
        
        ComponenteCategoria gastos = categorias.stream()
                .filter(c -> c.getNombre().equals("Gastos"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(gastos);
        String idGastos = gastos.getId();
        
        // Agregar subcategorías
        assertTrue(billeteraService.agregarCategoriaSimple("Comida", "Gastos en comida", idGastos));
        assertTrue(billeteraService.agregarCategoriaSimple("Transporte", "Gastos en transporte", idGastos));
        
        // Verificar la estructura
        categorias = billeteraService.obtenerCategoriasJerarquicas();
        assertEquals(3, categorias.size()); // Gastos, Comida, Transporte
        
        // 3. Realizar transacciones con las nuevas categorías
        Usuario usuario = billeteraService.getUsuarioActual();
        Cuenta cuenta = usuario.buscarCuenta(CUENTA_ID);
        
        // Buscar la categoría Comida
        Categoria categoriaComida = billeteraService.obtenerCategorias().stream()
                .filter(c -> c.getNombre().equals("Comida"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(categoriaComida);
        
        // Realizar un retiro con la categoría Comida
        assertTrue(billeteraService.realizarRetiro(100.0, CUENTA_ID, "Restaurante", categoriaComida));
        
        // 4. Verificar transacciones por tipo
        LinkedList<TransaccionFactory> retiros = billeteraService.obtenerTransaccionesPorTipo(TipoTransaccion.RETIRO);
        assertFalse(retiros.isEmpty());
        
        // 5. Cerrar sesión
        billeteraService.cerrarSesion();
    }
    
    /**
     * Prueba la integración del patrón Command para operaciones financieras.
     */
    @Test
    @Order(3)
    public void testIntegracionCommand() {
        // 1. Iniciar sesión
        assertTrue(billeteraService.iniciarSesionUsuario(USER_ID, USER_PASSWORD));
        
        // 2. Realizar varias operaciones
        Usuario usuario = billeteraService.getUsuarioActual();
        Cuenta cuenta = usuario.buscarCuenta(CUENTA_ID);
        
        double saldoInicial = cuenta.getSaldo();
        
        // Realizar depósito
        assertTrue(billeteraService.realizarDeposito(500.0, CUENTA_ID, "Depósito de prueba", null));
        
        // Realizar retiro
        assertTrue(billeteraService.realizarRetiro(200.0, CUENTA_ID, "Retiro de prueba", null));
        
        // 3. Verificar historial de comandos
        List<Command> historial = billeteraService.obtenerHistorialOperaciones();
        assertFalse(historial.isEmpty());
        
        // 4. Deshacer todas las operaciones
        while (billeteraService.puedeDeshacer()) {
            assertTrue(billeteraService.deshacerOperacion());
        }
        
        // Verificar que el saldo volvió al valor inicial
        assertEquals(saldoInicial, cuenta.getSaldo());
        
        // 5. Rehacer todas las operaciones
        while (billeteraService.puedeRehacer()) {
            assertTrue(billeteraService.rehacerOperacion());
        }
        
        // Verificar que el saldo se actualizó correctamente
        assertEquals(saldoInicial + 500.0 - 200.0, cuenta.getSaldo());
        
        // 6. Cerrar sesión
        billeteraService.cerrarSesion();
    }
    
    /**
     * Prueba la integración del patrón Builder para reportes.
     */
    @Test
    @Order(4)
    public void testIntegracionReportes() {
        // 1. Iniciar sesión
        assertTrue(billeteraService.iniciarSesionUsuario(USER_ID, USER_PASSWORD));
        
        // 2. Generar diferentes tipos de reportes
        
        // Reporte completo
        Reporte reporteCompleto = billeteraService.generarReporte(
            "completo", 
            LocalDate.now().minusMonths(1), 
            LocalDate.now(), 
            FormatoReporte.PDF
        );
        
        assertNotNull(reporteCompleto);
        assertEquals("Reporte Financiero Completo", reporteCompleto.getTitulo());
        
        // Reporte resumido
        Reporte reporteResumido = billeteraService.generarReporte(
            "resumido", 
            LocalDate.now().minusMonths(1), 
            LocalDate.now(), 
            FormatoReporte.CSV
        );
        
        assertNotNull(reporteResumido);
        assertEquals("Resumen Financiero", reporteResumido.getTitulo());
        assertEquals(FormatoReporte.CSV, reporteResumido.getFormato());
        
        // Reporte por categorías
        Reporte reporteCategorias = billeteraService.generarReporte(
            "categorias", 
            LocalDate.now().minusMonths(1), 
            LocalDate.now(), 
            FormatoReporte.HTML
        );
        
        assertNotNull(reporteCategorias);
        assertEquals("Análisis por Categorías", reporteCategorias.getTitulo());
        assertEquals(FormatoReporte.HTML, reporteCategorias.getFormato());
        
        // 3. Cerrar sesión
        billeteraService.cerrarSesion();
    }
}
