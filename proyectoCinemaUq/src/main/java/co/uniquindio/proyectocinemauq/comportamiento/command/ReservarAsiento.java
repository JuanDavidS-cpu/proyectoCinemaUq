package co.uniquindio.proyectocinemauq.comportamiento.command;

import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;

/** Aparta la silla del modelo, la misma que usa el State. */
public class ReservarAsiento implements Comando {

    private final Asiento asiento;

    public ReservarAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    @Override
    public void ejecutar() {
        asiento.reservar();
    }
}
