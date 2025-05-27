package co.edu.uniquindio.poo.proyectofinal_billeteravirtual;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Suite de pruebas que ejecuta todas las pruebas del proyecto
 */
@Suite
@SelectPackages({
    "co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model",
    "co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder",
    "co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command",
    "co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite",
    "co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController"
})
public class AllTests {
    // Esta clase no necesita contenido, solo las anotaciones
}
