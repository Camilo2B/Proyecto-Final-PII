# Manual de Usuario - Billetera Virtual

## Índice
1. [Introducción](#introducción)
2. [Requisitos del Sistema](#requisitos-del-sistema)
3. [Instalación](#instalación)
4. [Inicio de Sesión](#inicio-de-sesión)
5. [Registro de Usuario](#registro-de-usuario)
6. [Dashboard Principal](#dashboard-principal)
7. [Gestión de Cuentas](#gestión-de-cuentas)
8. [Transacciones](#transacciones)
   - [Depósitos](#depósitos)
   - [Retiros](#retiros)
   - [Transferencias](#transferencias)
9. [Presupuestos](#presupuestos)
10. [Categorías](#categorías)
11. [Estadísticas](#estadísticas)
12. [Reportes](#reportes)
13. [Historial de Operaciones](#historial-de-operaciones)
14. [Preguntas Frecuentes](#preguntas-frecuentes)
15. [Solución de Problemas](#solución-de-problemas)

## Introducción

Billetera Virtual es una aplicación de gestión financiera personal que le permite administrar sus finanzas de manera eficiente y segura. Con esta aplicación, puede realizar un seguimiento de sus ingresos y gastos, crear presupuestos, categorizar transacciones, visualizar estadísticas y generar reportes detallados.

La aplicación ha sido diseñada con una interfaz intuitiva y fácil de usar, implementando patrones de diseño que garantizan su robustez, flexibilidad y mantenibilidad.

## Requisitos del Sistema

Para ejecutar la aplicación Billetera Virtual, su sistema debe cumplir con los siguientes requisitos:

- **Sistema Operativo**: Windows 10/11, macOS 10.14 o superior, o Linux (distribuciones recientes)
- **Java**: JDK 21 o superior
- **Memoria RAM**: Mínimo 4 GB (recomendado 8 GB)
- **Espacio en Disco**: 100 MB de espacio libre
- **Resolución de Pantalla**: Mínimo 1280x720

## Instalación

1. Descargue el archivo de instalación desde el repositorio oficial.
2. Extraiga el contenido del archivo ZIP en la ubicación deseada.
3. Ejecute el archivo `BilleteraVirtual.jar` haciendo doble clic o mediante el comando:
   ```
   java -jar BilleteraVirtual.jar
   ```

## Inicio de Sesión

1. Al iniciar la aplicación, se mostrará la pantalla de bienvenida.
2. Haga clic en "Iniciar Sesión".
3. Ingrese su ID de usuario y contraseña.
4. Seleccione su rol (Usuario o Administrador).
5. Haga clic en "Ingresar".

Si ha olvidado su contraseña, contacte al administrador del sistema para restablecerla.

## Registro de Usuario

Si es la primera vez que utiliza la aplicación, deberá registrarse:

1. En la pantalla de bienvenida, haga clic en "Registrarse".
2. Complete el formulario con sus datos personales:
   - Nombre completo
   - ID de usuario (único)
   - Correo electrónico
   - Teléfono
   - Dirección
   - Contraseña
3. Haga clic en "Registrar".
4. Una vez registrado, podrá iniciar sesión con sus credenciales.

## Dashboard Principal

El dashboard principal es el centro de control de la aplicación, desde donde puede acceder a todas las funcionalidades:

- **Resumen de Saldo**: Muestra su saldo total y el saldo de cada cuenta.
- **Transacciones Recientes**: Lista las últimas transacciones realizadas.
- **Presupuestos Activos**: Muestra el estado de sus presupuestos actuales.
- **Accesos Rápidos**: Botones para acceder rápidamente a las funciones más utilizadas.
- **Menú de Navegación**: Permite acceder a todas las secciones de la aplicación.

## Gestión de Cuentas

### Crear una Nueva Cuenta

1. En el dashboard, haga clic en "Cuentas" en el menú de navegación.
2. Haga clic en "Nueva Cuenta".
3. Complete el formulario con los datos de la cuenta:
   - Nombre del banco
   - Tipo de cuenta (Ahorro, Corriente, etc.)
   - Número de cuenta
   - Saldo inicial (opcional)
4. Haga clic en "Crear Cuenta".

### Editar una Cuenta

1. En la sección "Cuentas", seleccione la cuenta que desea editar.
2. Haga clic en el ícono de edición (lápiz).
3. Modifique los datos necesarios.
4. Haga clic en "Guardar Cambios".

### Eliminar una Cuenta

1. En la sección "Cuentas", seleccione la cuenta que desea eliminar.
2. Haga clic en el ícono de eliminación (papelera).
3. Confirme la eliminación en el diálogo de confirmación.

**Nota**: No se puede eliminar una cuenta con saldo positivo. Debe transferir o retirar el saldo antes de eliminarla.

## Transacciones

### Depósitos

Para realizar un depósito:

1. En el dashboard, haga clic en "Nueva Transacción" o vaya a la sección "Transacciones".
2. Seleccione "Depósito" como tipo de transacción.
3. Seleccione la cuenta donde desea depositar (opcional).
4. Ingrese el monto a depositar.
5. Agregue una descripción (opcional).
6. Seleccione una categoría (opcional).
7. Haga clic en "Realizar Depósito".

### Retiros

Para realizar un retiro:

1. En el dashboard, haga clic en "Nueva Transacción" o vaya a la sección "Transacciones".
2. Seleccione "Retiro" como tipo de transacción.
3. Seleccione la cuenta de donde desea retirar (opcional).
4. Ingrese el monto a retirar.
5. Agregue una descripción (opcional).
6. Seleccione una categoría (opcional).
7. Haga clic en "Realizar Retiro".

### Transferencias

Para realizar una transferencia:

1. En el dashboard, haga clic en "Nueva Transacción" o vaya a la sección "Transacciones".
2. Seleccione "Transferencia" como tipo de transacción.
3. Seleccione la cuenta de origen.
4. Seleccione la cuenta de destino.
5. Ingrese el monto a transferir.
6. Agregue una descripción (opcional).
7. Seleccione una categoría (opcional).
8. Haga clic en "Realizar Transferencia".

## Presupuestos

### Crear un Presupuesto

1. En el dashboard, haga clic en "Presupuestos" en el menú de navegación.
2. Haga clic en "Nuevo Presupuesto".
3. Complete el formulario:
   - Nombre del presupuesto
   - Monto total asignado
   - Categoría específica (opcional)
   - Fecha de inicio y fin (opcional)
4. Haga clic en "Crear Presupuesto".

### Monitorear Presupuestos

En la sección "Presupuestos", puede ver el estado de todos sus presupuestos:
- Porcentaje de uso
- Monto gastado vs. monto asignado
- Estado (Normal, Advertencia, Excedido)

### Editar o Eliminar Presupuestos

1. Seleccione el presupuesto que desea modificar.
2. Haga clic en el ícono correspondiente (editar o eliminar).
3. Realice los cambios necesarios o confirme la eliminación.

## Categorías

### Gestionar Categorías

1. En el menú de navegación, haga clic en "Categorías".
2. Verá la estructura jerárquica de categorías.

### Crear Categorías

1. Haga clic en "Nueva Categoría" para crear una categoría simple.
2. Haga clic en "Nuevo Grupo" para crear un grupo de categorías.
3. Complete el formulario con el nombre y descripción.
4. Si está creando una categoría simple, seleccione el grupo al que pertenecerá (opcional).
5. Haga clic en "Crear".

### Organizar Categorías

Puede arrastrar y soltar categorías para reorganizarlas dentro de la estructura jerárquica.

## Estadísticas

La sección de estadísticas proporciona visualizaciones gráficas de sus datos financieros:

1. En el menú de navegación, haga clic en "Estadísticas".
2. Seleccione el tipo de gráfico que desea ver:
   - Gráfico de barras (gastos por categoría)
   - Gráfico circular (distribución de gastos)
   - Gráfico de línea (tendencias de gastos e ingresos)
3. Utilice los filtros para ajustar el período de tiempo:
   - Última semana
   - Último mes
   - Últimos 3 meses
   - Último año
   - Personalizado (seleccione fechas específicas)

## Reportes

### Generar Reportes

1. En el menú de navegación, haga clic en "Reportes".
2. Seleccione el tipo de reporte:
   - Reporte completo
   - Resumen financiero
   - Análisis por categorías
3. Configure las opciones del reporte:
   - Período de tiempo
   - Incluir resumen de transacciones
   - Incluir análisis por categoría
   - Incluir gráficos
   - Formato de salida (PDF, CSV, etc.)
4. Haga clic en "Generar Reporte".

### Exportar Reportes

Una vez generado el reporte, puede:
- Guardarlo en su dispositivo
- Imprimirlo
- Enviarlo por correo electrónico

## Historial de Operaciones

La aplicación mantiene un historial de todas las operaciones realizadas, permitiéndole deshacer o rehacer acciones:

1. En el menú de navegación, haga clic en "Historial".
2. Verá una lista de todas las operaciones realizadas.
3. Para deshacer una operación, haga clic en el botón "Deshacer" junto a la operación.
4. Para rehacer una operación deshecha, haga clic en el botón "Rehacer".

## Preguntas Frecuentes

### ¿Cómo puedo recuperar mi contraseña?
Actualmente, debe contactar al administrador del sistema para restablecer su contraseña.

### ¿Puedo acceder a mi cuenta desde diferentes dispositivos?
Sí, puede acceder a su cuenta desde cualquier dispositivo que tenga la aplicación instalada.

### ¿Mis datos están seguros?
Sí, la aplicación utiliza técnicas de encriptación para proteger sus datos financieros.

### ¿Puedo exportar mis datos?
Sí, puede exportar sus datos en varios formatos a través de la sección de Reportes.

## Solución de Problemas

### La aplicación no inicia
- Verifique que tiene instalada la versión correcta de Java.
- Asegúrese de tener suficiente espacio en disco.
- Intente ejecutar la aplicación como administrador.

### No puedo realizar una transacción
- Verifique que tiene saldo suficiente para la operación.
- Asegúrese de que la cuenta está activa.
- Compruebe que ha completado todos los campos obligatorios.

### Los gráficos no se muestran correctamente
- Actualice la página haciendo clic en el botón de actualizar.
- Intente seleccionar un período de tiempo diferente.
- Reinicie la aplicación si el problema persiste.

---

Para obtener más ayuda, contacte al soporte técnico en support@billeteravirtual.com o visite nuestro sitio web www.billeteravirtual.com.
