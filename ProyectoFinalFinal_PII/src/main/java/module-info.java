module co.edu.uniquindio.poo.proyectofinal_billeteravirtual {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires transitive javafx.graphics;

    opens co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController to javafx.fxml;
    opens co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model to javafx.fxml;
    exports co.edu.uniquindio.poo.proyectofinal_billeteravirtual;
    exports co.edu.uniquindio.poo.proyectofinal_billeteravirtual.ViewController;
    exports co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;
}
