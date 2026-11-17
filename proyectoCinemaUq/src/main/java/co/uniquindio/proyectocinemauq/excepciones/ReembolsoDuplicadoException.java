package co.uniquindio.proyectocinemauq.excepciones;

/** Esa compra ya devolvió el dinero o los puntos. */
public class ReembolsoDuplicadoException extends ExcepcionCinema {

    public ReembolsoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
