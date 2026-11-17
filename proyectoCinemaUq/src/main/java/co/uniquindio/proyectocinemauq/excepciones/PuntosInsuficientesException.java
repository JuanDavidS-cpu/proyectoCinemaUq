package co.uniquindio.proyectocinemauq.excepciones;

/** No hay puntos vigentes suficientes. Los vencidos no cuentan. */
public class PuntosInsuficientesException extends ExcepcionCinema {

    public PuntosInsuficientesException(String mensaje) {
        super(mensaje);
    }
}
