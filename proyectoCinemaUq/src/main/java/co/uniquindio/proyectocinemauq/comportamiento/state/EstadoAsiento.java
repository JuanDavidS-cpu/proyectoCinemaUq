package co.uniquindio.proyectocinemauq.comportamiento.state;

import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/**
 * La silla cambia de comportamiento según esté libre, apartada o vendida.
 * Ocupar dos veces no libera. El reembolso sí devuelve la silla a disponible.
 */
public interface EstadoAsiento {
    void reservar(Asiento asiento);

    void ocupar(Asiento asiento);

    void liberarReserva(Asiento asiento);

    void liberarPorReembolso(Asiento asiento);

    String getNombre();
}
