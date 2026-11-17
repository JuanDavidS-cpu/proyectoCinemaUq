package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/**
 * Un eslabón de la compra. El siguiente se engancha sin reescribir los anteriores.
 */
public abstract class ValidadorCompra {

    private ValidadorCompra siguiente;

    public ValidadorCompra enlazar(ValidadorCompra siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public final void validar(Compra compra) {
        comprobar(compra);
        if (siguiente != null) {
            siguiente.validar(compra);
        }
    }

    protected abstract void comprobar(Compra compra);
}
