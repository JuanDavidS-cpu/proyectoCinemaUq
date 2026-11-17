package co.uniquindio.proyectocinemauq.excepciones;

/** La compra llegó incompleta o con un dato que el cine no puede aceptar. */
public class CompraInvalidaException extends ExcepcionCinema {

    public CompraInvalidaException(String mensaje) {
        super(mensaje);
    }
}
