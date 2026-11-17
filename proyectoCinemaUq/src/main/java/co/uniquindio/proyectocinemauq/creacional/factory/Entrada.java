package co.uniquindio.proyectocinemauq.creacional.factory;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

/**
 * Producto de la fábrica de entradas. No incluye gafas: eso lo agrega el decorador.
 */
public class Entrada implements Comprable {

    private final String nombre;
    private final double precio;
    private final int puntos;

    public Entrada(String nombre, double precio, int puntos) {
        this.nombre = nombre;
        this.precio = precio;
        this.puntos = puntos;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    @Override
    public int getPuntosRedencion() {
        return puntos;
    }

    @Override
    public boolean esEntrada() {
        return true;
    }

    @Override
    public boolean esCombo() {
        return false;
    }
}
