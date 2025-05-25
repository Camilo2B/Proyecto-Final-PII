package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Clase que representa a un administrador de la billetera virtual
 */
public class Administrador extends Persona {

    private LinkedList<Cuenta> cuentas;
    private String password;
    private DataManager dataManager;

    /**
     * Constructor sin parámetros para serialización
     */
    public Administrador() {
        super();
        this.cuentas = new LinkedList<>();
        this.password = "";
    }

    /**
     * Constructor básico
     */
    public Administrador(String nombre, String idGeneral, String email, String telefono, String direccion) {
        super(nombre, idGeneral, email, telefono, direccion);
        this.cuentas = new LinkedList<>();
        // No inicializar dataManager aquí para evitar recursión
    }

    /**
     * Constructor con cuentas
     */
    public Administrador(String nombre, String idGeneral, String email, String telefono, String direccion, LinkedList<Cuenta> cuentas) {
        super(nombre, idGeneral, email, telefono, direccion);
        this.cuentas = cuentas;
        // No inicializar dataManager aquí para evitar recursión
    }

    /**
     * Constructor completo
     */
    public Administrador(String nombre, String idGeneral, String email, String telefono, String direccion, LinkedList<Cuenta> cuentas, String password) {
        this(nombre, idGeneral, email, telefono, direccion, cuentas);
        this.password = password;
    }

    /**
     * Método para establecer el DataManager manualmente
     * Esto evita la recursión infinita durante la inicialización
     * @param dataManager Instancia de DataManager
     */
    public void setDataManagerManually(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(LinkedList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gestiona usuarios (crear, actualizar, eliminar, listar)
     */

    /**
     * Crea un nuevo usuario
     * @param nombre Nombre del usuario
     * @param idGeneral ID del usuario
     * @param email Email del usuario
     * @param telefono Teléfono del usuario
     * @param direccion Dirección del usuario
     * @param saldoTotal Saldo inicial
     * @param password Contraseña
     * @return El usuario creado
     */
    public Usuario crearUsuario(String nombre, String idGeneral, String email, String telefono, String direccion, double saldoTotal, String password) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        Usuario usuario = new Usuario(nombre, idGeneral, email, telefono, direccion, saldoTotal, null, password);
        dataManager.agregarUsuario(usuario);
        return usuario;
    }

    /**
     * Actualiza un usuario existente
     * @param idUsuario ID del usuario a actualizar
     * @param nombre Nuevo nombre
     * @param email Nuevo email
     * @param telefono Nuevo teléfono
     * @param direccion Nueva dirección
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarUsuario(String idUsuario, String nombre, String email, String telefono, String direccion) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        if (usuario == null) {
            return false;
        }

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        return true;
    }

    /**
     * Elimina un usuario
     * @param idUsuario ID del usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarUsuario(String idUsuario) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        if (usuario == null) {
            return false;
        }

        dataManager.eliminarUsuario(idUsuario);
        return true;
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    public LinkedList<Usuario> listarUsuarios() {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        return dataManager.getUsuarios();
    }

    /**
     * Gestiona cuentas (agregar, actualizar, eliminar)
     */

    /**
     * Crea una nueva cuenta
     * @param nombreBanco Nombre del banco
     * @param tipoCuenta Tipo de cuenta
     * @param numeroCuenta Número de cuenta
     * @return La cuenta creada
     */
    public Cuenta crearCuenta(String nombreBanco, TipoCuenta tipoCuenta, String numeroCuenta) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        String idCuenta = dataManager.generarId();
        Cuenta cuenta = new Cuenta(idCuenta, nombreBanco, tipoCuenta, numeroCuenta);
        dataManager.agregarCuenta(cuenta);
        return cuenta;
    }

    /**
     * Asigna una cuenta a un usuario
     * @param idUsuario ID del usuario
     * @param idCuenta ID de la cuenta
     * @return true si se asignó correctamente, false en caso contrario
     */
    public boolean asignarCuentaAUsuario(String idUsuario, String idCuenta) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        Cuenta cuenta = dataManager.buscarCuenta(idCuenta);

        if (usuario == null || cuenta == null) {
            return false;
        }

        return usuario.agregarCuenta(cuenta);
    }

    /**
     * Elimina una cuenta
     * @param idCuenta ID de la cuenta a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCuenta(String idCuenta) {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }
        Cuenta cuenta = dataManager.buscarCuenta(idCuenta);
        if (cuenta == null) {
            return false;
        }

        // Eliminar la cuenta de todos los usuarios que la tengan
        for (Usuario usuario : dataManager.getUsuarios()) {
            usuario.eliminarCuenta(idCuenta);
        }

        dataManager.eliminarCuenta(idCuenta);
        return true;
    }

    /**
     * Obtiene estadísticas de la billetera virtual
     * @return Mapa con las estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        if (dataManager == null) {
            dataManager = DataManager.getInstance();
        }

        Map<String, Object> estadisticas = new HashMap<>();

        // Saldo promedio de usuarios
        double saldoPromedio = dataManager.getUsuarios().stream()
                .mapToDouble(Usuario::getSaldoTotal)
                .average()
                .orElse(0.0);

        // Categorías más comunes (si hay transacciones)
        Map<String, Long> categoriasMasComunes = new HashMap<>();
        if (!dataManager.getTransacciones().isEmpty()) {
            categoriasMasComunes = dataManager.getTransacciones().stream()
                    .filter(t -> t.getCategoria() != null)
                    .collect(Collectors.groupingBy(
                            t -> t.getCategoria().getNombre(),
                            Collectors.counting()
                    ));
        }

        estadisticas.put("saldoPromedio", saldoPromedio);
        estadisticas.put("categoriasMasComunes", categoriasMasComunes);
        estadisticas.put("totalUsuarios", dataManager.getUsuarios().size());
        estadisticas.put("totalCuentas", dataManager.getCuentas().size());

        return estadisticas;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "nombre='" + getNombre() + '\'' +
                ", idGeneral='" + getIdGeneral() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cuentas=" + cuentas +
                '}';
    }
}
