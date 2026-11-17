package co.uniquindio.proyectocinemauq.comportamiento.state;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/** Silla apartada para la compra en curso. Se puede soltar o confirmar la venta. */
public class Reservado implements EstadoAsiento {

    @Override
    public void reservar(Asiento asiento) {
        throw new CompraInvalidaException("El asiento " + asiento.getCodigo() + " ya está apartado.");
    }

    @Override
    public void ocupar(Asiento asiento) {
        asiento.cambiarEstado(new Ocupado());
    }

    @Override
    public void liberarReserva(Asiento asiento) {
        asiento.cambiarEstado(new Disponible());
    }

    @Override
    public void liberarPorReembolso(Asiento asiento) {
        asiento.cambiarEstado(new Disponible());
    }

    @Override
    public String getNombre() {
        return "Reservado";
    }
}
