package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Pelicula;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Alta y listado de películas. */
public class AdminPeliculasControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<Pelicula> lista = new ListView<>();
        lista.getItems().setAll(cine.getPeliculas());
        lista.setPlaceholder(new javafx.scene.control.Label("Aún no hay películas."));
        lista.setPrefHeight(220);
        TextField titulo = new TextField();
        titulo.setPromptText("El viaje del cóndor");
        TextField duracion = new TextField();
        duracion.setPromptText("120");
        TextField genero = new TextField();
        genero.setPromptText("Aventura");
        Button crear = VistaBase.primario("Crear película");
        crear.setOnAction(evento -> {
            try {
                cine.agregarPelicula(Navegacion.getSesion(), titulo.getText(),
                        Lectura.entero(duracion.getText(), "Escribe la duración en minutos, por ejemplo 120."),
                        genero.getText());
                lista.getItems().setAll(cine.getPeliculas());
                titulo.clear();
                duracion.clear();
                genero.clear();
                Avisos.info("Película creada", "Ya aparece en la lista. Puedes usarla al crear una función.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });
        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("Crea la película y luego prográmala en Funciones."),
                lista,
                VistaBase.subtitulo("Nueva película"),
                Formularios.campo("Título", titulo),
                Formularios.campo("Duración en minutos", duracion),
                Formularios.campo("Género", genero),
                crear));
    }
}
