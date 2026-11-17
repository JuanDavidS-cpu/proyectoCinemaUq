package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Textos compartidos para que la ventana y el archivo .txt digan lo mismo. */
public final class Textos {

    private static final Locale ESPANOL = Locale.forLanguageTag("es");
    private static final DateTimeFormatter FECHA_HORA =
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy, h:mm a", ESPANOL);
    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", ESPANOL);
    private static final DateTimeFormatter FECHA_CORTA =
            DateTimeFormatter.ofPattern("d MMM yyyy", ESPANOL);

    private Textos() {
    }

    public static String pesos(double valor) {
        long pesos = Math.round(valor);
        boolean negativo = pesos < 0;
        String digitos = Long.toString(Math.abs(pesos));
        StringBuilder grupos = new StringBuilder();
        int cuenta = 0;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            if (cuenta > 0 && cuenta % 3 == 0) {
                grupos.append('.');
            }
            grupos.append(digitos.charAt(i));
            cuenta++;
        }
        grupos.reverse();
        return (negativo ? "-$" : "$") + grupos;
    }

    public static String puntos(int cantidad) {
        return cantidad + (cantidad == 1 ? " punto" : " puntos");
    }

    public static String fechaHora(LocalDateTime fecha) {
        return FECHA_HORA.format(fecha);
    }

    public static String fecha(LocalDateTime fecha) {
        return FECHA.format(fecha);
    }

    public static String fecha(LocalDate fecha) {
        return FECHA.format(fecha);
    }

    public static String fechaCorta(LocalDate fecha) {
        return FECHA_CORTA.format(fecha);
    }

    public static String dia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
