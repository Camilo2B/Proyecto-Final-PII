# Diagrama de Clases - Billetera Virtual

## Descripción General

El siguiente documento describe el diagrama de clases actualizado de la aplicación Billetera Virtual, incluyendo los patrones de diseño implementados y las relaciones entre las diferentes clases y componentes del sistema.

## Patrones de Diseño Implementados

### Patrones Creacionales

1. **Singleton**
   - Implementado en:
     - `DataManager`: Gestiona el acceso a los datos de la aplicación.
     - `CommandInvoker`: Gestiona la ejecución de comandos.
     - `GestorCategorias`: Gestiona la jerarquía de categorías.

2. **Factory Method**
   - Implementado en:
     - `TransaccionFactory`: Crea diferentes tipos de transacciones.
     - `DepositoFactory`: Crea transacciones de depósito.
     - `RetiroFactory`: Crea transacciones de retiro.
     - `TransferenciaFactory`: Crea transacciones de transferencia.

3. **Builder**
   - Implementado en:
     - `ReporteBuilder`: Interfaz para construir reportes.
     - `ReporteConcreteBuilder`: Implementación concreta del builder.
     - `ReporteDirector`: Dirige la construcción de diferentes tipos de reportes.
     - `Reporte`: Producto final construido.

### Patrones Estructurales

1. **Decorator**
   - Implementado en:
     - `ICuenta`: Interfaz para cuentas.
     - `Cuenta`: Implementación básica de cuenta.
     - `SeguridadDecorator`: Decorador base para seguridad.
     - `SeguridadDosPasos`: Decorador concreto para autenticación de dos pasos.
     - `Pin`: Decorador concreto para seguridad con PIN.

2. **Facade**
   - Implementado en:
     - `BilleteraVirtual`: Interfaz de la fachada.
     - `FacadeBilletera`: Implementación de la fachada.

3. **Composite**
   - Implementado en:
     - `ComponenteCategoria`: Interfaz componente para categorías.
     - `CategoriaSimple`: Implementación de hoja (categoría simple).
     - `GrupoCategoria`: Implementación de compuesto (grupo de categorías).

### Patrones de Comportamiento

1. **Strategy**
   - Implementado en:
     - `OperacionStrategy`: Interfaz para estrategias de operaciones financieras.
     - `DepositoStrategy`: Estrategia concreta para depósitos.
     - `RetiroStrategy`: Estrategia concreta para retiros.
     - `TransferenciaStrategy`: Estrategia concreta para transferencias.
     - `OperacionContext`: Contexto que utiliza las estrategias.

2. **Command**
   - Implementado en:
     - `Command`: Interfaz para comandos.
     - `DepositoCommand`: Comando concreto para depósitos.
     - `RetiroCommand`: Comando concreto para retiros.
     - `TransferenciaCommand`: Comando concreto para transferencias.
     - `CommandInvoker`: Invocador que ejecuta los comandos.

3. **Observer**
   - Implementado en:
     - `CommandInvoker.CommandListener`: Interfaz para observadores de cambios en el historial de comandos.

## Estructura del Diagrama

```
+------------------+     +------------------+     +------------------+
|  BilleteraService|---->|    DataManager   |<----|  GestorCategorias|
+------------------+     +------------------+     +------------------+
         |                       |                        |
         v                       v                        v
+------------------+     +------------------+     +------------------+
|  CommandInvoker  |     |  TransaccionFactory|   | ComponenteCategoria|
+------------------+     +------------------+     +------------------+
         |                       |                     /     \
         v                       v                    /       \
+------------------+     +------------------+        /         \
|     Command      |     |OperacionStrategy |       /           \
+------------------+     +------------------+      /             \
    /    |    \               /    |    \         /               \
   /     |     \             /     |     \       /                 \
  v      v      v           v      v      v     v                   v
+------+ +------+ +--------+ +------+ +------+ +------+        +------------+
|Deposito|Retiro|Transferencia|Deposito|Retiro|Transfer|        |CategoriaSimple|
|Command| |Command|Command| |Strategy|Strategy|Strategy|        +------------+
+------+ +------+ +--------+ +------+ +------+ +------+              |
                                                                     |
+------------------+     +------------------+     +------------------+
|  ReporteBuilder  |---->|    Reporte       |<----|  ReporteDirector|
+------------------+     +------------------+     +------------------+
         |
         v
+------------------+
|ReporteConcreteBuilder|
+------------------+
```

## Clases Principales

### Modelo

1. **Usuario**
   - Atributos: nombre, idGeneral, email, telefono, direccion, saldoTotal, cuentasAsociadas, presupuestos, password
   - Métodos: agregarCuenta, eliminarCuenta, buscarCuenta, agregarPresupuesto, eliminarPresupuesto, buscarPresupuestoPorId

2. **Cuenta**
   - Atributos: idCuenta, nombreBanco, tipoCuenta, numeroCuenta, saldo, activa
   - Métodos: depositar, retirar, transferir

3. **TransaccionFactory**
   - Métodos: getIdTransaccion, getFechaTransaccion, getTipoTransaccion, getMonto, getDescripcion, getCategoria

4. **Presupuesto**
   - Atributos: idPresupuesto, nombre, montoTotal, montoGastado, categoriaEspecifica, activo
   - Métodos: registrarGasto, isExcedido, getPorcentajeUso, getMontoDisponible

### Patrones Builder

1. **ReporteBuilder**
   - Métodos: setTitulo, setUsuario, setFechaInicio, setFechaFin, setTransacciones, setCategorias, incluirResumenTransacciones, incluirAnalisisPorCategoria, incluirGraficos, setFormato, build

2. **Reporte**
   - Atributos: titulo, usuario, fechaInicio, fechaFin, transacciones, categorias, incluirResumenTransacciones, incluirAnalisisPorCategoria, incluirGraficos, formato, contenido, rutaArchivo
   - Métodos: generarNombreArchivo

### Patrones Composite

1. **ComponenteCategoria**
   - Métodos: getId, getNombre, setNombre, getDescripcion, setDescripcion, agregarComponente, eliminarComponente, obtenerComponente, esHoja, getComponentes, calcularTotal, contarTransacciones, getRutaCompleta, setPadre, getPadre

2. **CategoriaSimple**
   - Implementa ComponenteCategoria como hoja

3. **GrupoCategoria**
   - Implementa ComponenteCategoria como compuesto
   - Atributos adicionales: componentes
   - Métodos adicionales: obtenerTodasLasHojas

### Patrones Command

1. **Command**
   - Métodos: ejecutar, deshacer, getDescripcion

2. **CommandInvoker**
   - Atributos: comandosEjecutados, comandosDeshecho, listeners
   - Métodos: ejecutarComando, deshacer, rehacer, puedeDeshacer, puedeRehacer, getHistorialComandos, agregarListener, eliminarListener, notificarCambio

## Controladores

1. **SceneController**
   - Gestiona la navegación entre vistas

2. **DashboardController**
   - Controla la vista principal del dashboard

3. **TransaccionesController**
   - Controla la vista de transacciones

4. **EstadisticasController**
   - Controla la vista de estadísticas y gráficos

## Vistas

1. **Bienvenida.fxml**
   - Vista de bienvenida

2. **Sesion.fxml**
   - Vista de inicio de sesión

3. **Registro.fxml**
   - Vista de registro de usuario

4. **Dashboard.fxml**
   - Vista principal del dashboard

5. **Transacciones.fxml**
   - Vista de transacciones

6. **Estadisticas.fxml**
   - Vista de estadísticas y gráficos

## Relaciones Principales

1. **Usuario - Cuenta**: Un usuario puede tener múltiples cuentas (composición).
2. **Usuario - Presupuesto**: Un usuario puede tener múltiples presupuestos (composición).
3. **BilleteraService - CommandInvoker**: BilleteraService utiliza CommandInvoker para ejecutar comandos.
4. **BilleteraService - GestorCategorias**: BilleteraService utiliza GestorCategorias para gestionar categorías.
5. **CommandInvoker - Command**: CommandInvoker ejecuta y gestiona objetos Command.
6. **GrupoCategoria - ComponenteCategoria**: GrupoCategoria contiene múltiples ComponenteCategoria (composición).
7. **ReporteDirector - ReporteBuilder**: ReporteDirector dirige la construcción utilizando un ReporteBuilder.

## Notas de Implementación

1. **Serialización**: Las clases principales implementan Serializable para permitir la persistencia de datos.
2. **Patrones Combinados**: Se han combinado varios patrones para lograr una arquitectura flexible y mantenible.
3. **Extensibilidad**: La arquitectura permite agregar fácilmente nuevos tipos de transacciones, categorías y reportes.
4. **Separación de Responsabilidades**: Se ha aplicado el principio de responsabilidad única en todas las clases.
5. **Interfaces vs Clases Abstractas**: Se han utilizado interfaces para definir contratos y clases abstractas para compartir implementación.

## Conclusiones

El diagrama de clases presentado muestra una arquitectura robusta y flexible, que implementa múltiples patrones de diseño para resolver diferentes aspectos del sistema. La combinación de estos patrones permite una aplicación fácil de mantener, extender y probar.

La implementación de patrones como Command, Composite y Builder proporciona capacidades avanzadas como deshacer/rehacer operaciones, organizar categorías jerárquicamente y crear reportes personalizados, mejorando significativamente la experiencia del usuario y la calidad del software.
