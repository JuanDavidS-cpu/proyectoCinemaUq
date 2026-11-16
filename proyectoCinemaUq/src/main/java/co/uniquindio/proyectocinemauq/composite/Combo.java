package co.uniquindio.proyectocinemauq.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Compuesta (Composite).
 * Agrupa multiples objetos Comprable y calcula su precio total aplicando un descuento.
 */
public class Combo implements Comprable {
    private String nombre;
    private List<Comprable> listProductos;
    private double descuento; // Ejemplo: 0.10 para 10%

    public Combo(String nombre, double descuento) {
        this.nombre = nombre;
        this.descuento = descuento;
        this.listProductos = new ArrayList<>();
    }

    /**
     * Agrega un producto individual o sub-combo a la lista del combo.
     */
    public void agregarProducto(Comprable producto) {
        listProductos.add(producto);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * Calcula el precio total recorriendo la lista de productos contenidos.
     */
    @Override
    public double getPrecio() {
        double subtotal = 0;
        for (Comprable p : listProductos) {
            subtotal += p.getPrecio();
        }
        double valorDescuento = subtotal * descuento;
        return subtotal - valorDescuento;
    }
}