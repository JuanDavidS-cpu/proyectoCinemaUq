package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.composite.Combo;
import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.estructural.composite.ProductoConfiteria;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Paso 3: tipo de entrada, gafas 3D y confitería. */
public class ConfiteriaControlador {

    public Parent crearVista() {
        CompraEnCurso curso = Navegacion.getCompraEnCurso();
        CinemaUQ cine = CinemaUQ.getInstancia();
        BorderPane pagina = VistaBase.pagina("Entradas y confitería", "Volver", () -> Navegacion.irAsientos(),
                "Paso 3 de 4 · Entradas y confitería");

        ToggleGroup tipos = new ToggleGroup();
        RadioButton general = new RadioButton("General · " + Textos.pesos(ReglasPuntos.PRECIO_ENTRADA_GENERAL)
                + " · " + ReglasPuntos.PUNTOS_ENTRADA_GENERAL + " puntos");
        RadioButton preferencial = new RadioButton("Preferencial · " + Textos.pesos(ReglasPuntos.PRECIO_ENTRADA_PREFERENCIAL)
                + " · " + ReglasPuntos.PUNTOS_ENTRADA_PREFERENCIAL + " puntos");
        general.setToggleGroup(tipos);
        preferencial.setToggleGroup(tipos);
        if (curso.isPreferencial()) {
            preferencial.setSelected(true);
        } else {
            general.setSelected(true);
        }

        CheckBox gafas = new CheckBox("Agregar gafas 3D a cada entrada (+ "
                + Textos.pesos(ReglasPuntos.PRECIO_GAFAS) + ")");
        gafas.setSelected(curso.isGafas());

        Label total = new Label();
        total.setWrapText(true);
        total.getStyleClass().add("subtitulo");

        general.setOnAction(evento -> {
            curso.setPreferencial(false);
            actualizarTotal(curso, total);
        });
        preferencial.setOnAction(evento -> {
            curso.setPreferencial(true);
            actualizarTotal(curso, total);
        });
        gafas.setOnAction(evento -> {
            curso.setGafas(gafas.isSelected());
            actualizarTotal(curso, total);
        });

        VBox productos = new VBox(10);
        if (cine.getProductos().isEmpty() && cine.getCombos().isEmpty()) {
            productos.getChildren().add(VistaBase.ayuda("Aún no hay productos de confitería."));
        }
        for (ProductoConfiteria producto : cine.getProductos()) {
            productos.getChildren().add(filaCantidad(curso, producto, producto.toString(), total));
        }
        for (Combo combo : cine.getCombos()) {
            productos.getChildren().add(filaCantidad(curso, combo, combo.toString(), total));
        }
        actualizarTotal(curso, total);

        Button resumen = VistaBase.primario("Ver resumen");
        resumen.setOnAction(evento -> Navegacion.irPago());

        VBox centro = VistaBase.conMargen(
                VistaBase.ayuda("El tipo y las gafas se aplican a todas las sillas de esta compra ("
                        + curso.getAsientos().size() + ")."),
                VistaBase.subtitulo("Tipo de entrada"),
                general,
                preferencial,
                gafas,
                VistaBase.subtitulo("Confitería"),
                VistaBase.ayuda(curso.textoPromocionHoy()),
                productos,
                total);
        pagina.setCenter(VistaBase.scroll(centro));
        pagina.setBottom(VistaBase.barra(resumen));
        return pagina;
    }

    private HBox filaCantidad(CompraEnCurso curso, Comprable item, String nombre, Label total) {
        Label texto = new Label(nombre);
        texto.setWrapText(true);
        texto.setMaxWidth(520);
        Label numero = new Label(String.valueOf(curso.getCantidad(item)));
        numero.setMinWidth(24);
        numero.setAlignment(Pos.CENTER);
        Button menos = new Button("−");
        Button mas = new Button("+");
        menos.getStyleClass().add("boton-secundario");
        mas.getStyleClass().add("boton-secundario");
        menos.setOnAction(evento -> cambiar(curso, item, -1, numero, total));
        mas.setOnAction(evento -> cambiar(curso, item, 1, numero, total));
        HBox fila = new HBox(8, texto, menos, numero, mas);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    private void cambiar(CompraEnCurso curso, Comprable item, int delta, Label numero, Label total) {
        int nuevo = curso.getCantidad(item) + delta;
        if (nuevo > 10) {
            co.uniquindio.proyectocinemauq.mvc.vista.Avisos.error("Cantidad",
                    "Puedes llevar hasta 10 de cada producto.");
            return;
        }
        curso.setCantidad(item, Math.max(0, nuevo));
        numero.setText(String.valueOf(curso.getCantidad(item)));
        actualizarTotal(curso, total);
    }

    private void actualizarTotal(CompraEnCurso curso, Label total) {
        try {
            Compra vista = ArmadorCompra.armar(Navegacion.getSesion(), curso, MedioPago.TARJETA, "vista");
            String promo = vista.getDescuentoPromocion() > 0
                    ? " Promoción: -" + Textos.pesos(vista.getDescuentoPromocion()) + "."
                    : "";
            total.setText("Total estimado: " + Textos.pesos(vista.getTotal())
                    + " · o " + Textos.puntos(vista.getPuntosRequeridos()) + "." + promo);
        } catch (ExcepcionCinema e) {
            total.setText(e.getMessage());
        }
    }
}
