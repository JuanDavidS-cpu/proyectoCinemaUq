package co.uniquindio.proyectocinemauq.excepciones;

/** La tarjeta no cubre el cobro y no puede quedar en negativo. */
public class SaldoInsuficienteException extends ExcepcionCinema {

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
