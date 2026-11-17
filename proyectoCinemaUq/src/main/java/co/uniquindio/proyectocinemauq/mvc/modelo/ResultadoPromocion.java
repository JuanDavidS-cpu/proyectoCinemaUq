package co.uniquindio.proyectocinemauq.mvc.modelo;

/** Cuánto descontó la promoción y cómo se llama, para mostrarlo en el resumen. */
public class ResultadoPromocion {

    private final double descuento;
    private final String descripcion;

    public ResultadoPromocion(double descuento, String descripcion) {
        this.descuento = descuento;
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
