package co.uniquindio.proyectocinemauq.excepciones;

/** Recargar, cancelar una función o administrar el cine es solo del administrador. */
public class OperacionSoloAdminException extends ExcepcionCinema {

    public OperacionSoloAdminException(String mensaje) {
        super(mensaje);
    }
}
