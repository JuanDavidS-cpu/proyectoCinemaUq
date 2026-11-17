package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/** Un solo motivo: la cuenta del cliente tiene que existir y estar activa. */
public class ValidadorCliente extends ValidadorCompra {

    @Override
    protected void comprobar(Compra compra) {
        if (compra.getCliente() == null) {
            throw new CompraInvalidaException("No hay un cliente para esta compra.");
        }
        if (!compra.getCliente().isActivo()) {
            throw new CompraInvalidaException("Tu cuenta está inactiva. Pide ayuda en taquilla.");
        }
    }
}
