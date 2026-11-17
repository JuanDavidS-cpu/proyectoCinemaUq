package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/** El total tiene que ser un valor cobrable y coincidir con los ítems menos la promoción. */
public class ValidadorMonto extends ValidadorCompra {

    @Override
    protected void comprobar(Compra compra) {
        if (!Double.isFinite(compra.getTotal()) || compra.getTotal() <= 0) {
            throw new CompraInvalidaException("El total de la compra no es válido.");
        }
        if (compra.getDescuentoPromocion() < 0) {
            throw new CompraInvalidaException("El descuento de la promoción no es válido.");
        }
        double suma = 0;
        for (Comprable item : compra.getItems()) {
            suma += item.getPrecio();
        }
        double esperado = Math.round(suma) - Math.round(compra.getDescuentoPromocion());
        if (Math.abs(esperado - compra.getTotal()) > 1) {
            throw new CompraInvalidaException(
                    "El total de la compra no coincide. Vuelve al resumen e inténtalo otra vez.");
        }
    }
}
