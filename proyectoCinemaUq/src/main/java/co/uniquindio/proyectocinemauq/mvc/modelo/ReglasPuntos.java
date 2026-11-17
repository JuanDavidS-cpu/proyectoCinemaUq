package co.uniquindio.proyectocinemauq.mvc.modelo;

/**
 * Acumulación: 1 punto por cada 1000 pesos pagados con tarjeta.
 * Lo pagado con puntos no acumula. Cada lote vence a los 12 meses.
 * Redención mínima: entrada general 150 puntos, entrada preferencial 250,
 * producto suelto o combo = precio en pesos / 100 (el combo usa el precio ya descontado).
 * Las gafas 3D suman 50 puntos. La promoción del día descuenta pesos y no baja estos puntos,
 * para no aplicar dos descuentos.
 */
public final class ReglasPuntos {

    public static final int PESOS_POR_PUNTO = 1000;
    public static final int PUNTOS_ENTRADA_GENERAL = 150;
    public static final int PUNTOS_ENTRADA_PREFERENCIAL = 250;
    public static final double PRECIO_ENTRADA_GENERAL = 12000;
    public static final double PRECIO_ENTRADA_PREFERENCIAL = 18000;
    public static final double PRECIO_GAFAS = 5000;
    public static final int PUNTOS_GAFAS = 50;

    private ReglasPuntos() {
    }

    public static int porPagoConTarjeta(double pesos) {
        if (pesos <= 0) {
            return 0;
        }
        return (int) (Math.round(pesos) / PESOS_POR_PUNTO);
    }

    public static int porPrecio(double precio) {
        if (precio <= 0) {
            return 0;
        }
        return (int) Math.round(Math.round(precio) / 100.0);
    }
}
