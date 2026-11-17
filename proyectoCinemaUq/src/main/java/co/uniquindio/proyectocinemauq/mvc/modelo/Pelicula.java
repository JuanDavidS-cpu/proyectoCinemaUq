package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;

/**
 * Película de la cartelera. La duración se muestra al elegir la función.
 */
public class Pelicula {

    private final String id;
    private final String titulo;
    private final int duracion;
    private final String genero;

    public Pelicula(String id, String titulo, int duracion, String genero) {
        if (titulo == null || titulo.isBlank()) {
            throw new CompraInvalidaException("Escribe el título de la película.");
        }
        if (duracion <= 0) {
            throw new CompraInvalidaException("La duración tiene que ser mayor que cero.");
        }
        if (genero == null || genero.isBlank()) {
            throw new CompraInvalidaException("Escribe el género de la película.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.duracion = duracion;
        this.genero = genero.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getGenero() {
        return genero;
    }

    @Override
    public String toString() {
        return titulo + " · " + duracion + " min · " + genero;
    }
}
