package co.uniquindio.proyectocinemauq.creacional.factory;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;

/** Entrada preferencial: $18.000 y 250 puntos. */
public class CreadorEntradaPreferencial extends CreadorEntrada {

    @Override
    public Comprable crearEntrada() {
        return new Entrada(
                "Entrada preferencial",
                ReglasPuntos.PRECIO_ENTRADA_PREFERENCIAL,
                ReglasPuntos.PUNTOS_ENTRADA_PREFERENCIAL);
    }
}
