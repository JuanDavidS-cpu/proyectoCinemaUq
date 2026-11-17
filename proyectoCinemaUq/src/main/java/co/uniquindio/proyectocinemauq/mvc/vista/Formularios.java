package co.uniquindio.proyectocinemauq.mvc.vista;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Etiqueta encima del campo, para que el formulario se lea solo. */
public final class Formularios {

    private Formularios() {
    }

    public static VBox campo(String etiqueta, Node control) {
        Label titulo = new Label(etiqueta);
        titulo.getStyleClass().add("campo-etiqueta");
        VBox caja = new VBox(4, titulo, control);
        return caja;
    }
}
