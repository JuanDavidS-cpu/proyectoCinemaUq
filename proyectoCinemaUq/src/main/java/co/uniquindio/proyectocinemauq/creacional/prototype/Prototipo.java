package co.uniquindio.proyectocinemauq.creacional.prototype;

/**
 * Cada función necesita sus propias sillas.
 * Clonar evita que dos horarios compartan el mismo asiento y el mismo estado.
 */
public interface Prototipo {
    Prototipo clonar();
}
