package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Datos de un reporte, antes de elegir si se guardan en TXT o se simula un PDF. */
public class Reporte {

    private final String titulo;
    private final LocalDateTime fechaGeneracion;
    private final LocalDate desde;
    private final LocalDate hasta;
    private final List<String> registros;
    private final String totales;

    public Reporte(String titulo, LocalDate desde, LocalDate hasta, List<String> registros, String totales) {
        this.titulo = titulo;
        this.fechaGeneracion = LocalDateTime.now();
        this.desde = desde;
        this.hasta = hasta;
        this.registros = List.copyOf(registros);
        this.totales = totales;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public List<String> getRegistros() {
        return registros;
    }

    public String getTotales() {
        return totales;
    }
}
