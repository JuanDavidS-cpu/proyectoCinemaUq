package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.LocalDateTime;

public class MovimientoPuntos {

    private final LocalDateTime fecha;
    private final TipoMovimientoPuntos tipo;
    private final int cantidad;
    private final String descripcion;

    public MovimientoPuntos(LocalDateTime fecha, TipoMovimientoPuntos tipo, int cantidad, String descripcion) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimientoPuntos getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
