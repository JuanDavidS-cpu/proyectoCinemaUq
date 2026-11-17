package co.uniquindio.proyectocinemauq.comportamiento.strategy;

import co.uniquindio.proyectocinemauq.estructural.proxy.SujetoTarjeta;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/**
 * Cobra con la tarjeta del cliente a través del proxy. No guarda otro saldo.
 */
public class PagoConTarjeta implements EstrategiaPago {

    private final SujetoTarjeta tarjeta;

    public PagoConTarjeta(SujetoTarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    @Override
    public void pagar(Compra compra) {
        tarjeta.realizarPago(compra.getTotal());
    }
}
