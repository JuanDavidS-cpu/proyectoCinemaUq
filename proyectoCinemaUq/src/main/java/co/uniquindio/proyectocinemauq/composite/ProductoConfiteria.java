package co.uniquindio.proyectocinemauq.composite;

/**
 * Clase Hoja (Leaf) del patron Composite.
 * Representa un producto individual de la confiteria (ej. crispetas, gaseosa).
 */
public class ProductoConfiteria implements Comprable {
    private String nombre;
    private double precio;

    public ProductoConfiteria(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }
}