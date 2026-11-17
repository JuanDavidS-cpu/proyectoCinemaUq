package co.uniquindio.proyectocinemauq.mvc.vista;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

/** Mensajes que el usuario lee en la ventana, no en la consola. */
public final class Avisos {

    private Avisos() {
    }

    public static void info(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    public static void error(String titulo, String mensaje) {
        mostrar(Alert.AlertType.WARNING, titulo, mensaje);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        ButtonType si = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, si, no);
        preparar(alerta, titulo, mensaje);
        return alerta.showAndWait().orElse(no) == si;
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        preparar(alerta, titulo, mensaje);
        alerta.showAndWait();
    }

    private static void preparar(Alert alerta, String titulo, String mensaje) {
        alerta.setTitle("CinemaUQ");
        alerta.setHeaderText(titulo);
        TextArea area = new TextArea(mensaje);
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(4);
        area.setPrefWidth(460);
        alerta.getDialogPane().setContent(area);
        alerta.getDialogPane().setMinWidth(480);
        alerta.setContentText(mensaje);
    }
}
