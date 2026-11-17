package co.uniquindio.proyectocinemauq.estructural.composite;

import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;

import java.util.ArrayList;
import java.util.List;

/**
 * Combo: se vende como un solo producto y suma lo de adentro con su descuento interno.
 * Ese descuento no se vuelve a aplicar fuera. La promoción del día usa este precio ya calculado.
 * Los puntos del combo salen de ese precio (precio / 100), no de la suma sin descuento.
 */
public class Combo implements Comprable {

    private final String id;
    private final String nombre;
    private final double descuento;
    private final List<Comprable> productos;

    public Combo(String id, String nombre, double descuento) {
        this.id = id;
        this.nombre = nombre;
        this.descuento = descuento;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Comprable producto) {
        if (producto != null) {
            productos.add(producto);
        }
    }

    public String getId() {
        return id;
    }

    public double getDescuento() {
        return descuento;
    }

    public List<Comprable> getProductos() {
        return List.copyOf(productos);
    }

    public String textoContenido() {
        StringBuilder texto = new StringBuilder();
        for (Comprable producto : productos) {
            if (!texto.isEmpty()) {
                texto.append(" + ");
            }
            texto.append(producto.getNombre());
        }
        return texto.toString();
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        double subtotal = 0;
        for (Comprable producto : productos) {
            subtotal += producto.getPrecio();
        }
        return Math.round(subtotal * (1 - descuento));
    }

    @Override
    public int getPuntosRedencion() {
        return ReglasPuntos.porPrecio(getPrecio());
    }

    @Override
    public boolean esEntrada() {
        return false;
    }

    @Override
    public boolean esCombo() {
        return true;
    }

    @Override
    public String toString() {
        int porcentaje = (int) Math.round(descuento * 100);
        return nombre + " · " + Textos.pesos(getPrecio()) + " · " + textoContenido()
                + " · descuento interno " + porcentaje + "%";
    }
}
