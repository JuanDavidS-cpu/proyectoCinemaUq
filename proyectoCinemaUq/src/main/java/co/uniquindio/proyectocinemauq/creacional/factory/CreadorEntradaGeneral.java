package co.uniquindio.proyectocinemauq.creacional.factory;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;

/** Entrada general: $12.000 y 150 puntos. */
public class CreadorEntradaGeneral extends CreadorEntrada {

    @Override
    public Comprable crearEntrada() {
        return new Entrada(
                "Entrada general",
                ReglasPuntos.PRECIO_ENTRADA_GENERAL,
                ReglasPuntos.PUNTOS_ENTRADA_GENERAL);
    }
}
