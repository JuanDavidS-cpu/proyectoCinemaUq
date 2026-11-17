package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.comportamiento.command.CancelarReserva;
import co.uniquindio.proyectocinemauq.comportamiento.command.GestorReservas;
import co.uniquindio.proyectocinemauq.comportamiento.command.ReservarAsiento;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Paso 2: grilla de sillas. Reservar y soltar usa el comando sobre el asiento del modelo. */
public class AsientosControlador {

    public Parent crearVista() {
        CompraEnCurso curso = Navegacion.getCompraEnCurso();
        Funcion funcion = curso.getFuncion();
        BorderPane pagina = VistaBase.pagina("Asientos", "Volver", () -> Navegacion.irCartelera(),
                "Paso 2 de 4 · Elige los asientos");

        Label detalle = new Label(funcion.getPelicula().getTitulo() + " · " + funcion.getSala().getNombre()
                + " · " + Textos.fechaHora(funcion.getFechaHora()));
        detalle.getStyleClass().add("subtitulo");
        detalle.setWrapText(true);

        Label foco = new Label("Pasa el mouse por una silla para ver su código.");
        foco.getStyleClass().add("ayuda");
        foco.setWrapText(true);
        Label apartadas = new Label(curso.textoAsientos());
        apartadas.setWrapText(true);

        Label pantalla = new Label("Pantalla");
        pantalla.setMaxWidth(Double.MAX_VALUE);
        pantalla.setAlignment(Pos.CENTER);
        pantalla.setStyle("-fx-background-color: #241418; -fx-text-fill: white; -fx-padding: 8; -fx-background-radius: 8;");

        GridPane grilla = new GridPane();
        grilla.setHgap(8);
        grilla.setVgap(8);
        grilla.setAlignment(Pos.CENTER);
        GestorReservas gestor = new GestorReservas();
        for (Asiento asiento : funcion.getAsientos()) {
            Button boton = new Button(asiento.getCodigo());
            boton.setPrefSize(64, 42);
            pintar(boton, asiento);
            Tooltip.install(boton, new Tooltip("Asiento " + asiento.getCodigo()));
            boton.setOnMouseEntered(evento -> foco.setText(
                    "Asiento " + asiento.getCodigo() + " · " + textoEstado(asiento)));
            boton.setOnAction(evento -> pulsar(asiento, boton, gestor, curso, apartadas, foco));
            int fila = asiento.getFila().charAt(0) - 'A';
            int columna = asiento.getColumna() - 1;
            grilla.add(boton, columna, fila);
        }

        Button continuar = VistaBase.primario("Continuar");
        continuar.setOnAction(evento -> {
            if (curso.getAsientos().isEmpty()) {
                Avisos.error("Elige una silla",
                        "Aparta al menos una silla verde. El amarillo significa que ya va en tu compra.");
                return;
            }
            Navegacion.irConfiteria();
        });

        VBox centro = VistaBase.conMargen(detalle, VistaBase.ayuda(
                        "Verde está libre, amarillo la estás apartando y gris ya se vendió."),
                leyenda(), pantalla, grilla, foco, apartadas);
        pagina.setCenter(VistaBase.scroll(centro));
        pagina.setBottom(VistaBase.barra(continuar));
        return pagina;
    }

    private void pulsar(Asiento asiento, Button boton, GestorReservas gestor, CompraEnCurso curso,
                        Label apartadas, Label foco) {
        try {
            if (asiento.estaOcupado()) {
                Avisos.error("Silla vendida",
                        "El asiento " + asiento.getCodigo() + " ya está ocupado. Elige una verde.");
                return;
            }
            if (asiento.estaReservado()) {
                gestor.ejecutar(new CancelarReserva(asiento));
                curso.quitarAsiento(asiento);
            } else {
                gestor.ejecutar(new ReservarAsiento(asiento));
                curso.agregarAsiento(asiento);
            }
            pintar(boton, asiento);
            apartadas.setText(curso.textoAsientos());
            foco.setText("Asiento " + asiento.getCodigo() + " · " + textoEstado(asiento));
        } catch (ExcepcionCinema e) {
            Avisos.error("No se pudo apartar", e.getMessage());
        }
    }

    private HBox leyenda() {
        return new HBox(16, muestra("#2E7D32", "Verde: libre"),
                muestra("#F9A825", "Amarillo: apartado"),
                muestra("#BDBDBD", "Gris: vendido"));
    }

    private HBox muestra(String color, String texto) {
        Label cuadro = new Label();
        cuadro.setMinSize(18, 18);
        cuadro.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 4;");
        return new HBox(6, cuadro, new Label(texto));
    }

    private void pintar(Button boton, Asiento asiento) {
        String fondo;
        String tinta;
        if (asiento.estaOcupado()) {
            fondo = "#BDBDBD";
            tinta = "#424242";
        } else if (asiento.estaReservado()) {
            fondo = "#F9A825";
            tinta = "#241418";
        } else {
            fondo = "#2E7D32";
            tinta = "white";
        }
        boton.setStyle("-fx-background-color: " + fondo + "; -fx-text-fill: " + tinta
                + "; -fx-font-weight: bold; -fx-background-radius: 6;");
    }

    private String textoEstado(Asiento asiento) {
        if (asiento.estaOcupado()) {
            return "vendido";
        }
        if (asiento.estaReservado()) {
            return "apartado para tu compra";
        }
        return "libre";
    }
}
