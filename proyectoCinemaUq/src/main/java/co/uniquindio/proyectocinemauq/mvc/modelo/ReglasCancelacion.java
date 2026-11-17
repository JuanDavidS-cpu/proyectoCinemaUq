package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.excepciones.CancelacionFueraDeTiempoException;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Cancelación del cliente, según lo que falta para la función:
 * más de 24 horas: reembolso del 100%;
 * desde 2 horas hasta 24 horas inclusive: reembolso del 80%;
 * menos de 2 horas, o si la función ya pasó: no se permite.
 * Si el administrador cancela la función, el reembolso es siempre del 100% y no usa esta tabla.
 */
public final class ReglasCancelacion {

    private static final long MINUTOS_24H = 24 * 60;
    private static final long MINUTOS_2H = 2 * 60;

    private ReglasCancelacion() {
    }

    public static double porcentaje(LocalDateTime ahora, LocalDateTime funcion) {
        long minutos = minutosQueFaltan(ahora, funcion);
        if (minutos < 0) {
            throw new CancelacionFueraDeTiempoException(
                    "La función ya pasó, así que esta compra no se puede cancelar.");
        }
        if (minutos < MINUTOS_2H) {
            throw new CancelacionFueraDeTiempoException(
                    "Faltan menos de 2 horas para la función, así que ya no se puede cancelar.");
        }
        if (minutos > MINUTOS_24H) {
            return 1.0;
        }
        return 0.8;
    }

    public static String frase(LocalDateTime ahora, LocalDateTime funcion) {
        long minutos = minutosQueFaltan(ahora, funcion);
        if (minutos > MINUTOS_24H) {
            return "Faltan más de 24 horas para la función. Te devolvemos el 100%.";
        }
        return "Faltan entre 2 y 24 horas para la función. Te devolvemos el 80%.";
    }

    private static long minutosQueFaltan(LocalDateTime ahora, LocalDateTime funcion) {
        return Duration.between(ahora, funcion).toMinutes();
    }
}
