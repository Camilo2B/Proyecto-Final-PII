package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.FormatoReporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.Reporte;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.ReporteConcreteBuilder;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Builder.ReporteDirector;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Command.*;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.ComponenteCategoria;
import co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model.Composite.GestorCategorias;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase de servicio principal que implementa la lógica de negocio de la billetera virtual.
 *
 * <p>Esta clase actúa como intermediario entre la interfaz de usuario y el modelo de datos,
 * proporcionando una API coherente para todas las operaciones financieras y de gestión
 * de la aplicación. Implementa el patrón Facade para simplificar la interacción con
 * los subsistemas complejos del modelo.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>Gestión de usuarios y sesiones</li>
 *   <li>Operaciones financieras (depósitos, retiros, transferencias)</li>
 *   <li>Gestión de presupuestos y categorías</li>
 *   <li>Generación de reportes y estadísticas</li>
 *   <li>Historial de operaciones con capacidad de deshacer/rehacer</li>
 * </ul>
 *
 * <p>Esta clase utiliza varios patrones de diseño:</p>
 * <ul>
 *   <li>Facade: Proporciona una interfaz unificada para los subsistemas</li>
 *   <li>Command: Para operaciones financieras con capacidad de deshacer/rehacer</li>
 *   <li>Singleton: Para acceder a instancias únicas como DataManager</li>
 *   <li>Strategy: Para diferentes estrategias de operaciones financieras</li>
 *   <li>Builder: Para la construcción de reportes personalizados</li>
 * </ul>
 */
public class BilleteraService {

    private static BilleteraService instance;
    private DataManager dataManager;
    private OperacionContext operacionContext;
    private PresupuestoManager presupuestoManager;
    private CategoriaManager categoriaManager;
    private GestorCategorias gestorCategorias;
    private CommandInvoker commandInvoker;
    private Usuario usuarioActual;
    private Administrador adminActual;
    private final LoggerService logger;

    /**
     * Constructor privado para implementar Singleton
     */
    public BilleteraService() {
        this.dataManager = DataManager.getInstance();
        this.presupuestoManager = PresupuestoManager.getInstance();
        this.categoriaManager = CategoriaManager.getInstance();
        this.gestorCategorias = GestorCategorias.getInstance();
        this.commandInvoker = CommandInvoker.getInstance();
        this.logger = LoggerService.getInstance();

        // Log de inicialización
        logger.info("BilleteraService inicializado correctamente");
    }

    /**
     * Obtiene la instancia única del servicio
     * @return Instancia del servicio
     */
    public static BilleteraService getInstance() {
        if (instance == null) {
            instance = new BilleteraService();
        }
        return instance;
    }

    /**
     * Inicia sesión de un usuario
     * @param idUsuario ID del usuario
     * @param password Contraseña
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesionUsuario(String idUsuario, String password) {
        logger.info("Intento de inicio de sesión de usuario: {}", idUsuario);

        Usuario usuario = dataManager.buscarUsuario(idUsuario);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            logger.warn("Inicio de sesión fallido para usuario: {} - Credenciales inválidas", idUsuario);
            logger.auditLogin(idUsuario, false, "USER", null);
            return false;
        }

        this.usuarioActual = usuario;
        logger.setUserContext(idUsuario, "USER");
        logger.info("Inicio de sesión exitoso para usuario: {}", idUsuario);
        logger.auditLogin(idUsuario, true, "USER", null);
        return true;
    }

    /**
     * Inicia sesión de un administrador
     * @param idAdmin ID del administrador
     * @param password Contraseña
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesionAdmin(String idAdmin, String password) {
        logger.info("Intento de inicio de sesión de administrador: {}", idAdmin);

        Administrador admin = dataManager.buscarAdministrador(idAdmin);
        if (admin == null || !admin.getPassword().equals(password)) {
            logger.warn("Inicio de sesión fallido para administrador: {} - Credenciales inválidas", idAdmin);
            logger.auditLogin(idAdmin, false, "ADMIN", null);
            return false;
        }

        this.adminActual = admin;
        logger.setUserContext(idAdmin, "ADMIN");
        logger.info("Inicio de sesión exitoso para administrador: {}", idAdmin);
        logger.auditLogin(idAdmin, true, "ADMIN", null);
        return true;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        String userId = null;
        String userType = null;

        if (usuarioActual != null) {
            userId = usuarioActual.getIdGeneral();
            userType = "USER";
        } else if (adminActual != null) {
            userId = adminActual.getIdGeneral();
            userType = "ADMIN";
        }

        if (userId != null) {
            logger.info("Cerrando sesión para {}: {}", userType, userId);
            logger.auditLogout(userId, userType);
        }

        this.usuarioActual = null;
        this.adminActual = null;
        logger.clearUserContext();

        logger.info("Sesión cerrada correctamente");
    }

    /**
     * Verifica si hay un usuario con sesión iniciada
     * @return true si hay un usuario con sesión iniciada
     */
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }

    /**
     * Verifica si hay un administrador con sesión iniciada
     * @return true si hay un administrador con sesión iniciada
     */
    public boolean hayAdminLogueado() {
        return adminActual != null;
    }

    /**
     * Obtiene el usuario actual
     * @return Usuario actual o null si no hay sesión iniciada
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Establece el usuario actual
     * Este método es útil para sincronizar el estado entre diferentes servicios
     * @param usuario Usuario a establecer como actual
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    /**
     * Obtiene el administrador actual
     * @return Administrador actual o null si no hay sesión iniciada
     */
    public Administrador getAdminActual() {
        return adminActual;
    }

    /**
     * Establece el administrador actual
     * Este método es útil para sincronizar el estado entre diferentes servicios
     * @param admin Administrador a establecer como actual
     */
    public void setAdminActual(Administrador admin) {
        this.adminActual = admin;
    }

    /**
     * Registra un nuevo usuario
     * @param nombre Nombre del usuario
     * @param idGeneral ID del usuario
     * @param email Email del usuario
     * @param telefono Teléfono del usuario
     * @param direccion Dirección del usuario
     * @param password Contraseña
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarUsuario(String nombre, String idGeneral, String email, String telefono, String direccion, String password) {
        if (dataManager.buscarUsuario(idGeneral) != null) {
            return false;
        }

        Usuario usuario = new Usuario(nombre, idGeneral, email, telefono, direccion, 0.0, null, password);
        dataManager.agregarUsuario(usuario);
        return true;
    }

    /**
     * Realiza un depósito en la cuenta del usuario actual
     * @param monto Monto a depositar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el depósito fue exitoso, false en caso contrario
     */
    public boolean realizarDeposito(double monto, String idCuenta) {
        String userId = usuarioActual != null ? usuarioActual.getIdGeneral() : "UNKNOWN";
        logger.info("Iniciando depósito - Usuario: {} - Monto: {} - Cuenta: {}", userId, monto, idCuenta);

        if (!hayUsuarioLogueado() || monto <= 0) {
            logger.warn("Depósito rechazado - Usuario no logueado o monto inválido - Usuario: {} - Monto: {}", userId, monto);
            return false;
        }

        // Obtener la cuenta si se especificó un ID
        Cuenta cuenta = null;
        if (idCuenta != null && !idCuenta.isEmpty()) {
            cuenta = usuarioActual.buscarCuenta(idCuenta);
            if (cuenta == null) {
                logger.warn("Depósito rechazado - Cuenta no encontrada - Usuario: {} - Cuenta: {}", userId, idCuenta);
                return false;
            }
        }

        try {
            // Crear y ejecutar el comando de depósito
            DepositoCommand comando = new DepositoCommand(
                usuarioActual,
                cuenta,
                monto,
                "Depósito de fondos",
                null
            );

            boolean resultado = commandInvoker.ejecutarComando(comando);

            if (resultado) {
                logger.info("Depósito exitoso - Usuario: {} - Monto: {} - Cuenta: {}", userId, monto, idCuenta);
                logger.auditTransaction(userId, "DEPOSITO", monto, idCuenta != null ? idCuenta : "PRINCIPAL", "Depósito de fondos");
            } else {
                logger.warn("Depósito fallido - Usuario: {} - Monto: {} - Cuenta: {}", userId, monto, idCuenta);
            }

            return resultado;
        } catch (Exception e) {
            logger.error("Error en depósito - Usuario: {} - Monto: {} - Cuenta: {}", userId, monto, idCuenta, e);
            return false;
        }
    }

    /**
     * Realiza un depósito en la cuenta del usuario actual con categoría
     * @param monto Monto a depositar
     * @param idCuenta ID de la cuenta (opcional)
     * @param descripcion Descripción del depósito
     * @param categoria Categoría del depósito
     * @return true si el depósito fue exitoso, false en caso contrario
     */
    public boolean realizarDeposito(double monto, String idCuenta, String descripcion, Categoria categoria) {
        if (!hayUsuarioLogueado() || monto <= 0) {
            return false;
        }

        // Obtener la cuenta si se especificó un ID
        Cuenta cuenta = null;
        if (idCuenta != null && !idCuenta.isEmpty()) {
            cuenta = usuarioActual.buscarCuenta(idCuenta);
            if (cuenta == null) {
                return false;
            }
        }

        // Crear y ejecutar el comando de depósito
        DepositoCommand comando = new DepositoCommand(
            usuarioActual,
            cuenta,
            monto,
            descripcion != null ? descripcion : "Depósito de fondos",
            categoria
        );

        return commandInvoker.ejecutarComando(comando);
    }

    /**
     * Realiza un retiro de la cuenta del usuario actual
     * @param monto Monto a retirar
     * @param idCuenta ID de la cuenta (opcional)
     * @return true si el retiro fue exitoso, false en caso contrario
     */
    public boolean realizarRetiro(double monto, String idCuenta) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }

        // Obtener la cuenta si se especificó un ID
        Cuenta cuenta = null;
        if (idCuenta != null && !idCuenta.isEmpty()) {
            cuenta = usuarioActual.buscarCuenta(idCuenta);
            if (cuenta == null) {
                return false;
            }

            // Verificar si la cuenta tiene saldo suficiente
            if (cuenta.getSaldo() < monto) {
                return false;
            }
        }

        // Crear y ejecutar el comando de retiro
        RetiroCommand comando = new RetiroCommand(
            usuarioActual,
            cuenta,
            monto,
            "Retiro de fondos",
            null
        );

        return commandInvoker.ejecutarComando(comando);
    }

    /**
     * Realiza un retiro de la cuenta del usuario actual con categoría
     * @param monto Monto a retirar
     * @param idCuenta ID de la cuenta (opcional)
     * @param descripcion Descripción del retiro
     * @param categoria Categoría del retiro
     * @return true si el retiro fue exitoso, false en caso contrario
     */
    public boolean realizarRetiro(double monto, String idCuenta, String descripcion, Categoria categoria) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }

        // Obtener la cuenta si se especificó un ID
        Cuenta cuenta = null;
        if (idCuenta != null && !idCuenta.isEmpty()) {
            cuenta = usuarioActual.buscarCuenta(idCuenta);
            if (cuenta == null) {
                return false;
            }

            // Verificar si la cuenta tiene saldo suficiente
            if (cuenta.getSaldo() < monto) {
                return false;
            }
        }

        // Crear y ejecutar el comando de retiro
        RetiroCommand comando = new RetiroCommand(
            usuarioActual,
            cuenta,
            monto,
            descripcion != null ? descripcion : "Retiro de fondos",
            categoria
        );

        return commandInvoker.ejecutarComando(comando);
    }

    /**
     * Realiza una transferencia desde la cuenta del usuario actual
     * @param monto Monto a transferir
     * @param idCuentaOrigen ID de la cuenta origen
     * @param idCuentaDestino ID de la cuenta destino
     * @return true si la transferencia fue exitosa, false en caso contrario
     */
    public boolean realizarTransferencia(double monto, String idCuentaOrigen, String idCuentaDestino) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }

        // Obtener la cuenta origen
        Cuenta cuentaOrigen = null;
        if (idCuentaOrigen != null && !idCuentaOrigen.isEmpty()) {
            cuentaOrigen = usuarioActual.buscarCuenta(idCuentaOrigen);
            if (cuentaOrigen == null) {
                return false;
            }

            // Verificar si la cuenta origen tiene saldo suficiente
            if (cuentaOrigen.getSaldo() < monto) {
                return false;
            }
        }

        // Obtener la cuenta destino (puede ser de otro usuario)
        Cuenta cuentaDestino = null;
        Usuario usuarioDestino = usuarioActual; // Por defecto, transferencia a sí mismo

        if (idCuentaDestino != null && !idCuentaDestino.isEmpty()) {
            // Buscar primero en las cuentas del usuario actual
            cuentaDestino = usuarioActual.buscarCuenta(idCuentaDestino);

            // Si no se encuentra, buscar en todos los usuarios
            if (cuentaDestino == null) {
                for (Usuario usuario : dataManager.getUsuarios()) {
                    if (usuario.equals(usuarioActual)) {
                        continue; // Ya buscamos en el usuario actual
                    }

                    Cuenta cuenta = usuario.buscarCuenta(idCuentaDestino);
                    if (cuenta != null) {
                        cuentaDestino = cuenta;
                        usuarioDestino = usuario;
                        break;
                    }
                }

                // Si aún no se encuentra, error
                if (cuentaDestino == null) {
                    return false;
                }
            }
        }

        // Crear y ejecutar el comando de transferencia
        TransferenciaCommand comando = new TransferenciaCommand(
            usuarioActual,
            cuentaOrigen,
            usuarioDestino,
            cuentaDestino,
            monto,
            "Transferencia de fondos",
            null
        );

        return commandInvoker.ejecutarComando(comando);
    }

    /**
     * Realiza una transferencia desde la cuenta del usuario actual con categoría
     * @param monto Monto a transferir
     * @param idCuentaOrigen ID de la cuenta origen
     * @param idCuentaDestino ID de la cuenta destino
     * @param descripcion Descripción de la transferencia
     * @param categoria Categoría de la transferencia
     * @return true si la transferencia fue exitosa, false en caso contrario
     */
    public boolean realizarTransferencia(double monto, String idCuentaOrigen, String idCuentaDestino,
                                        String descripcion, Categoria categoria) {
        if (!hayUsuarioLogueado() || monto <= 0 || usuarioActual.getSaldoTotal() < monto) {
            return false;
        }

        // Obtener la cuenta origen
        Cuenta cuentaOrigen = null;
        if (idCuentaOrigen != null && !idCuentaOrigen.isEmpty()) {
            cuentaOrigen = usuarioActual.buscarCuenta(idCuentaOrigen);
            if (cuentaOrigen == null) {
                return false;
            }

            // Verificar si la cuenta origen tiene saldo suficiente
            if (cuentaOrigen.getSaldo() < monto) {
                return false;
            }
        }

        // Obtener la cuenta destino (puede ser de otro usuario)
        Cuenta cuentaDestino = null;
        Usuario usuarioDestino = usuarioActual; // Por defecto, transferencia a sí mismo

        if (idCuentaDestino != null && !idCuentaDestino.isEmpty()) {
            // Buscar primero en las cuentas del usuario actual
            cuentaDestino = usuarioActual.buscarCuenta(idCuentaDestino);

            // Si no se encuentra, buscar en todos los usuarios
            if (cuentaDestino == null) {
                for (Usuario usuario : dataManager.getUsuarios()) {
                    if (usuario.equals(usuarioActual)) {
                        continue; // Ya buscamos en el usuario actual
                    }

                    Cuenta cuenta = usuario.buscarCuenta(idCuentaDestino);
                    if (cuenta != null) {
                        cuentaDestino = cuenta;
                        usuarioDestino = usuario;
                        break;
                    }
                }

                // Si aún no se encuentra, error
                if (cuentaDestino == null) {
                    return false;
                }
            }
        }

        // Crear y ejecutar el comando de transferencia
        TransferenciaCommand comando = new TransferenciaCommand(
            usuarioActual,
            cuentaOrigen,
            usuarioDestino,
            cuentaDestino,
            monto,
            descripcion != null ? descripcion : "Transferencia de fondos",
            categoria
        );

        return commandInvoker.ejecutarComando(comando);
    }

    /**
     * Crea un nuevo presupuesto para el usuario actual
     * @param nombre Nombre del presupuesto
     * @param montoTotal Monto total asignado
     * @param categoriaEspecifica Categoría específica (opcional)
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearPresupuesto(String nombre, double montoTotal, String categoriaEspecifica) {
        if (!hayUsuarioLogueado() || montoTotal <= 0) {
            return false;
        }

        Presupuesto presupuesto = presupuestoManager.crearPresupuesto(nombre, montoTotal, categoriaEspecifica);
        return usuarioActual.agregarPresupuesto(presupuesto);
    }

    /**
     * Obtiene los presupuestos del usuario actual
     * @return Lista de presupuestos
     */
    public LinkedList<Presupuesto> obtenerPresupuestosUsuario() {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }

        return usuarioActual.getPresupuestos();
    }

    /**
     * Crea una nueva categoría
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearCategoria(String nombre, String descripcion) {
        if (categoriaManager.buscarCategoriaPorNombre(nombre) != null) {
            return false;
        }

        categoriaManager.crearCategoria(nombre, descripcion);
        return true;
    }

    /**
     * Obtiene todas las categorías
     * @return Lista de categorías
     */
    public LinkedList<Categoria> obtenerCategorias() {
        return categoriaManager.obtenerCategorias();
    }

    /**
     * Obtiene las transacciones del usuario actual
     * @return Lista de transacciones
     */
    public LinkedList<TransaccionFactory> obtenerTransaccionesUsuario() {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }

        // Filtrar transacciones por usuario
        String idUsuario = usuarioActual.getIdGeneral();
        return dataManager.getTransacciones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getIdGeneral().equals(idUsuario))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Obtiene las transacciones de un usuario específico
     * @param usuario Usuario del que se quieren obtener las transacciones
     * @return Lista de transacciones del usuario
     */
    public LinkedList<TransaccionFactory> obtenerTransaccionesUsuario(Usuario usuario) {
        if (usuario == null) {
            return new LinkedList<>();
        }

        // Filtrar transacciones por el usuario especificado
        String idUsuario = usuario.getIdGeneral();
        return dataManager.getTransacciones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getIdGeneral().equals(idUsuario))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Obtiene las transacciones por tipo
     * @param tipo Tipo de transacción
     * @return Lista de transacciones del tipo especificado
     */
    public LinkedList<TransaccionFactory> obtenerTransaccionesPorTipo(TipoTransaccion tipo) {
        if (!hayUsuarioLogueado()) {
            return new LinkedList<>();
        }

        // Filtrar transacciones por tipo y usuario
        String idUsuario = usuarioActual.getIdGeneral();
        return dataManager.getTransaccionesPorTipo(tipo).stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getIdGeneral().equals(idUsuario))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Genera un reporte financiero utilizando el patrón Builder
     *
     * <p>Este método permite generar diferentes tipos de reportes financieros
     * personalizados según las necesidades del usuario. Utiliza el patrón Builder
     * para construir reportes con diferentes configuraciones y contenidos.</p>
     *
     * @param tipoReporte Tipo de reporte a generar ("completo", "resumido" o "categorias")
     * @param fechaInicio Fecha de inicio del período del reporte
     * @param fechaFin Fecha de fin del período del reporte
     * @param formato Formato del reporte (PDF, CSV, etc.)
     * @return Reporte generado o null si no se pudo generar
     */
    public Reporte generarReporte(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin, FormatoReporte formato) {
        if (!hayUsuarioLogueado()) {
            return null;
        }

        // Crear el builder y el director
        ReporteConcreteBuilder builder = new ReporteConcreteBuilder();
        ReporteDirector director = new ReporteDirector(builder);

        // Generar el reporte según el tipo solicitado
        switch (tipoReporte.toLowerCase()) {
            case "completo":
                return director.construirReporteCompleto(usuarioActual, fechaInicio, fechaFin, formato);
            case "resumido":
                return director.construirReporteResumido(usuarioActual, fechaInicio, fechaFin, formato);
            case "categorias":
                return director.construirReportePorCategorias(usuarioActual, fechaInicio, fechaFin, formato);
            default:
                return null;
        }
    }

    /**
     * Obtiene todas las categorías utilizando el patrón Composite
     *
     * <p>Este método devuelve todas las categorías disponibles en el sistema,
     * organizadas en una estructura jerárquica utilizando el patrón Composite.</p>
     *
     * @return Lista de componentes de categoría
     */
    public List<ComponenteCategoria> obtenerCategoriasJerarquicas() {
        return gestorCategorias.obtenerTodasLasCategorias();
    }

    /**
     * Agrega una categoría simple a un grupo específico
     *
     * @param nombreCategoria Nombre de la categoría
     * @param descripcion Descripción de la categoría
     * @param idGrupo ID del grupo al que se agregará (null para agregar a la raíz)
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarCategoriaSimple(String nombreCategoria, String descripcion, String idGrupo) {
        return gestorCategorias.agregarCategoria(nombreCategoria, descripcion, idGrupo);
    }

    /**
     * Agrega un grupo de categorías a un grupo específico
     *
     * @param nombreGrupo Nombre del grupo
     * @param descripcion Descripción del grupo
     * @param idGrupoPadre ID del grupo padre (null para agregar a la raíz)
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarGrupoCategoria(String nombreGrupo, String descripcion, String idGrupoPadre) {
        return gestorCategorias.agregarGrupo(nombreGrupo, descripcion, idGrupoPadre);
    }

    /**
     * Elimina una categoría o grupo por su ID
     *
     * @param idCategoria ID de la categoría o grupo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCategoria(String idCategoria) {
        return gestorCategorias.eliminarComponente(idCategoria);
    }

    /**
     * Deshace la última operación realizada
     *
     * <p>Este método utiliza el patrón Command para deshacer la última operación
     * financiera realizada por el usuario.</p>
     *
     * @return true si se deshizo correctamente, false en caso contrario
     */
    public boolean deshacerOperacion() {
        return commandInvoker.deshacer();
    }

    /**
     * Rehace la última operación deshecha
     *
     * <p>Este método utiliza el patrón Command para rehacer la última operación
     * financiera que fue deshecha por el usuario.</p>
     *
     * @return true si se rehizo correctamente, false en caso contrario
     */
    public boolean rehacerOperacion() {
        return commandInvoker.rehacer();
    }

    /**
     * Verifica si se puede deshacer alguna operación
     *
     * @return true si hay operaciones para deshacer, false en caso contrario
     */
    public boolean puedeDeshacer() {
        return commandInvoker.puedeDeshacer();
    }

    /**
     * Verifica si se puede rehacer alguna operación
     *
     * @return true si hay operaciones para rehacer, false en caso contrario
     */
    public boolean puedeRehacer() {
        return commandInvoker.puedeRehacer();
    }

    /**
     * Obtiene el historial de operaciones realizadas
     *
     * @return Lista de comandos ejecutados
     */
    public List<Command> obtenerHistorialOperaciones() {
        return commandInvoker.getHistorialComandos();
    }

    /**
     * Obtiene estadísticas de la billetera virtual (solo para administradores)
     * @return Mapa con las estadísticas o null si no hay un administrador logueado
     */
    public Map<String, Object> obtenerEstadisticas() {
        if (!hayAdminLogueado()) {
            return null;
        }

        return adminActual.obtenerEstadisticas();
    }

    /**
     * Realiza un depósito en la cuenta del usuario especificado
     * @param usuario Usuario que realiza el depósito
     * @param cuenta Cuenta donde se realiza el depósito (opcional)
     * @param monto Monto a depositar
     * @param descripcion Descripción del depósito
     * @param categoria Categoría del depósito (opcional)
     * @return La transacción creada o null si hubo un error
     */
    public TransaccionFactory realizarDeposito(Usuario usuario, Cuenta cuenta, double monto, String descripcion, Categoria categoria) {
        if (usuario == null || monto <= 0) {
            return null;
        }

        try {
            // Actualizar saldo del usuario
            usuario.setSaldoTotal(usuario.getSaldoTotal() + monto);

            // Actualizar saldo de la cuenta si se especificó
            if (cuenta != null) {
                cuenta.setSaldo(cuenta.getSaldo() + monto);
            }

            // Crear la transacción
            String idTransaccion = dataManager.generarId();
            TransaccionFactory transaccion = new TransaccionDeposito();
            transaccion.setIdTransaccion(idTransaccion);
            transaccion.setFechaTransaccion(java.time.LocalDate.now());
            transaccion.setTipoTransaccion(TipoTransaccion.DEPOSITO);
            transaccion.setMonto(monto);
            transaccion.setDescripcion(descripcion);
            transaccion.setCategoria(categoria);
            transaccion.setUsuario(usuario);

            // Guardar la transacción
            dataManager.agregarTransaccion(transaccion);

            return transaccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Realiza un retiro de la cuenta del usuario especificado
     * @param usuario Usuario que realiza el retiro
     * @param cuenta Cuenta de donde se realiza el retiro (opcional)
     * @param monto Monto a retirar
     * @param descripcion Descripción del retiro
     * @param categoria Categoría del retiro (opcional)
     * @return La transacción creada o null si hubo un error
     */
    public TransaccionFactory realizarRetiro(Usuario usuario, Cuenta cuenta, double monto, String descripcion, Categoria categoria) {
        if (usuario == null || monto <= 0 || usuario.getSaldoTotal() < monto) {
            return null;
        }

        // Verificar si la cuenta tiene saldo suficiente
        if (cuenta != null && cuenta.getSaldo() < monto) {
            return null;
        }

        try {
            // Actualizar saldo del usuario
            usuario.setSaldoTotal(usuario.getSaldoTotal() - monto);

            // Actualizar saldo de la cuenta si se especificó
            if (cuenta != null) {
                cuenta.setSaldo(cuenta.getSaldo() - monto);
            }

            // Crear la transacción
            String idTransaccion = dataManager.generarId();
            TransaccionFactory transaccion = new TransaccionRetiro();
            transaccion.setIdTransaccion(idTransaccion);
            transaccion.setFechaTransaccion(java.time.LocalDate.now());
            transaccion.setTipoTransaccion(TipoTransaccion.RETIRO);
            transaccion.setMonto(monto);
            transaccion.setDescripcion(descripcion);
            transaccion.setCategoria(categoria);
            transaccion.setUsuario(usuario);

            // Guardar la transacción
            dataManager.agregarTransaccion(transaccion);

            return transaccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todas las categorías disponibles
     * @return Lista de categorías
     */
    public List<Categoria> obtenerTodasLasCategorias() {
        return dataManager.getCategorias();
    }

    /**
     * Obtiene los presupuestos de un usuario
     * @param usuario Usuario del que se obtendrán los presupuestos
     * @return Lista de presupuestos del usuario
     */
    public List<Presupuesto> obtenerPresupuestosUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        return usuario.getPresupuestos();
    }

    /**
     * Agrega un presupuesto a un usuario
     * @param usuario Usuario al que se le agregará el presupuesto
     * @param presupuesto Presupuesto a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarPresupuestoUsuario(Usuario usuario, Presupuesto presupuesto) {
        if (usuario == null || presupuesto == null) {
            return false;
        }

        try {
            // Agregar el presupuesto al usuario
            usuario.agregarPresupuesto(presupuesto);

            // Guardar los cambios
            dataManager.guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un presupuesto de un usuario
     * @param usuario Usuario del que se eliminará el presupuesto
     * @param presupuesto Presupuesto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarPresupuestoUsuario(Usuario usuario, Presupuesto presupuesto) {
        if (usuario == null || presupuesto == null) {
            return false;
        }

        try {
            // Eliminar el presupuesto del usuario
            usuario.eliminarPresupuesto(presupuesto.getId());

            // Guardar los cambios
            dataManager.guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene las transacciones de un usuario en un rango de fechas
     * @param usuario Usuario del que se obtendrán las transacciones
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de transacciones en el rango de fechas
     */
    public List<TransaccionFactory> obtenerTransaccionesEnRango(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin) {
        if (usuario == null || fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }

        // Obtener todas las transacciones del usuario
        List<TransaccionFactory> todasLasTransacciones = dataManager.getTransacciones().stream()
                .filter(t -> {
                    Usuario usuarioTransaccion = t.getUsuario();
                    return usuarioTransaccion != null && usuarioTransaccion.getIdGeneral().equals(usuario.getIdGeneral());
                })
                .collect(Collectors.toList());

        // Filtrar por rango de fechas
        return todasLasTransacciones.stream()
                .filter(t -> {
                    LocalDate fechaTransaccion = t.getFechaTransaccion();
                    return !fechaTransaccion.isBefore(fechaInicio) && !fechaTransaccion.isAfter(fechaFin);
                })
                .collect(Collectors.toList());
    }

    /**
     * Agrega una cuenta a un usuario
     * @param usuario Usuario al que se le agregará la cuenta
     * @param cuenta Cuenta a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarCuentaUsuario(Usuario usuario, Cuenta cuenta) {
        if (usuario == null || cuenta == null) {
            return false;
        }

        try {
            // Agregar la cuenta al usuario
            usuario.agregarCuenta(cuenta);

            // Actualizar el saldo total del usuario
            usuario.setSaldoTotal(usuario.getSaldoTotal() + cuenta.getSaldo());

            // Guardar los cambios
            dataManager.guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una cuenta de un usuario
     * @param usuario Usuario del que se eliminará la cuenta
     * @param cuenta Cuenta a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCuentaUsuario(Usuario usuario, Cuenta cuenta) {
        if (usuario == null || cuenta == null) {
            return false;
        }

        try {
            // Actualizar el saldo total del usuario
            usuario.setSaldoTotal(usuario.getSaldoTotal() - cuenta.getSaldo());

            // Eliminar la cuenta del usuario
            usuario.eliminarCuenta(cuenta);

            // Guardar los cambios
            dataManager.guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Realiza una transferencia entre cuentas
     * @param usuarioOrigen Usuario que realiza la transferencia
     * @param cuentaOrigen Cuenta de origen (opcional)
     * @param usuarioDestino Usuario destinatario
     * @param cuentaDestino Cuenta de destino
     * @param monto Monto a transferir
     * @param descripcion Descripción de la transferencia
     * @param categoria Categoría de la transferencia (opcional)
     * @return La transacción creada o null si hubo un error
     */
    public TransaccionFactory realizarTransferencia(Usuario usuarioOrigen, Cuenta cuentaOrigen, Usuario usuarioDestino, Cuenta cuentaDestino, double monto, String descripcion, Categoria categoria) {
        if (usuarioOrigen == null || usuarioDestino == null || cuentaDestino == null || monto <= 0 || usuarioOrigen.getSaldoTotal() < monto) {
            return null;
        }

        // Verificar si la cuenta origen tiene saldo suficiente
        if (cuentaOrigen != null && cuentaOrigen.getSaldo() < monto) {
            return null;
        }

        try {
            // Actualizar saldo del usuario origen
            usuarioOrigen.setSaldoTotal(usuarioOrigen.getSaldoTotal() - monto);

            // Actualizar saldo de la cuenta origen si se especificó
            if (cuentaOrigen != null) {
                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
            }

            // Actualizar saldo del usuario destino (si es diferente al origen)
            if (!usuarioOrigen.equals(usuarioDestino)) {
                usuarioDestino.setSaldoTotal(usuarioDestino.getSaldoTotal() + monto);
            }

            // Actualizar saldo de la cuenta destino
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

            // Crear la transacción
            String idTransaccion = dataManager.generarId();
            TransaccionTransferencia transaccion = new TransaccionTransferencia();
            transaccion.setIdTransaccion(idTransaccion);
            transaccion.setFechaTransaccion(java.time.LocalDate.now());
            transaccion.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
            transaccion.setMonto(monto);
            transaccion.setDescripcion(descripcion);
            transaccion.setCategoria(categoria);
            transaccion.setUsuario(usuarioOrigen);
            transaccion.setUsuarioDestino(usuarioDestino);
            transaccion.setCuentaDestino(cuentaDestino);

            // Guardar la transacción
            dataManager.agregarTransaccion(transaccion);

            return transaccion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una transacción
     * @param transaccion Transacción a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarTransaccion(TransaccionFactory transaccion) {
        if (transaccion == null) {
            return false;
        }

        try {
            // Buscar la transacción en el DataManager
            TransaccionFactory transaccionEncontrada = dataManager.buscarTransaccion(transaccion.getIdTransaccion());
            if (transaccionEncontrada == null) {
                return false;
            }

            // Eliminar la transacción
            dataManager.getTransacciones().remove(transaccionEncontrada);
            dataManager.guardarDatos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene estadísticas generales del sistema
     * Este método puede ser llamado desde la fachada para obtener estadísticas generales
     * @return Mapa con las estadísticas generales
     */
    public Map<String, Object> obtenerEstadisticasGenerales() {
        // Si hay un administrador logueado, usar sus estadísticas
        if (hayAdminLogueado()) {
            return adminActual.obtenerEstadisticas();
        }

        // Si no hay administrador pero hay usuario, obtener estadísticas básicas
        if (hayUsuarioLogueado()) {
            Map<String, Object> estadisticas = new HashMap<>();

            // Obtener datos básicos
            estadisticas.put("totalUsuarios", dataManager.getUsuarios().size());
            estadisticas.put("totalTransacciones", dataManager.getTransacciones().size());
            estadisticas.put("saldoUsuarioActual", usuarioActual.getSaldoTotal());

            // Calcular totales por tipo de transacción
            double totalDepositos = dataManager.getTransaccionesPorTipo(TipoTransaccion.DEPOSITO)
                    .stream()
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();

            double totalRetiros = dataManager.getTransaccionesPorTipo(TipoTransaccion.RETIRO)
                    .stream()
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();

            double totalTransferencias = dataManager.getTransaccionesPorTipo(TipoTransaccion.TRANSFERENCIA)
                    .stream()
                    .mapToDouble(TransaccionFactory::getMonto)
                    .sum();

            estadisticas.put("totalDepositos", totalDepositos);
            estadisticas.put("totalRetiros", totalRetiros);
            estadisticas.put("totalTransferencias", totalTransferencias);

            return estadisticas;
        }

        // Si no hay usuario ni administrador, devolver estadísticas mínimas
        Map<String, Object> estadisticasMinimas = new HashMap<>();
        estadisticasMinimas.put("totalUsuarios", dataManager.getUsuarios().size());
        estadisticasMinimas.put("totalTransacciones", dataManager.getTransacciones().size());

        return estadisticasMinimas;
    }
}
