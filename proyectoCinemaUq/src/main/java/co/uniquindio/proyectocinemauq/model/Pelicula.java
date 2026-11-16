package co.uniquindio.proyectocinemauq.model;

/**
 * Representa una pelicula en cartelera.
 */
public class Pelicula {
    private String id;
    private String titulo;
    private int duracion;

    public Pelicula(String id, String titulo, int duracion) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}
