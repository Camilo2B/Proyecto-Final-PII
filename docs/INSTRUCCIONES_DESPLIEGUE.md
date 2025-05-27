# Instrucciones de Despliegue - Billetera Virtual

Este documento proporciona instrucciones detalladas para compilar, empaquetar y desplegar la aplicación Billetera Virtual en diferentes sistemas operativos.

## Requisitos Previos

Antes de comenzar, asegúrese de tener instalado lo siguiente:

- **Java Development Kit (JDK)** 21 o superior
- **Maven** 3.8 o superior
- **Git** (opcional, para clonar el repositorio)

## Opciones de Despliegue

La aplicación Billetera Virtual puede desplegarse de varias maneras:

1. **Ejecución directa desde Maven**: Útil durante el desarrollo
2. **JAR ejecutable**: Portable, pero requiere Java instalado
3. **Instalador nativo**: Crea un instalador específico para cada sistema operativo

## 1. Ejecución desde Maven

Esta es la forma más sencilla de ejecutar la aplicación durante el desarrollo:

```bash
# Navegar al directorio del proyecto
cd ProyectoFinal_BilleteraVirtual

# Ejecutar la aplicación
mvn clean javafx:run
```

## 2. Creación de JAR Ejecutable

Para crear un JAR ejecutable que incluya todas las dependencias:

```bash
# Navegar al directorio del proyecto
cd ProyectoFinal_BilleteraVirtual

# Compilar y empaquetar
mvn clean package
```

El JAR ejecutable se generará en `target/ProyectoFinal_BilleteraVirtual-1.0-SNAPSHOT-fat.jar`.

Para ejecutar el JAR:

```bash
java -jar target/ProyectoFinal_BilleteraVirtual-1.0-SNAPSHOT-fat.jar
```

## 3. Creación de Instaladores Nativos

### Usando los Scripts de Instalación

Hemos proporcionado scripts para facilitar la creación de instaladores:

#### Windows

1. Abra una terminal de PowerShell o CMD
2. Navegue al directorio del proyecto
3. Ejecute el script de instalación:

```
.\install.bat
```

#### Linux/macOS

1. Abra una terminal
2. Navegue al directorio del proyecto
3. Asegúrese de que el script tiene permisos de ejecución:

```bash
chmod +x install.sh
```

4. Ejecute el script:

```bash
./install.sh
```

### Creación Manual de Instaladores

Si prefiere crear los instaladores manualmente:

#### Paso 1: Compilar y Empaquetar

```bash
mvn clean package
```

#### Paso 2: Crear Imagen de Tiempo de Ejecución

```bash
mvn javafx:jlink
```

#### Paso 3: Crear Instalador

```bash
mvn jpackage:jpackage
```

Los instaladores se generarán en la carpeta `target/installer`.

## Estructura de los Instaladores

Los instaladores generados tendrán la siguiente estructura:

### Windows

- **BilleteraVirtual-1.0.0.exe**: Instalador ejecutable
- **BilleteraVirtual-1.0.0.msi**: Paquete de instalación MSI

### macOS

- **BilleteraVirtual-1.0.0.dmg**: Imagen de disco para instalación
- **BilleteraVirtual-1.0.0.pkg**: Paquete de instalación

### Linux

- **billeteravirtual_1.0.0-1_amd64.deb**: Paquete Debian (Ubuntu, Debian)
- **billeteravirtual-1.0.0-1.x86_64.rpm**: Paquete RPM (Fedora, CentOS)

## Instalación en Diferentes Sistemas Operativos

### Windows

1. Ejecute el archivo `.exe` o `.msi`
2. Siga las instrucciones del asistente de instalación
3. La aplicación se instalará en `C:\Program Files\BilleteraVirtual` por defecto
4. Se creará un acceso directo en el escritorio y en el menú de inicio

### macOS

1. Monte el archivo `.dmg` haciendo doble clic en él
2. Arrastre la aplicación a la carpeta Aplicaciones
3. Alternativamente, ejecute el archivo `.pkg` y siga el asistente de instalación

### Linux

#### Debian/Ubuntu

```bash
sudo dpkg -i billeteravirtual_1.0.0-1_amd64.deb
sudo apt-get install -f  # Para resolver dependencias si es necesario
```

#### Fedora/CentOS

```bash
sudo rpm -i billeteravirtual-1.0.0-1.x86_64.rpm
```

## Solución de Problemas

### Error: "Java no encontrado"

Asegúrese de tener instalado JDK 21 o superior y que esté correctamente configurado en la variable de entorno PATH.

### Error: "Maven no encontrado"

Asegúrese de tener instalado Maven 3.8 o superior y que esté correctamente configurado en la variable de entorno PATH.

### Error: "No se puede crear el instalador"

- Verifique que tiene instalado JDK 21 o superior
- En Windows, asegúrese de tener instalado WiX Toolset para crear instaladores MSI
- En macOS, asegúrese de tener instalado Xcode Command Line Tools
- En Linux, asegúrese de tener instalado fakeroot y rpmbuild

## Notas Adicionales

- Los instaladores incluyen un JRE personalizado, por lo que no es necesario tener Java instalado para ejecutar la aplicación una vez instalada.
- La aplicación se configurará para iniciarse automáticamente al iniciar el sistema operativo.
- Los datos de la aplicación se almacenan en:
  - Windows: `%APPDATA%\BilleteraVirtual`
  - macOS: `~/Library/Application Support/BilleteraVirtual`
  - Linux: `~/.billeteravirtual`

## Soporte

Si encuentra algún problema durante el despliegue, por favor contacte al equipo de desarrollo en support@billeteravirtual.com o abra un issue en el repositorio del proyecto.
