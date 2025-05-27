package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

public class ConfiguracionBilletera {
    private static ConfiguracionBilletera instance;

    private ConfiguracionBilletera() {

    }

    public static ConfiguracionBilletera getInstance() {
        if (instance == null) {
            instance = new ConfiguracionBilletera();
        }
        return instance;
    }

    /**
     * Método para enviar una notificación con un mensaje por consola
     *
     * @param mensaje el mensaje que se desea notificar.
     */
    public void Configurar(String mensaje) {
        System.out.println("Configuracion" + mensaje);
    }
}