package co.uniquindio.proyectocinemauq.comportamiento.state;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/** Silla libre: se puede apartar o vender. */
public class Disponible implements EstadoAsiento {

    @Override
    public void reservar(Asiento asiento) {
        asiento.cambiarEstado(new Reservado());
    }

    @Override
    public void ocupar(Asiento asiento) {
        asiento.cambiarEstado(new Ocupado());
    }

    @Override
    public void liberarReserva(Asiento asiento) {
        throw new CompraInvalidaException("El asiento " + asiento.getCodigo() + " ya está libre.");
    }

    @Override
    public void liberarPorReembolso(Asiento asiento) {
        throw new CompraInvalidaException(
                "El asiento " + asiento.getCodigo() + " no tiene una venta que devolver.");
    }

    @Override
    public String getNombre() {
        return "Disponible";
    }
}
