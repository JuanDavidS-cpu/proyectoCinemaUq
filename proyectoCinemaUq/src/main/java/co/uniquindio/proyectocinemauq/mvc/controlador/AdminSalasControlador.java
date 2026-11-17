package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Sala;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/** Alta y listado de salas. La plantilla se clona al crear cada función. */
public class AdminSalasControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<Sala> lista = new ListView<>();
        lista.getItems().setAll(cine.getSalas());
        lista.setPlaceholder(new javafx.scene.control.Label("Aún no hay salas."));
        lista.setPrefHeight(220);
        TextField nombre = new TextField();
        nombre.setPromptText("Sala 3");
        TextField filas = new TextField("4");
        TextField columnas = new TextField("6");
        Button crear = VistaBase.primario("Crear sala");
        crear.setOnAction(evento -> {
            try {
                cine.agregarSala(Navegacion.getSesion(), nombre.getText(),
                        Lectura.entero(filas.getText(), "Escribe las filas, por ejemplo 4."),
                        Lectura.entero(columnas.getText(), "Escribe las columnas, por ejemplo 6."));
                lista.getItems().setAll(cine.getSalas());
                nombre.clear();
                Avisos.info("Sala creada",
                        "Las funciones nuevas clonan estas sillas. Las funciones ya creadas no cambian.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });
        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("Una sala de 4 filas y 6 columnas alcanza para probar la grilla."),
                lista,
                VistaBase.subtitulo("Nueva sala"),
                Formularios.campo("Nombre", nombre),
                Formularios.campo("Filas", filas),
                Formularios.campo("Columnas", columnas),
                crear));
    }
}
