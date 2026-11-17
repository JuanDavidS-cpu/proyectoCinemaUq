package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Pelicula;
import co.uniquindio.proyectocinemauq.mvc.modelo.Sala;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

/** Programa funciones y puede cancelarlas con reembolso del 100%. */
public class AdminFuncionesControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<Funcion> lista = new ListView<>();
        lista.setCellFactory(vista -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Funcion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.textoAdmin());
            }
        });
        Runnable refrescar = () -> lista.getItems().setAll(cine.getFunciones());
        refrescar.run();
        lista.setPlaceholder(new javafx.scene.control.Label("Aún no hay funciones."));
        lista.setPrefHeight(220);

        ComboBox<Pelicula> peliculas = new ComboBox<>();
        peliculas.getItems().setAll(cine.getPeliculas());
        peliculas.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Sala> salas = new ComboBox<>();
        salas.getItems().setAll(cine.getSalas());
        salas.setMaxWidth(Double.MAX_VALUE);
        DatePicker fecha = new DatePicker();
        TextField hora = new TextField();
        hora.setPromptText("18:30");

        Button crear = VistaBase.primario("Crear función");
        crear.setOnAction(evento -> {
            try {
                if (fecha.getValue() == null) {
                    throw new co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException(
                            "Elige la fecha de la función.");
                }
                LocalDateTime cuando = LocalDateTime.of(fecha.getValue(), Lectura.hora(hora.getText()));
                cine.agregarFuncion(Navegacion.getSesion(), peliculas.getValue(), salas.getValue(), cuando);
                refrescar.run();
                Avisos.info("Función creada",
                        "Ya está en la cartelera si la hora es futura. Cada función tiene sus propias sillas.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });

        Button cancelar = VistaBase.secundario("Cancelar función");
        cancelar.setOnAction(evento -> {
            Funcion funcion = lista.getSelectionModel().getSelectedItem();
            if (funcion == null) {
                Avisos.error("Elige una función", "Selecciona la función de la lista y luego cancélala.");
                return;
            }
            boolean sigue = Avisos.confirmar("Cancelar función",
                    "Se devolverá el 100% de cada compra activa y avisaremos a esos clientes. ¿Deseas cancelarla?");
            if (!sigue) {
                return;
            }
            try {
                cine.cancelarFuncion(Navegacion.getSesion(), funcion);
                refrescar.run();
                Avisos.info("Función cancelada",
                        "Se devolvió el 100% a cada compra activa. Los clientes lo ven al volver a su inicio.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo cancelar", e.getMessage());
            }
        });

        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("Cancelar la función devuelve el 100% y avisa a quien compró."),
                lista,
                cancelar,
                VistaBase.subtitulo("Nueva función"),
                Formularios.campo("Película", peliculas),
                Formularios.campo("Sala", salas),
                Formularios.campo("Fecha", fecha),
                Formularios.campo("Hora (18:30)", hora),
                crear));
    }
}
