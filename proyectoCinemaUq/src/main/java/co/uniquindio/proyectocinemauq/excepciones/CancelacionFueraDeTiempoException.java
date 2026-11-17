package co.uniquindio.proyectocinemauq.excepciones;

/** El cliente pidió cancelar cuando ya no aplica el reembolso. */
public class CancelacionFueraDeTiempoException extends ExcepcionCinema {

    public CancelacionFueraDeTiempoException(String mensaje) {
        super(mensaje);
    }
}
