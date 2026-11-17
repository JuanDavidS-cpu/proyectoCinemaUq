package co.uniquindio.proyectocinemauq.creacional.factory;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

/**
 * La taquilla pide "una entrada" y la fábrica concreta decide el tipo.
 * Un tipo nuevo es otra fábrica. Esta clase no conoce las gafas 3D.
 */
public abstract class CreadorEntrada {
    public abstract Comprable crearEntrada();
}
