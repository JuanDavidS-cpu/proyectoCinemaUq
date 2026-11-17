package co.uniquindio.proyectocinemauq.estructural.adapter;

import co.uniquindio.proyectocinemauq.mvc.modelo.Reporte;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Escribe el reporte en un archivo .txt de verdad.
 * Solo escribe: no calcula las ventas. El archivo lleva título, fecha, periodo, registros y totales.
 */
public class ExportadorTXT implements ExportadorReporte {

    @Override
    public String formatear(Reporte reporte) {
        StringBuilder texto = new StringBuilder();
        texto.append(reporte.getTitulo()).append('\n');
        texto.append("Fecha de generación: ").append(Textos.fechaHora(reporte.getFechaGeneracion())).append('\n');
        texto.append("Periodo: ").append(Textos.fecha(reporte.getDesde()))
                .append(" a ").append(Textos.fecha(reporte.getHasta())).append("\n\n");
        texto.append("Registros:\n");
        if (reporte.getRegistros().isEmpty()) {
            texto.append("(sin registros)\n");
        } else {
            for (String registro : reporte.getRegistros()) {
                texto.append("- ").append(registro).append('\n');
            }
        }
        texto.append("\nTotales:\n").append(reporte.getTotales()).append('\n');
        return texto.toString();
    }

    @Override
    public void exportar(Reporte reporte, Path destino) throws IOException {
        Files.writeString(destino, formatear(reporte), StandardCharsets.UTF_8);
    }
}
