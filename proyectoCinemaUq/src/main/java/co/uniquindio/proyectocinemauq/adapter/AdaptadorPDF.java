package co.uniquindio.proyectocinemauq.adapter;

/**
 * Clase Adaptador (Adapter).
 * Traduce la llamada simple de exportar() a la secuencia compleja de la libreria PDF.
 */
public class AdaptadorPDF implements ExportadorReporte {
    private LibreriaExternaPDF libreriaPDF;

    public AdaptadorPDF() {
        this.libreriaPDF = new LibreriaExternaPDF();
    }

    @Override
    public void exportar(String datos, String nombreArchivo) {
        libreriaPDF.crearDocumento(nombreArchivo);
        libreriaPDF.escribirContenido(datos);
        libreriaPDF.guardarArchivo();
    }
}