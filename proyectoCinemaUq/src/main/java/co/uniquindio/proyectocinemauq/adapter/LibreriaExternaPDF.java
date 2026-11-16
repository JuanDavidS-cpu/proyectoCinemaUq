package co.uniquindio.proyectocinemauq.adapter;

/**
 * Clase adaptada (Adaptee). Simula una libreria externa de terceros (ej. iText)
 * cuyos metodos son incompatibles con la interfaz ExportadorReporte.
 */
public class LibreriaExternaPDF {

    public void crearDocumento(String ruta) {
        System.out.println("Libreria PDF: Creando documento en " + ruta + ".pdf");
    }

    public void escribirContenido(String texto) {
        System.out.println("Libreria PDF: Escribiendo -> " + texto);
    }

    public void guardarArchivo() {
        System.out.println("Libreria PDF: Archivo guardado exitosamente.");
    }
}