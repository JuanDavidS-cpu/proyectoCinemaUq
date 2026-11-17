package co.uniquindio.proyectocinemauq.comportamiento.observer;

/**
 * Quien compró una función recibe el aviso si el cine la cancela.
 * El cliente del modelo implementa esta interfaz; no hay un segundo cliente paralelo.
 */
public interface Observador {
    void actualizar(String mensaje);
}
