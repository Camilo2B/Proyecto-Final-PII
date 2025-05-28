import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.DataManager;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Administrador;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.AuthenticationService;

public class TestAdmin {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE AUTENTICACIÓN DE ADMINISTRADOR ===");
        
        try {
            // Inicializar DataManager
            DataManager dataManager = DataManager.getInstance();
            
            // Verificar administradores
            System.out.println("Total administradores: " + dataManager.getAdministradores().size());
            
            for (Administrador admin : dataManager.getAdministradores()) {
                System.out.println("Admin encontrado:");
                System.out.println("  ID: '" + admin.getIdGeneral() + "'");
                System.out.println("  Nombre: " + admin.getNombre());
                System.out.println("  Password: '" + admin.getPassword() + "'");
            }
            
            // Probar búsqueda
            System.out.println("\n=== PRUEBA DE BÚSQUEDA ===");
            Administrador adminEncontrado = dataManager.buscarAdministrador("9876543210");
            System.out.println("Admin encontrado por búsqueda: " + (adminEncontrado != null ? adminEncontrado.getNombre() : "null"));
            
            // Probar autenticación
            System.out.println("\n=== PRUEBA DE AUTENTICACIÓN ===");
            AuthenticationService authService = AuthenticationService.getInstance();
            boolean resultado = authService.autenticarAdmin("9876543210", "admin123");
            System.out.println("Resultado de autenticación: " + resultado);
            
        } catch (Exception e) {
            System.err.println("Error en la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
