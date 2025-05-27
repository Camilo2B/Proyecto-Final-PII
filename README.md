# Billetera Virtual

## Descripción

Billetera Virtual es una aplicación de gestión financiera personal desarrollada como proyecto final para el curso de Patrones de Diseño. La aplicación permite a los usuarios gestionar sus finanzas digitales con funcionalidades como transacciones, presupuestos, categorización de gastos, estadísticas y reportes.

## Características Principales

- **Gestión de Usuarios**: Registro e inicio de sesión de usuarios y administradores.
- **Gestión de Cuentas**: Creación y administración de múltiples cuentas bancarias.
- **Operaciones Financieras**: Depósitos, retiros y transferencias entre cuentas.
- **Presupuestos**: Creación y seguimiento de presupuestos por categoría.
- **Categorización**: Organización jerárquica de categorías para clasificar transacciones.
- **Estadísticas**: Visualización gráfica de gastos e ingresos.
- **Reportes**: Generación de reportes personalizados en diferentes formatos.
- **Historial de Operaciones**: Registro de todas las operaciones con capacidad de deshacer/rehacer.

## Patrones de Diseño Implementados

### Patrones Creacionales

1. **Singleton**
   - Implementado en `DataManager`, `CommandInvoker` y `GestorCategorias`.
   - Garantiza una única instancia de estas clases en toda la aplicación.

2. **Factory Method**
   - Implementado en `TransaccionFactory` y sus subclases.
   - Permite crear diferentes tipos de transacciones de manera flexible.

3. **Builder**
   - Implementado en `ReporteBuilder` y clases relacionadas.
   - Facilita la creación de reportes personalizados con diferentes configuraciones.

### Patrones Estructurales

1. **Decorator**
   - Implementado en `ICuenta` y decoradores relacionados.
   - Permite añadir funcionalidades de seguridad a las cuentas de manera dinámica.

2. **Facade**
   - Implementado en `BilleteraService`.
   - Proporciona una interfaz unificada para los subsistemas complejos.

3. **Composite**
   - Implementado en `ComponenteCategoria` y clases relacionadas.
   - Permite organizar categorías en una estructura jerárquica.

### Patrones de Comportamiento

1. **Strategy**
   - Implementado en `OperacionStrategy` y sus implementaciones.
   - Define una familia de algoritmos para diferentes operaciones financieras.

2. **Command**
   - Implementado en `Command` y sus implementaciones.
   - Encapsula operaciones financieras como objetos, permitiendo deshacer/rehacer.

3. **Observer**
   - Implementado en `CommandInvoker.CommandListener`.
   - Notifica a los observadores sobre cambios en el historial de comandos.

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── co/edu/uniquindio/poo/proyectofinal_billeteravirtual/
│   │       ├── App.java                      # Clase principal de la aplicación
│   │       ├── Model/                        # Clases del modelo
│   │       │   ├── Builder/                  # Patrón Builder para reportes
│   │       │   ├── Command/                  # Patrón Command para operaciones
│   │       │   ├── Composite/                # Patrón Composite para categorías
│   │       │   ├── BilleteraService.java     # Fachada principal
│   │       │   └── ...                       # Otras clases del modelo
│   │       └── ViewController/               # Controladores de vista
│   └── resources/
│       └── co/edu/uniquindio/poo/proyectofinal_billeteravirtual/
│           ├── Bienvenida.fxml               # Vista de bienvenida
│           ├── Dashboard.fxml                # Vista principal
│           ├── Estadisticas.fxml             # Vista de estadísticas
│           └── ...                           # Otras vistas
└── test/
    └── java/
        └── co/edu/uniquindio/poo/proyectofinal_billeteravirtual/
            ├── AllTests.java                 # Suite de pruebas
            ├── Model/                        # Pruebas del modelo
            └── ViewController/               # Pruebas de controladores
```

## Requisitos del Sistema

- Java 21 o superior
- JavaFX 21 o superior
- Maven 3.8 o superior

## Instalación

1. Clonar el repositorio:
   ```
   git clone https://github.com/usuario/billetera-virtual.git
   ```

2. Navegar al directorio del proyecto:
   ```
   cd billetera-virtual
   ```

3. Compilar el proyecto con Maven:
   ```
   mvn clean compile
   ```

4. Ejecutar la aplicación:
   ```
   mvn javafx:run
   ```

## Uso

1. Al iniciar la aplicación, se mostrará la pantalla de bienvenida.
2. Puede registrarse como nuevo usuario o iniciar sesión con credenciales existentes.
3. Una vez iniciada la sesión, accederá al dashboard principal desde donde podrá:
   - Gestionar sus cuentas
   - Realizar transacciones
   - Crear y monitorear presupuestos
   - Visualizar estadísticas
   - Generar reportes
   - Gestionar categorías

Para más detalles, consulte el [Manual de Usuario](docs/MANUAL_USUARIO.md).

## Pruebas

El proyecto incluye pruebas unitarias para verificar el correcto funcionamiento de los componentes principales. Para ejecutar las pruebas:

```
mvn test
```

Para generar un informe de cobertura de código:

```
mvn jacoco:report
```

El informe se generará en `target/site/jacoco/index.html`.

## Documentación

La documentación completa del proyecto incluye:

- [Manual de Usuario](docs/MANUAL_USUARIO.md): Guía detallada para usuarios finales.
- [Diagrama de Clases](docs/DIAGRAMA_CLASES.md): Descripción de la estructura de clases y patrones implementados.
- JavaDoc: Documentación de la API generada a partir de los comentarios en el código.

Para generar la documentación JavaDoc:

```
mvn javadoc:javadoc
```

La documentación se generará en `target/site/apidocs/index.html`.

## Contribución

Si desea contribuir al proyecto, siga estos pasos:

1. Haga un fork del repositorio
2. Cree una rama para su funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Realice sus cambios y haga commit (`git commit -m 'Añadir nueva funcionalidad'`)
4. Haga push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abra un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo [LICENSE](LICENSE) para más detalles.

## Autores

- **Nombre del Estudiante** - *Trabajo inicial* - [usuario](https://github.com/usuario)

## Agradecimientos

- A los profesores del curso de Patrones de Diseño por su guía y enseñanzas.
- A la Universidad del Quindío por proporcionar los recursos necesarios para el desarrollo del proyecto.
- A todos los compañeros que contribuyeron con ideas y sugerencias.
