package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Clase que gestiona la generación de reportes financieros
 */
public class ReporteService {
    
    private DataManager dataManager;
    private AuthenticationService authService;
    
    /**
     * Constructor de la clase ReporteService
     */
    public ReporteService() {
        this.dataManager = DataManager.getInstance();
        this.authService = AuthenticationService.getInstance();
    }
    
    /**
     * Genera un reporte de transacciones en formato CSV
     * @param rutaArchivo Ruta donde se guardará el archivo
     * @param fechaInicio Fecha de inicio del reporte
     * @param fechaFin Fecha de fin del reporte
     * @return true si el reporte se generó correctamente, false en caso contrario
     */
    public boolean generarReporteTransaccionesCSV(String rutaArchivo, LocalDate fechaInicio, LocalDate fechaFin) {
        if (!authService.hayUsuarioAutenticado() && !authService.hayAdminAutenticado()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezados
            writer.append("ID,Fecha,Tipo,Monto,Descripción,Categoría\n");
            
            // Filtrar transacciones por fecha
            LinkedList<TransaccionFactory> transacciones = dataManager.getTransacciones().stream()
                    .filter(t -> !t.getFechaTransaccion().isBefore(fechaInicio) && !t.getFechaTransaccion().isAfter(fechaFin))
                    .collect(Collectors.toCollection(LinkedList::new));
            
            // Si es un usuario, filtrar solo sus transacciones
            if (authService.hayUsuarioAutenticado()) {
                // En una implementación real, se filtrarían las transacciones por usuario
                // Por simplicidad, se omite en esta implementación
            }
            
            // Escribir datos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TransaccionFactory transaccion : transacciones) {
                writer.append(transaccion.getIdTransaccion()).append(",")
                      .append(transaccion.getFechaTransaccion().format(formatter)).append(",")
                      .append(transaccion.getTipoTransaccion().toString()).append(",")
                      .append(String.valueOf(transaccion.getMonto())).append(",")
                      .append(transaccion.getDescripcion()).append(",")
                      .append(transaccion.getCategoria().getNombre()).append("\n");
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Genera un reporte de presupuestos en formato CSV
     * @param rutaArchivo Ruta donde se guardará el archivo
     * @return true si el reporte se generó correctamente, false en caso contrario
     */
    public boolean generarReportePresupuestosCSV(String rutaArchivo) {
        if (!authService.hayUsuarioAutenticado() && !authService.hayAdminAutenticado()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezados
            writer.append("ID,Nombre,Monto Total,Monto Gastado,Categoría,% Utilizado,Estado\n");
            
            LinkedList<Presupuesto> presupuestos;
            if (authService.hayUsuarioAutenticado()) {
                presupuestos = authService.getUsuarioAutenticado().getPresupuestos();
            } else {
                presupuestos = dataManager.getPresupuestos();
            }
            
            // Escribir datos
            for (Presupuesto presupuesto : presupuestos) {
                double porcentajeUso = presupuesto.getPorcentajeUso();
                String estado = presupuesto.isExcedido() ? "Excedido" : "En límite";
                
                writer.append(presupuesto.getIdPresupuesto()).append(",")
                      .append(presupuesto.getNombre()).append(",")
                      .append(String.valueOf(presupuesto.getMontoTotal())).append(",")
                      .append(String.valueOf(presupuesto.getMontoGastado())).append(",")
                      .append(presupuesto.getCategoriaEspecifica()).append(",")
                      .append(String.format("%.2f", porcentajeUso)).append(",")
                      .append(estado).append("\n");
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Genera un reporte de estadísticas en formato CSV (solo para administradores)
     * @param rutaArchivo Ruta donde se guardará el archivo
     * @return true si el reporte se generó correctamente, false en caso contrario
     */
    public boolean generarReporteEstadisticasCSV(String rutaArchivo) {
        if (!authService.hayAdminAutenticado()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Obtener estadísticas
            Map<String, Object> estadisticas = authService.getAdminAutenticado().obtenerEstadisticas();
            
            // Escribir encabezados y datos
            writer.append("Métrica,Valor\n");
            
            // Saldo promedio
            double saldoPromedio = (double) estadisticas.get("saldoPromedio");
            writer.append("Saldo promedio de usuarios,").append(String.format("%.2f", saldoPromedio)).append("\n");
            
            // Categorías más comunes
            @SuppressWarnings("unchecked")
            Map<String, Long> categoriasMasComunes = (Map<String, Long>) estadisticas.get("categoriasMasComunes");
            writer.append("\nCategoría,Cantidad de transacciones\n");
            
            for (Map.Entry<String, Long> entry : categoriasMasComunes.entrySet()) {
                writer.append(entry.getKey()).append(",").append(String.valueOf(entry.getValue())).append("\n");
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Genera un informe de resumen para el usuario actual
     * @return Mapa con el resumen de la información financiera del usuario
     */
    public Map<String, Object> generarResumenUsuario() {
        if (!authService.hayUsuarioAutenticado()) {
            return null;
        }
        
        Usuario usuario = authService.getUsuarioAutenticado();
        Map<String, Object> resumen = new HashMap<>();
        
        // Información básica
        resumen.put("nombre", usuario.getNombre());
        resumen.put("saldoTotal", usuario.getSaldoTotal());
        resumen.put("cantidadCuentas", usuario.getCuentasAsociadas().size());
        resumen.put("cantidadPresupuestos", usuario.getPresupuestos().size());
        
        // Presupuestos excedidos
        long presupuestosExcedidos = usuario.getPresupuestos().stream()
                .filter(Presupuesto::isExcedido)
                .count();
        resumen.put("presupuestosExcedidos", presupuestosExcedidos);
        
        // En una implementación real, se agregarían más estadísticas
        
        return resumen;
    }
}
