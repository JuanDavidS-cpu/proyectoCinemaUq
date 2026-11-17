package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.MovimientoTarjeta;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Saldo y movimientos de la tarjeta del cliente. */
public class TarjetaControlador {

    public Parent crearVista() {
        Cliente cliente = Navegacion.getSesion();
        BorderPane pagina = VistaBase.pagina("Mi tarjeta", "Volver", () -> Navegacion.irInicioCliente(), null);
        Label saldo = new Label(Textos.pesos(cliente.getTarjeta().getSaldo()));
        saldo.getStyleClass().add("grande");
        String estado = cliente.getTarjeta().estaActiva()
                ? "La tarjeta está activa."
                : "La tarjeta está inactiva. El administrador puede volver a activarla.";
        ListView<String> movimientos = new ListView<>();
        for (MovimientoTarjeta movimiento : cliente.getTarjeta().getMovimientos()) {
            movimientos.getItems().add(movimiento.texto());
        }
        movimientos.setPlaceholder(new Label("Aún no hay movimientos."));
        movimientos.setPrefHeight(360);
        VBox centro = VistaBase.conMargen(
                VistaBase.ayuda("Este es el saldo que baja cuando pagas con tarjeta."),
                saldo,
                VistaBase.ayuda(estado),
                VistaBase.subtitulo("Movimientos"),
                movimientos);
        pagina.setCenter(VistaBase.scroll(centro));
        return pagina;
    }
}
