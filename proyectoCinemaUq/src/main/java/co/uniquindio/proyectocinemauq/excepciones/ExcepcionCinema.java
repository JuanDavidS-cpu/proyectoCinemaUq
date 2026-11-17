package co.uniquindio.proyectocinemauq.excepciones;

/**
 * Error de una regla del cine. La ventana muestra el mensaje; no es un fallo técnico.
 */
public class ExcepcionCinema extends RuntimeException {

    public ExcepcionCinema(String mensaje) {
        super(mensaje);
    }
}
