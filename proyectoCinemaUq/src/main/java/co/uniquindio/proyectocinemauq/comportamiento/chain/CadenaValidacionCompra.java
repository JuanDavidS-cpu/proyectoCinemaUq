package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/**
 * Orden de la cadena: cliente, asientos de la función, monto y medio de pago.
 * Un eslabón nuevo se engancha aquí, sin tocar a los demás.
 */
public class CadenaValidacionCompra {

    private final ValidadorCompra inicio;

    public CadenaValidacionCompra() {
        ValidadorCliente cliente = new ValidadorCliente();
        ValidadorAsientos asientos = new ValidadorAsientos();
        ValidadorMonto monto = new ValidadorMonto();
        ValidadorPago pago = new ValidadorPago();
        cliente.enlazar(asientos).enlazar(monto).enlazar(pago);
        this.inicio = cliente;
    }

    public void validar(Compra compra) {
        inicio.validar(compra);
    }
}
