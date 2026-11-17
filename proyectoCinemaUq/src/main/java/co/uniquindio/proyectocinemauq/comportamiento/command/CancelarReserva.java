package co.uniquindio.proyectocinemauq.comportamiento.command;

import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/** Suelta la reserva de la misma silla. No toca una silla ya vendida. */
public class CancelarReserva implements Comando {

    private final Asiento asiento;

    public CancelarReserva(Asiento asiento) {
        this.asiento = asiento;
    }

    @Override
    public void ejecutar() {
        asiento.liberarReserva();
    }
}
