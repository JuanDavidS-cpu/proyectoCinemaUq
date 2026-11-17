package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.LocalDateTime;

/** Recarga, cobro o devolución de la tarjeta, con fecha. */
public class MovimientoTarjeta {

    private final LocalDateTime fecha;
    private final double valor;
    private final String descripcion;
    private final TipoMovimiento tipo;

    public MovimientoTarjeta(LocalDateTime fecha, double valor, String descripcion, TipoMovimiento tipo) {
        this.fecha = fecha;
        this.valor = valor;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getValor() {
        return valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public String texto() {
        String signo = tipo == TipoMovimiento.COMPRA ? "-" : "+";
        return tipoTexto() + " · " + signo + Textos.pesos(valor) + " · " + descripcion
                + " · " + Textos.fechaHora(fecha);
    }

    private String tipoTexto() {
        return switch (tipo) {
            case RECARGA -> "Recarga";
            case COMPRA -> "Compra";
            case REEMBOLSO -> "Reembolso";
        };
    }
}
