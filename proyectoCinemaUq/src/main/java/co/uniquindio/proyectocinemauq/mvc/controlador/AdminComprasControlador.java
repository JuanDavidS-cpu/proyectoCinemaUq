package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/** Listado de todas las compras del cine. */
public class AdminComprasControlador {

    public Parent crearVista() {
        ListView<String> lista = new ListView<>();
        for (Compra compra : CinemaUQ.getInstancia().getCompras()) {
            lista.getItems().add(compra.getCliente().getNombre() + " · " + compra.textoCliente());
        }
        lista.setPlaceholder(new Label("Aún no hay compras."));
        lista.setPrefHeight(420);
        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("Para devolver el 100% de una función, ve a Funciones y cancélala."),
                lista));
    }
}
