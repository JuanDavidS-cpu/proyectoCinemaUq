package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.LotePuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.List;

/** Puntos vigentes, vencidos y la regla para redimirlos en una compra. */
public class PuntosControlador {

    public Parent crearVista() {
        Cliente cliente = Navegacion.getSesion();
        LocalDateTime ahora = LocalDateTime.now();
        BorderPane pagina = VistaBase.pagina("Mis puntos", "Volver", () -> Navegacion.irInicioCliente(), null);
        int vigentes = cliente.getCuentaPuntos().puntosDisponibles(ahora);
        VBox lotes = new VBox(8);
        List<LotePuntos> orden = cliente.getCuentaPuntos().lotesOrdenados();
        if (orden.isEmpty()) {
            lotes.getChildren().add(VistaBase.subtitulo("Aún no tienes puntos."));
        }
        for (LotePuntos lote : orden) {
            String linea = Textos.puntos(lote.getMonto()) + " · obtenidos el "
                    + Textos.fecha(lote.getFechaObtencion());
            if (lote.estaVencido(ahora)) {
                linea += " · vencieron el " + Textos.fecha(lote.getFechaVencimiento())
                        + ". Vencidos, no se pueden usar.";
            } else {
                linea += " · vencen el " + Textos.fecha(lote.getFechaVencimiento()) + ".";
            }
            lotes.getChildren().add(VistaBase.ayuda(linea));
        }
        Button cartelera = VistaBase.primario("Ir a cartelera");
        cartelera.setOnAction(evento -> Navegacion.irCartelera());
        VBox centro = VistaBase.conMargen(
                new javafx.scene.control.Label("Vigentes: " + Textos.puntos(vigentes)),
                VistaBase.ayuda("Para redimir, arma una compra y elige Pagar con puntos. "
                        + "Se gastan primero los que vencen antes."),
                VistaBase.ayuda("Entrada general: " + ReglasPuntos.PUNTOS_ENTRADA_GENERAL
                        + " puntos. Preferencial: " + ReglasPuntos.PUNTOS_ENTRADA_PREFERENCIAL
                        + ". Un producto o combo vale su precio dividido entre 100."),
                VistaBase.subtitulo("Lotes"),
                lotes);
        centro.getChildren().get(0).getStyleClass().add("grande");
        pagina.setCenter(VistaBase.scroll(centro));
        pagina.setBottom(VistaBase.barra(cartelera));
        return pagina;
    }
}
