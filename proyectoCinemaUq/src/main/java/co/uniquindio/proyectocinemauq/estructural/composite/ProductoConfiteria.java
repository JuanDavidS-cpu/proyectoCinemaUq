package co.uniquindio.proyectocinemauq.estructural.composite;

import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;

/**
 * Producto suelto de confitería. Entra al carrito como cualquier Comprable.
 * Sus puntos son el precio dividido entre 100.
 */
public class ProductoConfiteria implements Comprable {

    private final String id;
    private final String nombre;
    private final double precio;

    public ProductoConfiteria(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
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
        return ReglasPuntos.porPrecio(precio);
    }

    @Override
    public boolean esEntrada() {
        return false;
    }

    @Override
    public boolean esCombo() {
        return false;
    }

    @Override
    public String toString() {
        return nombre + " · " + Textos.pesos(precio) + " · " + getPuntosRedencion() + " puntos";
    }
}
