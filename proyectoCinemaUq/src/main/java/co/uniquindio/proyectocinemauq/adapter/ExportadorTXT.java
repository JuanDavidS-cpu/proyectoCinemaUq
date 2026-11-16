package co.uniquindio.proyectocinemauq.adapter;

/**
 * Implementacion nativa para exportar reportes en formato de texto plano.
 */
public class ExportadorTXT implements ExportadorReporte {

    @Override
    public void exportar(String datos, String nombreArchivo) {
        System.out.println("Generando archivo TXT: " + nombreArchivo + ".txt");
        System.out.println("Contenido del reporte: " + datos);
    }
}