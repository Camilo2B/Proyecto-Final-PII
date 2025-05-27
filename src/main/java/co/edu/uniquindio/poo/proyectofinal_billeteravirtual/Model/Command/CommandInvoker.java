package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Invoker para el patrón Command
 * Se encarga de ejecutar los comandos y mantener un historial para deshacer/rehacer
 */
public class CommandInvoker {
    private static CommandInvoker instancia;
    private final Stack<Command> comandosEjecutados;
    private final Stack<Command> comandosDeshecho;
    private final List<CommandListener> listeners;
    
    /**
     * Constructor privado para el patrón Singleton
     */
    private CommandInvoker() {
        this.comandosEjecutados = new Stack<>();
        this.comandosDeshecho = new Stack<>();
        this.listeners = new ArrayList<>();
    }
    
    /**
     * Obtiene la instancia única del invoker (patrón Singleton)
     * @return Instancia del invoker
     */
    public static synchronized CommandInvoker getInstance() {
        if (instancia == null) {
            instancia = new CommandInvoker();
        }
        return instancia;
    }
    
    /**
     * Ejecuta un comando
     * @param comando Comando a ejecutar
     * @return true si se ejecutó correctamente, false en caso contrario
     */
    public boolean ejecutarComando(Command comando) {
        boolean resultado = comando.ejecutar();
        
        if (resultado) {
            comandosEjecutados.push(comando);
            comandosDeshecho.clear(); // Al ejecutar un nuevo comando, se pierde la historia de rehacer
            notificarCambio();
        }
        
        return resultado;
    }
    
    /**
     * Deshace el último comando ejecutado
     * @return true si se deshizo correctamente, false en caso contrario
     */
    public boolean deshacer() {
        if (comandosEjecutados.isEmpty()) {
            return false;
        }
        
        Command comando = comandosEjecutados.pop();
        boolean resultado = comando.deshacer();
        
        if (resultado) {
            comandosDeshecho.push(comando);
            notificarCambio();
        } else {
            // Si no se pudo deshacer, volver a agregarlo a la pila de ejecutados
            comandosEjecutados.push(comando);
        }
        
        return resultado;
    }
    
    /**
     * Rehace el último comando deshecho
     * @return true si se rehizo correctamente, false en caso contrario
     */
    public boolean rehacer() {
        if (comandosDeshecho.isEmpty()) {
            return false;
        }
        
        Command comando = comandosDeshecho.pop();
        boolean resultado = comando.ejecutar();
        
        if (resultado) {
            comandosEjecutados.push(comando);
            notificarCambio();
        } else {
            // Si no se pudo rehacer, volver a agregarlo a la pila de deshechos
            comandosDeshecho.push(comando);
        }
        
        return resultado;
    }
    
    /**
     * Verifica si hay comandos para deshacer
     * @return true si hay comandos para deshacer, false en caso contrario
     */
    public boolean puedeDeshacer() {
        return !comandosEjecutados.isEmpty();
    }
    
    /**
     * Verifica si hay comandos para rehacer
     * @return true si hay comandos para rehacer, false en caso contrario
     */
    public boolean puedeRehacer() {
        return !comandosDeshecho.isEmpty();
    }
    
    /**
     * Obtiene el historial de comandos ejecutados
     * @return Lista de comandos ejecutados
     */
    public List<Command> getHistorialComandos() {
        return new ArrayList<>(comandosEjecutados);
    }
    
    /**
     * Agrega un listener para cambios en el historial de comandos
     * @param listener Listener a agregar
     */
    public void agregarListener(CommandListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Elimina un listener
     * @param listener Listener a eliminar
     */
    public void eliminarListener(CommandListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notifica a los listeners que ha habido un cambio en el historial
     */
    private void notificarCambio() {
        for (CommandListener listener : listeners) {
            listener.onCommandHistoryChanged();
        }
    }
    
    /**
     * Interfaz para escuchar cambios en el historial de comandos
     */
    public interface CommandListener {
        /**
         * Se llama cuando cambia el historial de comandos
         */
        void onCommandHistoryChanged();
    }
}
