package co.uniquindio.proyectocinemauq.comportamiento.state;

import co.uniquindio.proyectocinemauq.excepciones.AsientoOcupadoException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/**
 * Silla vendida. Un segundo ocupar se rechaza.
 * Solo vuelve a disponible con cancelación o reembolso explícito.
 */
public class Ocupado implements EstadoAsiento {

    @Override
    public void reservar(Asiento asiento) {
        throw new AsientoOcupadoException("El asiento " + asiento.getCodigo() + " ya está ocupado.");
    }

    @Override
    public void ocupar(Asiento asiento) {
        throw new AsientoOcupadoException("El asiento " + asiento.getCodigo() + " ya está vendido.");
    }

    @Override
    public void liberarReserva(Asiento asiento) {
        throw new AsientoOcupadoException(
                "El asiento " + asiento.getCodigo()
                        + " ya está vendido. Solo se libera si la compra se cancela o se reembolsa.");
    }

    @Override
    public void liberarPorReembolso(Asiento asiento) {
        asiento.cambiarEstado(new Disponible());
    }

    @Override
    public String getNombre() {
        return "Ocupado";
    }
}
