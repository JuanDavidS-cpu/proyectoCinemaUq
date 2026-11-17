package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;

import java.util.ArrayList;
import java.util.List;

/**
 * Plantilla de sillas. Al crear una función se clonan; la plantilla no se vende.
 */
public class Sala {

    private final String id;
    private final String nombre;
    private final int filas;
    private final int columnas;
    private final List<Asiento> plantilla;

    public Sala(String id, String nombre, int filas, int columnas) {
        if (nombre == null || nombre.isBlank()) {
            throw new CompraInvalidaException("Escribe el nombre de la sala.");
        }
        if (filas < 1 || filas > 12 || columnas < 1 || columnas > 12) {
            throw new CompraInvalidaException("Usa entre 1 y 12 filas y columnas.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.filas = filas;
        this.columnas = columnas;
        this.plantilla = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            String letra = String.valueOf((char) ('A' + f));
            for (int c = 1; c <= columnas; c++) {
                plantilla.add(new Asiento(letra, c));
            }
        }
    }

    public List<Asiento> clonarPlantilla() {
        List<Asiento> copia = new ArrayList<>();
        for (Asiento asiento : plantilla) {
            copia.add(asiento.clonar());
        }
        return copia;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCupo() {
        return filas * columnas;
    }

    @Override
    public String toString() {
        return nombre + " · " + filas + " filas x " + columnas + " columnas";
    }
}
