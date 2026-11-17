package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** Paso 4: el cliente revisa y paga. Aquí entran builder, fachada, cadena, estrategia y proxy. */
public class PagoControlador {

    public Parent crearVista() {
        Cliente cliente = Navegacion.getSesion();
        CompraEnCurso curso = Navegacion.getCompraEnCurso();
        CinemaUQ cine = CinemaUQ.getInstancia();
        BorderPane pagina = VistaBase.pagina("Pago", "Volver", () -> Navegacion.irConfiteria(),
                "Paso 4 de 4 · Revisa y paga");

        Compra vista;
        try {
            vista = ArmadorCompra.armar(cliente, curso, MedioPago.TARJETA, "vista");
        } catch (ExcepcionCinema e) {
            pagina.setCenter(VistaBase.conMargen(VistaBase.ayuda(e.getMessage())));
            return pagina;
        }

        VBox lineas = new VBox(6);
        Map<String, Integer> conteo = new LinkedHashMap<>();
        Map<String, Double> precio = new LinkedHashMap<>();
        Map<String, Integer> puntos = new LinkedHashMap<>();
        for (Comprable item : vista.getItems()) {
            Integer cantidad = conteo.get(item.getNombre());
            if (cantidad == null) {
                conteo.put(item.getNombre(), 1);
                precio.put(item.getNombre(), item.getPrecio());
                puntos.put(item.getNombre(), item.getPuntosRedencion());
            } else {
                conteo.put(item.getNombre(), cantidad + 1);
            }
        }
        for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
            lineas.getChildren().add(new Label(entrada.getValue() + " × " + entrada.getKey()
                    + " · " + Textos.pesos(precio.get(entrada.getKey()))
                    + " · " + puntos.get(entrada.getKey()) + " puntos c/u"));
        }
        lineas.getChildren().add(new Label("Sillas: " + curso.textoAsientos().replace("Apartadas: ", "")));
        lineas.getChildren().add(new Label("Subtotal: " + Textos.pesos(vista.getSubtotal())));
        if (vista.getDescuentoPromocion() > 0) {
            lineas.getChildren().add(new Label("Promoción " + vista.getDescripcionPromocion()
                    + ": -" + Textos.pesos(vista.getDescuentoPromocion())));
        }
        Label totalPesos = new Label("Pagas con tarjeta: " + Textos.pesos(vista.getTotal()));
        totalPesos.getStyleClass().add("grande");
        Label totalPuntos = new Label("Pagas con puntos: " + Textos.puntos(vista.getPuntosRequeridos()));
        totalPuntos.getStyleClass().add("subtitulo");

        LocalDateTime ahora = LocalDateTime.now();
        int vigentes = cliente.getCuentaPuntos().puntosDisponibles(ahora);
        String textoTarjeta = textoTarjeta(cliente, vista.getTotal());
        String textoPuntos = vigentes >= vista.getPuntosRequeridos()
                ? "Tienes " + Textos.puntos(vigentes) + " vigentes. Si pagas con puntos no sumas puntos nuevos."
                : "No te alcanzan los puntos vigentes. Esta compra vale " + vista.getPuntosRequeridos()
                + " y tienes " + vigentes + ". Los vencidos no se pueden usar.";

        Button tarjeta = VistaBase.primario("Pagar con tarjeta");
        Button conPuntos = VistaBase.secundario("Pagar con puntos");
        tarjeta.setOnAction(evento -> pagar(cine, cliente, curso, MedioPago.TARJETA));
        conPuntos.setOnAction(evento -> pagar(cine, cliente, curso, MedioPago.PUNTOS));

        VBox centro = VistaBase.conMargen(
                new Label(vista.getFuncion().getPelicula().getTitulo() + " · "
                        + Textos.fechaHora(vista.getFuncion().getFechaHora())),
                lineas,
                totalPesos,
                totalPuntos,
                VistaBase.ayuda(textoTarjeta),
                VistaBase.ayuda(textoPuntos),
                VistaBase.ayuda("1 punto por cada $1.000 pagados con tarjeta. La parte pagada con puntos no acumula."));
        pagina.setCenter(VistaBase.scroll(centro));
        pagina.setBottom(VistaBase.barra(conPuntos, tarjeta));
        return pagina;
    }

    private void pagar(CinemaUQ cine, Cliente cliente, CompraEnCurso curso, MedioPago medio) {
        try {
            Compra compra = cine.comprar(ArmadorCompra.armar(cliente, curso, medio, cine.nuevoIdCompra()));
            curso.cerrarCompraExitosa();
            String mensaje;
            if (medio == MedioPago.TARJETA) {
                mensaje = "Compra confirmada. El saldo quedó en "
                        + Textos.pesos(cliente.getTarjeta().getSaldo());
                if (compra.getPuntosAcumulados() > 0) {
                    mensaje += " y sumaste " + compra.getPuntosAcumulados() + " puntos";
                }
                mensaje += ". Puedes verla en Mis compras.";
            } else {
                int quedan = cliente.getCuentaPuntos().puntosDisponibles(LocalDateTime.now());
                mensaje = "Compra confirmada. Pagaste con puntos y te quedan "
                        + Textos.puntos(quedan) + " vigentes. Puedes verla en Mis compras.";
            }
            Avisos.info("Compra confirmada", mensaje);
            Navegacion.irCompras();
        } catch (ExcepcionCinema e) {
            Avisos.error("No se pudo pagar", e.getMessage());
        }
    }

    private String textoTarjeta(Cliente cliente, double total) {
        if (!cliente.getTarjeta().estaActiva()) {
            return "Tu tarjeta está inactiva. Pide al administrador que la active en Clientes.";
        }
        if (cliente.getTarjeta().getSaldo() < total) {
            return "No te alcanza el saldo. La compra vale " + Textos.pesos(total)
                    + " y tu tarjeta tiene " + Textos.pesos(cliente.getTarjeta().getSaldo()) + ".";
        }
        return "Tu saldo es " + Textos.pesos(cliente.getTarjeta().getSaldo()) + ".";
    }
}
