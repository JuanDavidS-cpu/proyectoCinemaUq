package co.uniquindio.proyectocinemauq.decorator;


import co.uniquindio.proyectocinemauq.composite.Comprable;

/**
 * Clase abstracta Decorador (Decorator).
 * Envuelve un objeto Comprable para añadirle responsabilidades dinamicamente.
 */
public abstract class EntradaDecorator implements Comprable {
    protected Comprable entradaEnvuelta;

    public EntradaDecorator(Comprable entradaEnvuelta) {
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
}
