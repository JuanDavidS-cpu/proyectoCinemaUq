package co.uniquindio.proyectocinemauq.comportamiento.strategy;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/** Contexto del Strategy. La fachada le pone la estrategia y le pide cobrar. */
public class ProcesadorPago {

    private EstrategiaPago estrategia;

    public void setEstrategia(EstrategiaPago estrategia) {
        this.estrategia = estrategia;
    }

    public void pagar(Compra compra) {
        if (estrategia == null) {
            throw new CompraInvalidaException("Elige tarjeta o puntos para pagar.");
        }
        estrategia.pagar(compra);
    }
}
