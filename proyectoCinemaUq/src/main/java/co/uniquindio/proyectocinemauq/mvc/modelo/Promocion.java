package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.DayOfWeek;

/**
 * Promoción distinta del descuento interno del combo.
 * Porcentaje del día: ese día se descuenta un porcentaje solo de confitería
 * (productos y combos), usando el precio que ya traen. No se recalcula el descuento del combo.
 * 2x1: cada segunda unidad de un producto suelto sale gratis. No mira lo que va dentro de un combo.
 * Si coinciden las dos, primero se quitan las unidades 2x1 y el porcentaje se aplica al resto.
 * Esta promoción descuenta pesos. El precio en puntos de cada ítem no cambia.
 */
public class Promocion {

    private final String id;
    private final String nombre;
    private final TipoPromocion tipo;
    private final DayOfWeek dia;
    private final double porcentaje;
    private final String nombreProducto;

    public Promocion(String id, String nombre, TipoPromocion tipo, DayOfWeek dia,
                     double porcentaje, String nombreProducto) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dia = dia;
        this.porcentaje = porcentaje;
        this.nombreProducto = nombreProducto;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPromocion getTipo() {
        return tipo;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    @Override
    public String toString() {
        if (tipo == TipoPromocion.DOS_POR_UNO) {
            return nombre + " · 2x1 en " + nombreProducto;
        }
        int porciento = (int) Math.round(porcentaje * 100);
        return nombre + " · " + porciento + "% los " + Textos.dia(dia) + " en confitería";
    }
}
