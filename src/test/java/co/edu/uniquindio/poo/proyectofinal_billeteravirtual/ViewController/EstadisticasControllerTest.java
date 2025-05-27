package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.BilleteraService;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Cuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoCuenta;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Pruebas unitarias para el controlador de Estadísticas
 * Nota: Estas pruebas se centran en la lógica del controlador, no en la interfaz gráfica
 */
public class EstadisticasControllerTest {
    
    private BilleteraService billeteraService;
    private Usuario usuario;
    private Cuenta cuenta;
    private Categoria categoriaAlimentacion;
    private Categoria categoriaTransporte;
    private Categoria categoriaEntretenimiento;
    
    @BeforeEach
    public void setUp() {
        // Crear servicio
        billeteraService = new BilleteraService();
        
        // Crear categorías
        categoriaAlimentacion = new Categoria("Alimentación", "Gastos en comida");
        categoriaTransporte = new Categoria("Transporte", "Gastos en transporte");
        categoriaEntretenimiento = new Categoria("Entretenimiento", "Gastos en entretenimiento");
        
        // Agregar categorías al DataManager
        DataManager.getInstance().agregarCategoria(categoriaAlimentacion);
        DataManager.getInstance().agregarCategoria(categoriaTransporte);
        DataManager.getInstance().agregarCategoria(categoriaEntretenimiento);
        
        // Crear usuario de prueba
        usuario = new Usuario("Usuario Prueba", "U001", "usuario@test.com", "1234567890", "Dirección de prueba", 0.0, null, "password");
        
        // Crear cuenta de prueba
        cuenta = new Cuenta("C001", "Banco de Prueba", TipoCuenta.AHORRO, "123456789", 0.0);
        
        // Agregar cuenta al usuario
        usuario.agregarCuenta(cuenta);
        
        // Agregar usuario al DataManager
        DataManager.getInstance().agregarUsuario(usuario);
        
        // Iniciar sesión
        billeteraService.iniciarSesionUsuario("U001", "password");
        
        // Crear transacciones de prueba
        crearTransaccionesPrueba();
    }
    
    private void crearTransaccionesPrueba() {
        // Depósito inicial
        billeteraService.realizarDeposito(5000.0, "123456789", "Depósito inicial", null);
        
        // Gastos en Alimentación
        billeteraService.realizarRetiro(300.0, "123456789", "Supermercado", categoriaAlimentacion);
        billeteraService.realizarRetiro(150.0, "123456789", "Restaurante", categoriaAlimentacion);
        billeteraService.realizarRetiro(80.0, "123456789", "Café", categoriaAlimentacion);
        
        // Gastos en Transporte
        billeteraService.realizarRetiro(200.0, "123456789", "Gasolina", categoriaTransporte);
        billeteraService.realizarRetiro(50.0, "123456789", "Taxi", categoriaTransporte);
        
        // Gastos en Entretenimiento
        billeteraService.realizarRetiro(120.0, "123456789", "Cine", categoriaEntretenimiento);
        billeteraService.realizarRetiro(300.0, "123456789", "Concierto", categoriaEntretenimiento);
    }
    
    /**
     * Método auxiliar para simular la lógica del controlador de estadísticas
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos para el gráfico de barras
     */
    private Map<String, Double> obtenerDatosGraficoBarras(List<TransaccionFactory> transacciones) {
        Map<String, Double> datos = new HashMap<>();
        
        // Agrupar transacciones por categoría
        for (TransaccionFactory transaccion : transacciones) {
            if (transaccion.getTipoTransaccion() == TipoTransaccion.RETIRO) {
                String nombreCategoria = transaccion.getCategoria() != null ? 
                        transaccion.getCategoria().getNombre() : "Sin categoría";
                
                datos.put(nombreCategoria, datos.getOrDefault(nombreCategoria, 0.0) + transaccion.getMonto());
            }
        }
        
        return datos;
    }
    
    /**
     * Método auxiliar para simular la lógica del controlador de estadísticas
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos para el gráfico circular
     */
    private Map<String, Double> obtenerDatosGraficoCircular(List<TransaccionFactory> transacciones) {
        Map<String, Double> datos = new HashMap<>();
        double totalGastos = 0.0;
        
        // Calcular total de gastos
        for (TransaccionFactory transaccion : transacciones) {
            if (transaccion.getTipoTransaccion() == TipoTransaccion.RETIRO) {
                totalGastos += transaccion.getMonto();
            }
        }
        
        // Calcular porcentaje por categoría
        for (TransaccionFactory transaccion : transacciones) {
            if (transaccion.getTipoTransaccion() == TipoTransaccion.RETIRO) {
                String nombreCategoria = transaccion.getCategoria() != null ? 
                        transaccion.getCategoria().getNombre() : "Sin categoría";
                
                double montoCategoria = datos.getOrDefault(nombreCategoria, 0.0) + transaccion.getMonto();
                double porcentaje = (montoCategoria / totalGastos) * 100.0;
                datos.put(nombreCategoria, porcentaje);
            }
        }
        
        return datos;
    }
    
    /**
     * Método auxiliar para simular la lógica del controlador de estadísticas
     * @param transacciones Lista de transacciones
     * @return Mapa con los datos para el gráfico de línea
     */
    private Map<LocalDate, Map<String, Double>> obtenerDatosGraficoLinea(List<TransaccionFactory> transacciones) {
        Map<LocalDate, Map<String, Double>> datos = new HashMap<>();
        
        // Agrupar transacciones por fecha
        for (TransaccionFactory transaccion : transacciones) {
            LocalDate fecha = transaccion.getFechaTransaccion();
            
            if (!datos.containsKey(fecha)) {
                datos.put(fecha, new HashMap<>());
                datos.get(fecha).put("Ingresos", 0.0);
                datos.get(fecha).put("Gastos", 0.0);
            }
            
            if (transaccion.getTipoTransaccion() == TipoTransaccion.DEPOSITO) {
                double ingresos = datos.get(fecha).get("Ingresos") + transaccion.getMonto();
                datos.get(fecha).put("Ingresos", ingresos);
            } else if (transaccion.getTipoTransaccion() == TipoTransaccion.RETIRO) {
                double gastos = datos.get(fecha).get("Gastos") + transaccion.getMonto();
                datos.get(fecha).put("Gastos", gastos);
            }
        }
        
        return datos;
    }
    
    @Test
    public void testObtenerDatosGraficoBarras() {
        // Obtener transacciones
        List<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        
        // Obtener datos para el gráfico de barras
        Map<String, Double> datos = obtenerDatosGraficoBarras(transacciones);
        
        // Verificar que se obtuvieron los datos correctamente
        assertEquals(3, datos.size());
        assertEquals(530.0, datos.get("Alimentación"));
        assertEquals(250.0, datos.get("Transporte"));
        assertEquals(420.0, datos.get("Entretenimiento"));
    }
    
    @Test
    public void testObtenerDatosGraficoCircular() {
        // Obtener transacciones
        List<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        
        // Obtener datos para el gráfico circular
        Map<String, Double> datos = obtenerDatosGraficoCircular(transacciones);
        
        // Verificar que se obtuvieron los datos correctamente
        assertEquals(3, datos.size());
        
        // Calcular porcentajes esperados
        double totalGastos = 530.0 + 250.0 + 420.0;
        double porcentajeAlimentacion = (530.0 / totalGastos) * 100.0;
        double porcentajeTransporte = (250.0 / totalGastos) * 100.0;
        double porcentajeEntretenimiento = (420.0 / totalGastos) * 100.0;
        
        // Verificar porcentajes con un margen de error
        assertEquals(porcentajeAlimentacion, datos.get("Alimentación"), 0.01);
        assertEquals(porcentajeTransporte, datos.get("Transporte"), 0.01);
        assertEquals(porcentajeEntretenimiento, datos.get("Entretenimiento"), 0.01);
        
        // Verificar que la suma de porcentajes es 100%
        double sumaPorcentajes = datos.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(100.0, sumaPorcentajes, 0.01);
    }
    
    @Test
    public void testObtenerDatosGraficoLinea() {
        // Obtener transacciones
        List<TransaccionFactory> transacciones = billeteraService.obtenerTransaccionesUsuario();
        
        // Obtener datos para el gráfico de línea
        Map<LocalDate, Map<String, Double>> datos = obtenerDatosGraficoLinea(transacciones);
        
        // Verificar que se obtuvieron los datos correctamente
        assertFalse(datos.isEmpty());
        
        // Verificar que cada fecha tiene datos de ingresos y gastos
        for (Map<String, Double> datosFecha : datos.values()) {
            assertTrue(datosFecha.containsKey("Ingresos"));
            assertTrue(datosFecha.containsKey("Gastos"));
        }
        
        // Verificar que la suma de ingresos es 5000.0
        double totalIngresos = datos.values().stream()
                .mapToDouble(mapa -> mapa.get("Ingresos"))
                .sum();
        assertEquals(5000.0, totalIngresos);
        
        // Verificar que la suma de gastos es 1200.0
        double totalGastos = datos.values().stream()
                .mapToDouble(mapa -> mapa.get("Gastos"))
                .sum();
        assertEquals(1200.0, totalGastos);
    }
}
