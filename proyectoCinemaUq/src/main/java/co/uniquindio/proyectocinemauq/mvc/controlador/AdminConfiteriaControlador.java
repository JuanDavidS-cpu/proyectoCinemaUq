package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.composite.Combo;
import co.uniquindio.proyectocinemauq.estructural.composite.ProductoConfiteria;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/** Productos sueltos y combos. El descuento del combo no es la promoción del día. */
public class AdminConfiteriaControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<ProductoConfiteria> productos = new ListView<>();
        productos.getItems().setAll(cine.getProductos());
        productos.setPlaceholder(new javafx.scene.control.Label("Aún no hay productos."));
        productos.setPrefHeight(160);
        ListView<Combo> combos = new ListView<>();
        combos.getItems().setAll(cine.getCombos());
        combos.setPlaceholder(new javafx.scene.control.Label("Aún no hay combos."));
        combos.setPrefHeight(140);

        TextField nombreProducto = new TextField();
        TextField precio = new TextField();
        precio.setPromptText("15000");
        Button crearProducto = VistaBase.primario("Crear producto");
        crearProducto.setOnAction(evento -> {
            try {
                cine.agregarProducto(Navegacion.getSesion(), nombreProducto.getText(),
                        Lectura.decimal(precio.getText(), "Escribe el precio en pesos, por ejemplo 15000."));
                productos.getItems().setAll(cine.getProductos());
                nombreProducto.clear();
                precio.clear();
                Avisos.info("Producto creado", "Ya se puede vender y también puede entrar en un combo.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });

        TextField nombreCombo = new TextField();
        TextField descuento = new TextField("10");
        ListView<ProductoConfiteria> elegibles = new ListView<>();
        elegibles.getItems().setAll(cine.getProductos());
        elegibles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        elegibles.setPrefHeight(120);
        Button crearCombo = VistaBase.secundario("Crear combo");
        crearCombo.setOnAction(evento -> {
            try {
                int porciento = Lectura.entero(descuento.getText(),
                        "Escribe el descuento del combo entre 0 y 50.");
                List<ProductoConfiteria> elegidos = new ArrayList<>(elegibles.getSelectionModel().getSelectedItems());
                cine.agregarCombo(Navegacion.getSesion(), nombreCombo.getText(), porciento / 100.0, elegidos);
                combos.getItems().setAll(cine.getCombos());
                nombreCombo.clear();
                Avisos.info("Combo creado",
                        "El descuento interno ya quedó en el precio. La promoción del día no lo vuelve a restar.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });

        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.subtitulo("Productos"),
                productos,
                Formularios.campo("Nombre del producto", nombreProducto),
                Formularios.campo("Precio en pesos", precio),
                crearProducto,
                VistaBase.subtitulo("Combos"),
                combos,
                Formularios.campo("Nombre del combo", nombreCombo),
                Formularios.campo("Descuento interno (0 a 50)", descuento),
                Formularios.campo("Productos del combo (elige al menos dos)", elegibles),
                crearCombo));
    }
}
