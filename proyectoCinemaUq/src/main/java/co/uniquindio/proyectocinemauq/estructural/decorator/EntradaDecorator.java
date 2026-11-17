package co.uniquindio.proyectocinemauq.estructural.decorator;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

/**
 * Envuelve una entrada para sumarle algo, sin reescribir la fábrica.
 */
public abstract class EntradaDecorator implements Comprable {

    protected final Comprable entradaEnvuelta;

    protected EntradaDecorator(Comprable entradaEnvuelta) {
        this.entradaEnvuelta = entradaEnvuelta;
    }

    @Override
    public String getNombre() {
        return entradaEnvuelta.getNombre();
    }

    @Override
    public double getPrecio() {
        return entradaEnvuelta.getPrecio();
    }

    @Override
    public int getPuntosRedencion() {
        return entradaEnvuelta.getPuntosRedencion();
    }

    @Override
    public boolean esEntrada() {
        return entradaEnvuelta.esEntrada();
    }

    @Override
    public boolean esCombo() {
        return entradaEnvuelta.esCombo();
    }
}
