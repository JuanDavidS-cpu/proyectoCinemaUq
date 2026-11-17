package co.uniquindio.proyectocinemauq.estructural.adapter;

import co.uniquindio.proyectocinemauq.mvc.modelo.Reporte;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Traduce "exportar reporte" al idioma de la librería PDF.
 * El PDF sigue siendo simulado. El TXT es el archivo que sí se guarda.
 */
public class AdaptadorPDF implements ExportadorReporte {

    private final LibreriaExternaPDF libreria;
    private String ultimoTexto;

    public AdaptadorPDF() {
        this.libreria = new LibreriaExternaPDF();
        this.ultimoTexto = "";
    }

    @Override
    public String formatear(Reporte reporte) {
        StringBuilder texto = new StringBuilder();
        texto.append(libreria.crearDocumento(reporte.getTitulo())).append('\n');
        texto.append(libreria.escribirContenido(reporte.getTotales())).append('\n');
        texto.append(libreria.guardarArchivo());
        ultimoTexto = texto.toString();
        return ultimoTexto;
    }

    @Override
    public void exportar(Reporte reporte, Path destino) throws IOException {
        formatear(reporte);
    }

    public String getUltimoTexto() {
        return ultimoTexto;
    }
}
