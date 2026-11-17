package co.uniquindio.proyectocinemauq.comportamiento.command;

/**
 * La grilla pide una acción sobre la silla (apartar o soltar) sin conocer el estado.
 */
public interface Comando {
    void ejecutar();
}
