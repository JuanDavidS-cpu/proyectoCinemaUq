package co.uniquindio.proyectocinemauq.comportamiento.strategy;

import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.CuentaPuntos;

import java.time.LocalDateTime;

/**
 * Gasta puntos del cliente, primero el lote que vence antes. No toca la tarjeta.
 */
public class PagoConPuntos implements EstrategiaPago {

    private final CuentaPuntos cuenta;

    public PagoConPuntos(CuentaPuntos cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public void pagar(Compra compra) {
        cuenta.redimir(compra.getPuntosRequeridos(), LocalDateTime.now(), "Compra " + compra.getId());
    }
}
