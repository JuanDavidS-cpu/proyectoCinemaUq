package co.uniquindio.proyectocinemauq.estructural.adapter;

/**
 * Librería ajena de PDF. Sus métodos no coinciden con ExportadorReporte.
 * En este proyecto es un ejemplo: no genera un PDF real.
 */
public class LibreriaExternaPDF {

    public String crearDocumento(String ruta) {
        return "Librería PDF: creando documento en " + ruta + ".pdf";
    }

    public String escribirContenido(String texto) {
        return "Librería PDF: escribiendo el contenido del reporte";
    }

    public String guardarArchivo() {
        return "Librería PDF: simulación guardada. El archivo real del cine es el TXT.";
    }
}
