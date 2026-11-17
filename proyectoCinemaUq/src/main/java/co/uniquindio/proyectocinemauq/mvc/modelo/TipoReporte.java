package co.uniquindio.proyectocinemauq.mvc.modelo;

public enum TipoReporte {
    VENTAS("Ventas por periodo"),
    ENTRADAS("Entradas por película o función"),
    OCUPACION("Ocupación de salas"),
    PRODUCTOS("Productos y combos vendidos"),
    RECARGAS("Recargas"),
    REEMBOLSOS("Cancelaciones y reembolsos"),
    PUNTOS("Puntos acumulados y redimidos");

    private final String etiqueta;

    TipoReporte(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
