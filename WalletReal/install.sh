#!/bin/bash

echo "==================================================="
echo "Instalador de Billetera Virtual"
echo "==================================================="
echo
echo "Este script instalará la aplicación Billetera Virtual en su sistema."
echo
echo "Requisitos:"
echo "- Java 21 o superior"
echo "- Maven 3.8 o superior"
echo
read -p "Presione Enter para continuar o Ctrl+C para cancelar..."

echo
echo "Compilando la aplicación..."
mvn clean package

echo
echo "Creando imagen de tiempo de ejecución..."
mvn javafx:jlink

echo
echo "Creando instalador..."
mvn jpackage:jpackage

echo
echo "Instalación completada. El instalador se encuentra en la carpeta target/installer."
echo
read -p "Presione Enter para salir..."
