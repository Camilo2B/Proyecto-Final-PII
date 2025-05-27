@echo off
echo ===================================================
echo Instalador de Billetera Virtual
echo ===================================================
echo.
echo Este script instalara la aplicacion Billetera Virtual en su sistema.
echo.
echo Requisitos:
echo - Java 21 o superior
echo - Maven 3.8 o superior
echo.
echo Presione cualquier tecla para continuar o Ctrl+C para cancelar...
pause > nul

echo.
echo Compilando la aplicacion...
call mvn clean package

echo.
echo Creando imagen de tiempo de ejecucion...
call mvn javafx:jlink

echo.
echo Creando instalador...
call mvn jpackage:jpackage

echo.
echo Instalacion completada. El instalador se encuentra en la carpeta target/installer.
echo.
echo Presione cualquier tecla para salir...
pause > nul
