package co.uniquindio.proyectocinemauq.excepciones;

/** La tarjeta existe, pero el cine la tiene apagada y no puede cobrar. */
public class TarjetaInactivaException extends ExcepcionCinema {

    public TarjetaInactivaException(String mensaje) {
        super(mensaje);
    }
}
