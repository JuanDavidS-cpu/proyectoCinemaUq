package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.EstadoCompra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasCancelacion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

/** Historial del cliente y cancelación según las horas que faltan. */
public class ComprasControlador {

    public Parent crearVista() {
        Cliente cliente = Navegacion.getSesion();
        BorderPane pagina = VistaBase.pagina("Mis compras", "Volver", () -> Navegacion.irInicioCliente(), null);
        VBox lista = new VBox(12);
        if (cliente.getCompras().isEmpty()) {
            lista.getChildren().add(VistaBase.subtitulo("Aún no tienes compras."));
        }
        for (Compra compra : cliente.getCompras()) {
            lista.getChildren().add(tarjeta(cliente, compra));
        }
        VBox centro = VistaBase.conMargen(
                VistaBase.ayuda("Si faltan más de 24 horas, se devuelve el 100%. Entre 2 y 24 horas, el 80%. "
                        + "Con menos de 2 horas no se puede cancelar."),
                lista);
        pagina.setCenter(VistaBase.scroll(centro));
        return pagina;
    }

    private VBox tarjeta(Cliente cliente, Compra compra) {
        Label texto = new Label(compra.textoCliente());
        texto.setWrapText(true);
        VBox caja = new VBox(8, texto);
        caja.getStyleClass().add("tarjeta");
        if (compra.getEstado() != EstadoCompra.ACTIVA) {
            return caja;
        }
        Button cancelar = VistaBase.secundario("Cancelar compra");
        cancelar.setOnAction(evento -> cancelar(cliente, compra));
        caja.getChildren().add(cancelar);
        return caja;
    }

    private void cancelar(Cliente cliente, Compra compra) {
        LocalDateTime ahora = LocalDateTime.now();
        try {
            ReglasCancelacion.porcentaje(ahora, compra.getFuncion().getFechaHora());
        } catch (ExcepcionCinema e) {
            Avisos.error("No se puede cancelar", e.getMessage());
            return;
        }
        boolean sigue = Avisos.confirmar("Cancelar compra",
                ReglasCancelacion.frase(ahora, compra.getFuncion().getFechaHora()) + " ¿Deseas cancelarla?");
        if (!sigue) {
            return;
        }
        try {
            CinemaUQ.getInstancia().cancelarCompra(cliente, compra);
            String mensaje;
            if (compra.getMedioPago() == MedioPago.TARJETA) {
                mensaje = "Cancelamos la compra. Devolvimos " + Textos.pesos(compra.getMontoReembolsado())
                        + " a tu tarjeta. El saldo quedó en " + Textos.pesos(cliente.getTarjeta().getSaldo()) + ".";
            } else {
                mensaje = "Cancelamos la compra. Devolvimos " + compra.getPuntosDevueltos()
                        + " puntos, contados desde hoy y con vencimiento a un año. Míralos en Mis puntos.";
            }
            Avisos.info("Compra cancelada", mensaje);
            Navegacion.irCompras();
        } catch (ExcepcionCinema e) {
            Avisos.error("No se puede cancelar", e.getMessage());
        }
    }
}
