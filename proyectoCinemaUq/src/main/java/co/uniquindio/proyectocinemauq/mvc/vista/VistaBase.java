package co.uniquindio.proyectocinemauq.mvc.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Misma barra de título y botón de esquina en todas las pantallas. */
public final class VistaBase {

    private VistaBase() {
    }

    public static BorderPane pagina(String titulo, String textoEsquina, Runnable accion, String paso) {
        BorderPane raiz = new BorderPane();
        VBox tope = new VBox(6);
        tope.getStyleClass().add("encabezado");
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("titulo");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        Button esquina = new Button(textoEsquina);
        esquina.getStyleClass().add("boton-volver");
        esquina.setOnAction(evento -> accion.run());
        fila.getChildren().addAll(tituloLabel, espacio, esquina);
        tope.getChildren().add(fila);
        if (paso != null && !paso.isBlank()) {
            Label pasoLabel = new Label(paso);
            pasoLabel.getStyleClass().add("paso");
            tope.getChildren().add(pasoLabel);
        }
        tope.getChildren().add(new Separator());
        raiz.setTop(tope);
        return raiz;
    }

    public static Button primario(String texto) {
        Button boton = new Button(texto);
        boton.getStyleClass().add("boton-primario");
        return boton;
    }

    public static Button secundario(String texto) {
        Button boton = new Button(texto);
        boton.getStyleClass().add("boton-secundario");
        return boton;
    }

    public static Button grande(String texto) {
        Button boton = new Button(texto);
        boton.getStyleClass().add("boton-grande");
        boton.setWrapText(true);
        return boton;
    }

    public static Label ayuda(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.getStyleClass().add("ayuda");
        etiqueta.setWrapText(true);
        return etiqueta;
    }

    public static Label subtitulo(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.getStyleClass().add("subtitulo");
        etiqueta.setWrapText(true);
        return etiqueta;
    }

    public static VBox conMargen(Node... nodos) {
        VBox caja = new VBox(12, nodos);
        caja.setPadding(new Insets(18, 24, 18, 24));
        return caja;
    }

    public static HBox barra(Node... botones) {
        HBox barra = new HBox(12, botones);
        barra.setAlignment(Pos.CENTER_RIGHT);
        barra.setPadding(new Insets(12, 24, 18, 24));
        return barra;
    }

    public static ScrollPane scroll(Node nodo) {
        ScrollPane scroll = new ScrollPane(nodo);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-limpio");
        return scroll;
    }
}
