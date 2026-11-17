package co.uniquindio.proyectocinemauq.estructural.decorator;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;

/**
 * Suma $5.000 y 50 puntos de gafas 3D sobre la entrada que creó la fábrica.
 * La fábrica no conoce este extra.
 */
public class Gafas3DDecorator extends EntradaDecorator {

    public Gafas3DDecorator(Comprable entradaEnvuelta) {
        super(entradaEnvuelta);
    }

    @Override
    public String getNombre() {
        return super.getNombre() + " + gafas 3D";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + ReglasPuntos.PRECIO_GAFAS;
    }

    @Override
    public int getPuntosRedencion() {
        return super.getPuntosRedencion() + ReglasPuntos.PUNTOS_GAFAS;
    }
}
