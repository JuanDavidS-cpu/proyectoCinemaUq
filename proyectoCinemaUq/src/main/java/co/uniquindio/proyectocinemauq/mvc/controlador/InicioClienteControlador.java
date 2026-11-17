package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.LotePuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.List;

/** Puerta de entrada del cliente, después de iniciar sesión. */
public class InicioClienteControlador {

    private Cliente cliente;

    public Parent crearVista() {
        cliente = Navegacion.getSesion();
        BorderPane pagina = VistaBase.pagina("Hola, " + cliente.getNombre(), "Cerrar sesión",
                () -> Navegacion.cerrarSesion(), null);
        LocalDateTime ahora = LocalDateTime.now();
        int vigentes = cliente.getCuentaPuntos().puntosDisponibles(ahora);
        LotePuntos proximo = cliente.getCuentaPuntos().proximoAVencer(ahora);
        String vencimiento = proximo == null
                ? "No tienes puntos por vencer."
                : "El próximo lote vence el " + Textos.fecha(proximo.getFechaVencimiento())
                + " (" + Textos.puntos(proximo.getMonto()) + ").";

        Button cartelera = VistaBase.primario("Cartelera\nElegir función y silla");
        cartelera.setMinSize(260, 92);
        cartelera.setWrapText(true);
        cartelera.setOnAction(evento -> Navegacion.irCartelera());

        Button compras = VistaBase.grande("Mis compras\nVer o cancelar");
        compras.setOnAction(evento -> Navegacion.irCompras());
        Button tarjeta = VistaBase.grande("Mi tarjeta\nSaldo y movimientos");
        tarjeta.setOnAction(evento -> Navegacion.irTarjeta());
        Button puntos = VistaBase.grande("Mis puntos\nVencimiento y redención");
        puntos.setOnAction(evento -> Navegacion.irPuntos());

        FlowPane accesos = new FlowPane(16, 16, cartelera, compras, tarjeta, puntos);
        accesos.setAlignment(Pos.CENTER_LEFT);

        VBox centro = VistaBase.conMargen(
                VistaBase.ayuda("Desde aquí compras, miras tu saldo o tus puntos."),
                new javafx.scene.control.Label("Saldo  " + Textos.pesos(cliente.getTarjeta().getSaldo())),
                new javafx.scene.control.Label("Puntos vigentes  " + Textos.puntos(vigentes)),
                VistaBase.ayuda(vencimiento),
                accesos);
        centro.getChildren().get(1).getStyleClass().add("grande");
        centro.getChildren().get(2).getStyleClass().add("subtitulo");
        pagina.setCenter(VistaBase.scroll(centro));
        return pagina;
    }

    public void mostrarAvisosSiHay() {
        List<String> avisos = cliente.tomarAvisos();
        if (avisos.isEmpty()) {
            return;
        }
        Avisos.info("Tienes un aviso",
                String.join("\n", avisos) + "\nRevisa Mi tarjeta o Mis puntos.");
    }
}
