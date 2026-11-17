package co.uniquindio.proyectocinemauq.creacional.builder;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;

import java.util.ArrayList;
import java.util.List;

/**
 * Arma la compra por pasos (cliente, función, asientos, ítems) y la entrega con build().
 * No cobra, no aparta sillas y no decide las gafas: eso ya viene en los ítems.
 */
public class CompraBuilder {

    private String id;
    private Cliente cliente;
    private Funcion funcion;
    private List<Asiento> asientos = new ArrayList<>();
    private List<Comprable> items = new ArrayList<>();
    private double descuento;
    private String descripcionPromocion = "";
    private MedioPago medioPago;

    public CompraBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CompraBuilder cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public CompraBuilder funcion(Funcion funcion) {
        this.funcion = funcion;
        return this;
    }

    public CompraBuilder asientos(List<Asiento> asientos) {
        this.asientos = asientos == null ? new ArrayList<>() : new ArrayList<>(asientos);
        return this;
    }

    public CompraBuilder items(List<Comprable> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        return this;
    }

    public CompraBuilder descuento(double descuento, String descripcion) {
        this.descuento = descuento;
        this.descripcionPromocion = descripcion == null ? "" : descripcion;
        return this;
    }

    public CompraBuilder medioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
        return this;
    }

    public Compra build() {
        if (id == null || id.isBlank()) {
            throw new CompraInvalidaException("La compra no tiene número.");
        }
        if (cliente == null) {
            throw new CompraInvalidaException("Falta el cliente de la compra.");
        }
        if (funcion == null) {
            throw new CompraInvalidaException("Elige una función antes de pagar.");
        }
        if (items.isEmpty()) {
            throw new CompraInvalidaException("Agrega al menos una entrada.");
        }
        if (medioPago == null) {
            throw new CompraInvalidaException("Elige si vas a pagar con tarjeta o con puntos.");
        }
        int entradas = 0;
        double subtotal = 0;
        int puntos = 0;
        for (Comprable item : items) {
            subtotal += item.getPrecio();
            puntos += item.getPuntosRedencion();
            if (item.esEntrada()) {
                entradas++;
            }
        }
        subtotal = Math.round(subtotal);
        if (entradas < 1) {
            throw new CompraInvalidaException("Elige al menos un asiento y su entrada.");
        }
        if (asientos.size() != entradas) {
            throw new CompraInvalidaException(
                    "La cantidad de entradas tiene que coincidir con los asientos.");
        }
        double descuentoAplicado = Math.min(Math.max(0, Math.round(descuento)), subtotal);
        double total = subtotal - descuentoAplicado;
        if (total <= 0) {
            throw new CompraInvalidaException("El total quedó en cero. Revisa la promoción.");
        }
        return new Compra(id, cliente, funcion, asientos, items, subtotal, descuentoAplicado,
                descripcionPromocion, total, puntos, medioPago);
    }
}
