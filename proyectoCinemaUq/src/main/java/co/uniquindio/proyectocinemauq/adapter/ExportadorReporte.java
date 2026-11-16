package co.uniquindio.proyectocinemauq.adapter;

/**
 * Interfaz objetivo (Target) del patron Adapter.
 * Estandariza la forma en que CinemaUQ exporta cualquier reporte.
 */
public interface ExportadorReporte {
    void exportar(String datos, String nombreArchivo);
}