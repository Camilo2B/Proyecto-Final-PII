# Resumen Detallado del Proyecto Billetera Virtual

## Introducción

Este documento presenta un resumen detallado de las clases principales que componen el proyecto de Billetera Virtual. El proyecto implementa una aplicación de gestión financiera personal que permite a los usuarios administrar sus cuentas, realizar transacciones, establecer presupuestos, visualizar estadísticas y generar reportes.

El proyecto está estructurado siguiendo varios patrones de diseño, como Singleton, Factory, Strategy, Command, Builder, Composite, Decorator y Facade, lo que facilita su mantenimiento y extensibilidad.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes principales:

- **Model**: Contiene las clases que representan el modelo de datos y la lógica de negocio.
  - **Builder**: Implementa el patrón Builder para la creación de reportes.
  - **Command**: Implementa el patrón Command para las operaciones de transacciones.
  - **Composite**: Implementa el patrón Composite para la gestión de categorías.
- **ViewController**: Contiene los controladores para las vistas de la aplicación.
- **util**: Contiene clases utilitarias.

## Clases Principales del Modelo

### Clases Base

#### Usuario
- **Descripción**: Representa a un usuario de la billetera virtual.
- **Atributos principales**: ID, nombre, email, teléfono, dirección, saldo total, cuentas asociadas, presupuestos, contraseña.
- **Métodos principales**: Gestión de cuentas (agregar, eliminar, buscar), gestión de presupuestos, cálculo de saldo total.
- **Relaciones**: Hereda de Persona, contiene colecciones de Cuenta y Presupuesto.

#### Cuenta
- **Descripción**: Representa una cuenta financiera del usuario.
- **Atributos principales**: ID, nombre del banco, tipo de cuenta, número de cuenta, saldo, estado (activa/inactiva).
- **Métodos principales**: Getters y setters para los atributos, métodos para operaciones financieras.
- **Relaciones**: Pertenece a un Usuario, participa en Transacciones.

#### Presupuesto
- **Descripción**: Representa un presupuesto establecido por el usuario.
- **Atributos principales**: ID, nombre, monto total, monto gastado, categoría, fechas de inicio y fin, estado.
- **Métodos principales**: Registro de gastos, cálculo de porcentaje de uso, verificación de exceso.
- **Relaciones**: Pertenece a un Usuario, asociado a una Categoría.

#### TransaccionFactory
- **Descripción**: Clase abstracta base para todas las transacciones.
- **Atributos principales**: ID, fecha, tipo, monto, descripción, cuentas asociadas, categoría, usuario.
- **Métodos principales**: Getters y setters para los atributos, métodos abstractos para operaciones específicas.
- **Relaciones**: Implementa la interfaz Transaccion, es extendida por clases específicas de transacciones.

### Clases de Servicios

#### BilleteraService
- **Descripción**: Servicio principal que implementa la lógica de negocio de la billetera virtual.
- **Atributos principales**: Instancias de otros servicios y gestores (DataManager, PresupuestoManager, etc.).
- **Métodos principales**: Operaciones de transacciones, gestión de cuentas y presupuestos, generación de reportes.
- **Patrón**: Singleton.
- **Relaciones**: Utiliza DataManager, PresupuestoManager, CategoriaManager, GestorCategorias, CommandInvoker.

#### AuthService
- **Descripción**: Servicio de autenticación para la aplicación.
- **Atributos principales**: Usuario autenticado, administrador autenticado.
- **Métodos principales**: Autenticación de usuarios y administradores, registro de usuarios, cierre de sesión.
- **Patrón**: Singleton.
- **Relaciones**: Utiliza DataManager.

#### DataManager
- **Descripción**: Gestiona la persistencia de datos de la aplicación.
- **Atributos principales**: Listas de usuarios, administradores, transacciones, categorías.
- **Métodos principales**: Carga y guardado de datos, búsqueda y manipulación de entidades.
- **Patrón**: Singleton.
- **Relaciones**: Contiene colecciones de todas las entidades del sistema.

#### FacadeBilletera
- **Descripción**: Fachada que simplifica el acceso a las funcionalidades del sistema.
- **Atributos principales**: Instancia de BilleteraService.
- **Métodos principales**: Métodos simplificados para operaciones comunes.
- **Patrón**: Facade.
- **Relaciones**: Utiliza BilleteraService.

### Clases del Patrón Builder

#### Reporte
- **Descripción**: Representa un reporte financiero.
- **Atributos principales**: Título, contenido, formato, fecha, usuario, transacciones incluidas.
- **Métodos principales**: Getters y setters para los atributos, método para generar el reporte.
- **Relaciones**: Es construido por ReporteBuilder.

#### ReporteBuilder
- **Descripción**: Interfaz para la construcción de reportes.
- **Métodos principales**: Métodos para establecer las diferentes partes del reporte.
- **Patrón**: Builder.
- **Relaciones**: Es implementada por ReporteConcreteBuilder.

#### ReporteConcreteBuilder
- **Descripción**: Implementación concreta del builder de reportes.
- **Atributos principales**: Instancia de Reporte en construcción.
- **Métodos principales**: Implementaciones de los métodos de ReporteBuilder.
- **Patrón**: Builder.
- **Relaciones**: Implementa ReporteBuilder, construye objetos Reporte.

#### ReporteDirector
- **Descripción**: Director que coordina la construcción de reportes.
- **Atributos principales**: Instancia de ReporteBuilder.
- **Métodos principales**: Métodos para construir diferentes tipos de reportes.
- **Patrón**: Builder.
- **Relaciones**: Utiliza ReporteBuilder.

### Clases del Patrón Command

#### Command
- **Descripción**: Interfaz para el patrón Command.
- **Métodos principales**: execute(), undo().
- **Patrón**: Command.
- **Relaciones**: Es implementada por clases de comandos específicos.

#### CommandInvoker
- **Descripción**: Invocador de comandos.
- **Atributos principales**: Historial de comandos ejecutados.
- **Métodos principales**: executeCommand(), undoLastCommand().
- **Patrón**: Command.
- **Relaciones**: Utiliza objetos Command.

#### DepositoCommand, RetiroCommand, TransferenciaCommand
- **Descripción**: Comandos específicos para operaciones financieras.
- **Atributos principales**: Datos necesarios para la operación, instancia de BilleteraService.
- **Métodos principales**: execute(), undo().
- **Patrón**: Command.
- **Relaciones**: Implementan Command, utilizan BilleteraService.

### Clases del Patrón Composite

#### ComponenteCategoria
- **Descripción**: Clase abstracta base para el patrón Composite de categorías.
- **Atributos principales**: ID, nombre, descripción, padre.
- **Métodos principales**: Métodos para agregar, eliminar y obtener componentes, cálculo de totales.
- **Patrón**: Composite.
- **Relaciones**: Es extendida por CategoriaSimple y GrupoCategoria.

#### CategoriaSimple
- **Descripción**: Representa una categoría simple (hoja en el patrón Composite).
- **Atributos principales**: Heredados de ComponenteCategoria.
- **Métodos principales**: Implementaciones de los métodos de ComponenteCategoria.
- **Patrón**: Composite.
- **Relaciones**: Extiende ComponenteCategoria.

#### GrupoCategoria
- **Descripción**: Representa un grupo de categorías (compuesto en el patrón Composite).
- **Atributos principales**: Heredados de ComponenteCategoria, lista de componentes.
- **Métodos principales**: Implementaciones de los métodos de ComponenteCategoria.
- **Patrón**: Composite.
- **Relaciones**: Extiende ComponenteCategoria, contiene otros ComponenteCategoria.

#### GestorCategorias
- **Descripción**: Gestiona la estructura de categorías.
- **Atributos principales**: Raíz del árbol de categorías.
- **Métodos principales**: Creación y eliminación de categorías, búsqueda, sincronización.
- **Patrón**: Singleton.
- **Relaciones**: Utiliza ComponenteCategoria, CategoriaSimple, GrupoCategoria.

## Clases Principales de la Vista-Controlador

### SceneController
- **Descripción**: Controlador principal para la navegación entre vistas.
- **Atributos principales**: Stage principal, usuario actual.
- **Métodos principales**: Cambio de escenas, mostrar diálogos, gestión del usuario actual.
- **Patrón**: Singleton.
- **Relaciones**: Utilizado por todos los controladores de vista.

### DashboardController
- **Descripción**: Controlador para la vista principal del dashboard.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, carga de datos, navegación, actualización de la interfaz.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService.

### TransaccionesController
- **Descripción**: Controlador para la vista de transacciones.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, realización de transacciones, navegación.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService.

### CuentasController
- **Descripción**: Controlador para la vista de gestión de cuentas.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, creación y eliminación de cuentas, navegación.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService.

### CategoriasController
- **Descripción**: Controlador para la vista de gestión de categorías.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, creación y eliminación de categorías, navegación.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService, GestorCategorias.

### PresupuestosController
- **Descripción**: Controlador para la vista de gestión de presupuestos.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, creación y eliminación de presupuestos, navegación.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService.

### EstadisticasController
- **Descripción**: Controlador para la vista de estadísticas.
- **Atributos principales**: Referencias a elementos de la interfaz, servicios.
- **Métodos principales**: Inicialización, generación de gráficos, navegación.
- **Relaciones**: Utiliza SceneController, BilleteraService, AuthService.

## Patrones de Diseño Implementados

### Singleton
- **Clases**: BilleteraService, AuthService, DataManager, SceneController, GestorCategorias, CommandInvoker, PresupuestoManager, CategoriaManager.
- **Propósito**: Garantizar que una clase tenga una única instancia y proporcionar un punto de acceso global a ella.

### Factory
- **Clases**: TransaccionFactory, DepositoFactory, RetiroFactory, TransferenciaFactory.
- **Propósito**: Crear objetos sin especificar la clase exacta del objeto que se creará.

### Strategy
- **Clases**: OperacionStrategy, DepositoStrategy, RetiroStrategy, TransferenciaStrategy, OperacionContext.
- **Propósito**: Definir una familia de algoritmos, encapsular cada uno y hacerlos intercambiables.

### Command
- **Clases**: Command, CommandInvoker, DepositoCommand, RetiroCommand, TransferenciaCommand.
- **Propósito**: Encapsular una solicitud como un objeto, permitiendo parametrizar clientes con diferentes solicitudes.

### Builder
- **Clases**: ReporteBuilder, ReporteConcreteBuilder, ReporteDirector, Reporte.
- **Propósito**: Separar la construcción de un objeto complejo de su representación.

### Composite
- **Clases**: ComponenteCategoria, CategoriaSimple, GrupoCategoria, GestorCategorias.
- **Propósito**: Componer objetos en estructuras de árbol para representar jerarquías parte-todo.

### Decorator
- **Clases**: SeguridadDecorator, SeguridadDosPasos.
- **Propósito**: Añadir responsabilidades a objetos dinámicamente.

### Facade
- **Clases**: FacadeBilletera.
- **Propósito**: Proporcionar una interfaz unificada para un conjunto de interfaces en un subsistema.

## Conclusión

El proyecto Billetera Virtual implementa una aplicación completa de gestión financiera personal utilizando múltiples patrones de diseño que facilitan su mantenimiento y extensibilidad. La arquitectura está claramente separada en capas (modelo, vista-controlador) y utiliza patrones como Singleton, Factory, Strategy, Command, Builder, Composite, Decorator y Facade para resolver problemas específicos de diseño.

La aplicación permite a los usuarios gestionar sus cuentas, realizar transacciones, establecer presupuestos, visualizar estadísticas y generar reportes, proporcionando una solución integral para la gestión financiera personal.
