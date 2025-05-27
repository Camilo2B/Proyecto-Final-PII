package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Categoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TipoTransaccion;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.TransaccionFactory;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación concreta del ReporteBuilder
 * Construye un reporte con los parámetros especificados
 */
public class ReporteConcreteBuilder implements ReporteBuilder {
    private final Reporte reporte;
    
    /**
     * Constructor que inicializa un nuevo reporte
     */
    public ReporteConcreteBuilder() {
        this.reporte = new Reporte();
    }
    
    @Override
    public ReporteBuilder setTitulo(String titulo) {
        reporte.setTitulo(titulo);
        return this;
    }
    
    @Override
    public ReporteBuilder setUsuario(Usuario usuario) {
        reporte.setUsuario(usuario);
        return this;
    }
    
    @Override
    public ReporteBuilder setFechaInicio(LocalDate fechaInicio) {
        reporte.setFechaInicio(fechaInicio);
        return this;
    }
    
    @Override
    public ReporteBuilder setFechaFin(LocalDate fechaFin) {
        reporte.setFechaFin(fechaFin);
        return this;
    }
    
    @Override
    public ReporteBuilder setTransacciones(List<TransaccionFactory> transacciones) {
        reporte.setTransacciones(transacciones);
        return this;
    }
    
    @Override
    public ReporteBuilder setCategorias(List<Categoria> categorias) {
        reporte.setCategorias(categorias);
        return this;
    }
    
    @Override
    public ReporteBuilder incluirResumenTransacciones(boolean incluir) {
        reporte.setIncluirResumenTransacciones(incluir);
        return this;
    }
    
    @Override
    public ReporteBuilder incluirAnalisisPorCategoria(boolean incluir) {
        reporte.setIncluirAnalisisPorCategoria(incluir);
        return this;
    }
    
    @Override
    public ReporteBuilder incluirGraficos(boolean incluir) {
        reporte.setIncluirGraficos(incluir);
        return this;
    }
    
    @Override
    public ReporteBuilder setFormato(FormatoReporte formato) {
        reporte.setFormato(formato);
        return this;
    }
    
    @Override
    public Reporte build() {
        // Generar el contenido del reporte
        StringBuilder contenido = new StringBuilder();
        
        // Agregar encabezado
        contenido.append("=== ").append(reporte.getTitulo()).append(" ===\n\n");
        
        // Agregar información del usuario
        if (reporte.getUsuario() != null) {
            contenido.append("Usuario: ").append(reporte.getUsuario().getNombre()).append("\n");
        }
        
        // Agregar período del reporte
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (reporte.getFechaInicio() != null && reporte.getFechaFin() != null) {
            contenido.append("Período: Del ").append(reporte.getFechaInicio().format(formatter))
                    .append(" al ").append(reporte.getFechaFin().format(formatter)).append("\n\n");
        }
        
        // Agregar resumen de transacciones si está habilitado
        if (reporte.isIncluirResumenTransacciones() && reporte.getTransacciones() != null) {
            contenido.append("=== RESUMEN DE TRANSACCIONES ===\n\n");
            
            // Calcular totales por tipo de transacción
            double totalDepositos = reporte.getTransacciones().stream()
                    .filter(t -> t.getTipoTransaccion() == TipoTransaccion.DEPOSITO)
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();
            
            double totalRetiros = reporte.getTransacciones().stream()
                    .filter(t -> t.getTipoTransaccion() == TipoTransaccion.RETIRO)
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();
            
            double totalTransferencias = reporte.getTransacciones().stream()
                    .filter(t -> t.getTipoTransaccion() == TipoTransaccion.TRANSFERENCIA)
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();
            
            contenido.append("Total de transacciones: ").append(reporte.getTransacciones().size()).append("\n");
            contenido.append("Total de depósitos: $").append(String.format("%.2f", totalDepositos)).append("\n");
            contenido.append("Total de retiros: $").append(String.format("%.2f", totalRetiros)).append("\n");
            contenido.append("Total de transferencias: $").append(String.format("%.2f", totalTransferencias)).append("\n\n");
            
            // Listar transacciones
            contenido.append("Listado de transacciones:\n");
            contenido.append("Fecha | Tipo | Monto | Descripción\n");
            contenido.append("----------------------------------\n");
            
            for (TransaccionFactory transaccion : reporte.getTransacciones()) {
                contenido.append(transaccion.getFechaTransaccion().format(formatter))
                        .append(" | ")
                        .append(transaccion.getTipoTransaccion())
                        .append(" | $")
                        .append(String.format("%.2f", transaccion.getMonto()))
                        .append(" | ")
                        .append(transaccion.getDescripcion())
                        .append("\n");
            }
            
            contenido.append("\n");
        }
        
        // Agregar análisis por categoría si está habilitado
        if (reporte.isIncluirAnalisisPorCategoria() && reporte.getTransacciones() != null) {
            contenido.append("=== ANÁLISIS POR CATEGORÍA ===\n\n");
            
            // Agrupar transacciones por categoría
            Map<String, List<TransaccionFactory>> transaccionesPorCategoria = new HashMap<>();
            
            for (TransaccionFactory transaccion : reporte.getTransacciones()) {
                String nombreCategoria = transaccion.getCategoria() != null ? 
                        transaccion.getCategoria().getNombre() : "Sin categoría";
                
                if (!transaccionesPorCategoria.containsKey(nombreCategoria)) {
                    transaccionesPorCategoria.put(nombreCategoria, new ArrayList<>());
                }
                
                transaccionesPorCategoria.get(nombreCategoria).add(transaccion);
            }
            
            // Mostrar totales por categoría
            for (Map.Entry<String, List<TransaccionFactory>> entry : transaccionesPorCategoria.entrySet()) {
                String categoria = entry.getKey();
                List<TransaccionFactory> transacciones = entry.getValue();
                
                double totalCategoria = transacciones.stream()
                        .mapToDouble(TransaccionFactory::getMonto)
                        .sum();
                
                contenido.append("Categoría: ").append(categoria).append("\n");
                contenido.append("Total de transacciones: ").append(transacciones.size()).append("\n");
                contenido.append("Monto total: $").append(String.format("%.2f", totalCategoria)).append("\n\n");
            }
        }
        
        // Agregar información sobre gráficos si están habilitados
        if (reporte.isIncluirGraficos()) {
            contenido.append("=== GRÁFICOS ===\n\n");
            contenido.append("Los gráficos están disponibles en la aplicación.\n\n");
        }
        
        // Agregar pie de página
        contenido.append("Reporte generado el ").append(LocalDate.now().format(formatter)).append("\n");
        contenido.append("Formato: ").append(reporte.getFormato().getDescripcion()).append("\n");
        
        // Establecer el contenido generado
        reporte.setContenido(contenido.toString());
        
        // Generar la ruta del archivo
        String rutaArchivo = "reportes/" + reporte.generarNombreArchivo();
        reporte.setRutaArchivo(rutaArchivo);
        
        return reporte;
    }
}
