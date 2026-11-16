package co.uniquindio.proyectocinemauq.decorator;


import co.uniquindio.proyectocinemauq.composite.Comprable;

/**
 * Decorador concreto que agrega el alquiler de gafas 3D y su costo extra a una entrada.
 */
public class Gafas3DDecorator extends EntradaDecorator {

    public Gafas3DDecorator(Comprable entradaEnvuelta) {
        super(entradaEnvuelta);
    }

    @Override
    public String getNombre() {
        return super.getNombre() + " + Gafas 3D";
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() + 5000.0;
    }
}
