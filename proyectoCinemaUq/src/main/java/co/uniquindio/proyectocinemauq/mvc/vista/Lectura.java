package co.uniquindio.proyectocinemauq.mvc.vista;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;

import java.time.LocalTime;

public final class Lectura {

    private Lectura() {
    }

    public static int entero(String texto, String error) {
        if (texto == null || texto.isBlank()) {
            throw new CompraInvalidaException(error);
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            throw new CompraInvalidaException(error);
        }
    }

    public static double decimal(String texto, String error) {
        if (texto == null || texto.isBlank()) {
            throw new CompraInvalidaException(error);
        }
        String limpio = texto.trim().replace("$", "").replace(" ", "");
        try {
            if (limpio.matches("\\d{1,3}(\\.\\d{3})+")) {
                limpio = limpio.replace(".", "");
            } else {
                limpio = limpio.replace(',', '.');
            }
            return Double.parseDouble(limpio);
        } catch (NumberFormatException ex) {
            throw new CompraInvalidaException(error);
        }
    }

    public static LocalTime hora(String texto) {
        if (texto == null || !texto.contains(":")) {
            throw new CompraInvalidaException("Escribe la hora como 18:30.");
        }
        String[] partes = texto.trim().split(":");
        if (partes.length != 2) {
            throw new CompraInvalidaException("Escribe la hora como 18:30.");
        }
        int horas = entero(partes[0], "Escribe la hora como 18:30.");
        int minutos = entero(partes[1], "Escribe la hora como 18:30.");
        if (horas < 0 || horas > 23 || minutos < 0 || minutos > 59) {
            throw new CompraInvalidaException("Esa hora no existe. Usa un valor entre 00:00 y 23:59.");
        }
        return LocalTime.of(horas, minutos);
    }
}
