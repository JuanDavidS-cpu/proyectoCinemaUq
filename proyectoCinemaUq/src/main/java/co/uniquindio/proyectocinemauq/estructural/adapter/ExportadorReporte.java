package co.uniquindio.proyectocinemauq.estructural.adapter;

import co.uniquindio.proyectocinemauq.mvc.modelo.Reporte;

import java.io.IOException;
import java.nio.file.Path;

/**
 * El cine pide exportar un reporte sin saber si el destino habla TXT o PDF.
 */
public interface ExportadorReporte {
    String formatear(Reporte reporte);

    void exportar(Reporte reporte, Path destino) throws IOException;
}
