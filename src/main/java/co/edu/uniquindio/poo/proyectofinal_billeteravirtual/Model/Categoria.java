package co.edu.uniquindio.poo.proyectofinal_billeteravirtual.Model;

import java.io.Serializable;

public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idCategoria;
    private String nombre;
    private String descripcion;

    /**
     * Constructor sin parámetros para serialización
     */
    public Categoria() {
        this.idCategoria = "";
        this.nombre = "";
        this.descripcion = "";
    }

    public Categoria(String idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "idCategoria='" + idCategoria + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
