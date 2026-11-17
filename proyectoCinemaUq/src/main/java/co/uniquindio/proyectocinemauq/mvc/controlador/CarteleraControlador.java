package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Paso 1: el cliente elige la función. */
public class CarteleraControlador {

    @FXML
    private ListView<Funcion> listaFunciones;
    @FXML
    private Label vacio;

    @FXML
    public void initialize() {
        listaFunciones.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Funcion item, boolean empty) {
                super.updateItem(item, empty);
                setWrapText(true);
                setText(empty || item == null ? null : item.textoCartelera());
                setPrefHeight(empty || item == null ? 28 : 64);
            }
        });
        List<Funcion> visibles = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (Funcion funcion : CinemaUQ.getInstancia().getFunciones()) {
            if (!funcion.estaCancelada() && funcion.getFechaHora().isAfter(ahora)) {
                visibles.add(funcion);
            }
        }
        listaFunciones.getItems().setAll(visibles);
        boolean sinFunciones = visibles.isEmpty();
        vacio.setVisible(sinFunciones);
        vacio.setManaged(sinFunciones);
        listaFunciones.setVisible(!sinFunciones);
        listaFunciones.setManaged(!sinFunciones);
    }

    @FXML
    public void volver() {
        Navegacion.getCompraEnCurso().abandonar();
        Navegacion.irInicioCliente();
    }

    @FXML
    public void elegirAsientos() {
        Funcion funcion = listaFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            Avisos.error("Elige una función", "Selecciona una función de la lista y después pulsa Elegir asientos.");
            return;
        }
        if (funcion.contarLibres() == 0 && funcion.contarOcupados() == funcion.getAsientos().size()) {
            Avisos.error("Sala llena", "No quedan asientos en esta función. Elige otra.");
            return;
        }
        Navegacion.iniciarCompra(funcion);
    }
}
