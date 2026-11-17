package co.uniquindio.proyectocinemauq.excepciones;

/** Esa silla ya está vendida en la función, o se intentó ocuparla otra vez. */
public class AsientoOcupadoException extends ExcepcionCinema {

    public AsientoOcupadoException(String mensaje) {
        super(mensaje);
    }
}
