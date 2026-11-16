package co.uniquindio.proyectocinemauq.decorator;

import co.uniquindio.proyectocinemauq.composite.Comprable;

public class EntradaBase implements Comprable {
    private double precioBase;

    public EntradaBase(double precioBase) {
        this.precioBase = precioBase;
    }

    @Override
    public String getNombre() {
        return "Entrada General";
    }

    @Override
    public double getPrecio() {
        return precioBase;
    }
}

