package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.adapter.AdaptadorPDF;
import co.uniquindio.proyectocinemauq.estructural.adapter.ExportadorReporte;
import co.uniquindio.proyectocinemauq.estructural.adapter.ExportadorTXT;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Reporte;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoReporte;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/** Elige el reporte y lo guarda en un .txt real mediante el adaptador. */
public class AdminReportesControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ComboBox<TipoReporte> tipos = new ComboBox<>();
        tipos.getItems().addAll(TipoReporte.values());
        tipos.setValue(TipoReporte.VENTAS);
        tipos.setMaxWidth(Double.MAX_VALUE);
        DatePicker desde = new DatePicker(LocalDate.now().minusDays(7));
        DatePicker hasta = new DatePicker(LocalDate.now());
        TextArea vistaPrevia = new TextArea("Elige el tipo y las fechas, y pulsa Ver reporte.");
        vistaPrevia.setEditable(false);
        vistaPrevia.setWrapText(true);
        vistaPrevia.setPrefRowCount(16);

        Button ver = VistaBase.secundario("Ver reporte");
        ver.setOnAction(evento -> {
            try {
                Reporte reporte = cine.generarReporte(tipos.getValue(), desde.getValue(), hasta.getValue());
                vistaPrevia.setText(new ExportadorTXT().formatear(reporte));
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo armar el reporte", e.getMessage());
            }
        });

        Button exportar = VistaBase.primario("Exportar TXT");
        exportar.setOnAction(evento -> exportarTxt(cine, tipos, desde, hasta, vistaPrevia));

        Button pdf = VistaBase.secundario("Simular PDF");
        pdf.setOnAction(evento -> {
            try {
                Reporte reporte = cine.generarReporte(tipos.getValue(), desde.getValue(), hasta.getValue());
                ExportadorReporte adaptador = new AdaptadorPDF();
                Avisos.info("PDF de ejemplo",
                        adaptador.formatear(reporte)
                                + "\n\nLa librería PDF es un ejemplo. El archivo de verdad se guarda con Exportar TXT.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo armar el reporte", e.getMessage());
            }
        });

        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("El TXT se guarda en tu computador. Incluye título, fecha, periodo, registros y totales."),
                Formularios.campo("Tipo de reporte", tipos),
                Formularios.campo("Desde", desde),
                Formularios.campo("Hasta", hasta),
                ver,
                vistaPrevia,
                exportar,
                pdf));
    }

    private void exportarTxt(CinemaUQ cine, ComboBox<TipoReporte> tipos, DatePicker desde, DatePicker hasta,
                             TextArea vistaPrevia) {
        try {
            Reporte reporte = cine.generarReporte(tipos.getValue(), desde.getValue(), hasta.getValue());
            FileChooser selector = new FileChooser();
            selector.setTitle("Guardar reporte");
            selector.setInitialFileName("reporte-cinemauq.txt");
            selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto", "*.txt"));
            File archivo = selector.showSaveDialog(Navegacion.getEscenario());
            if (archivo == null) {
                return;
            }
            ExportadorReporte exportador = new ExportadorTXT();
            exportador.exportar(reporte, archivo.toPath());
            vistaPrevia.setText(exportador.formatear(reporte));
            Avisos.info("Reporte guardado",
                    "Puedes abrirlo con el bloc de notas. Quedó en " + archivo.getAbsolutePath());
        } catch (ExcepcionCinema e) {
            Avisos.error("No se pudo armar el reporte", e.getMessage());
        } catch (IOException e) {
            Avisos.error("No se pudo guardar", "No se pudo guardar el archivo. Elige otra carpeta.");
        }
    }
}
