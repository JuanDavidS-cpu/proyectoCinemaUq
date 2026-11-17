package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.composite.ProductoConfiteria;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Promocion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoPromocion;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;

/** Promociones aparte del descuento que ya trae el combo. */
public class AdminPromocionesControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<Promocion> lista = new ListView<>();
        lista.getItems().setAll(cine.getPromociones());
        lista.setPlaceholder(new javafx.scene.control.Label("Aún no hay promociones."));
        lista.setPrefHeight(180);

        TextField nombre = new TextField();
        ComboBox<TipoPromocion> tipo = new ComboBox<>();
        tipo.getItems().addAll(TipoPromocion.PORCENTAJE_DIA, TipoPromocion.DOS_POR_UNO);
        tipo.setValue(TipoPromocion.PORCENTAJE_DIA);
        tipo.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(TipoPromocion object) {
                if (object == TipoPromocion.DOS_POR_UNO) {
                    return "2x1 de un producto";
                }
                if (object == TipoPromocion.PORCENTAJE_DIA) {
                    return "Porcentaje por día";
                }
                return "";
            }

            @Override
            public TipoPromocion fromString(String string) {
                return null;
            }
        });
        tipo.setMaxWidth(Double.MAX_VALUE);
        ComboBox<DayOfWeek> dia = new ComboBox<>();
        dia.getItems().addAll(DayOfWeek.values());
        dia.setValue(java.time.LocalDate.now().getDayOfWeek());
        dia.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(DayOfWeek object) {
                return object == null ? "" : Textos.dia(object);
            }

            @Override
            public DayOfWeek fromString(String string) {
                return null;
            }
        });
        dia.setMaxWidth(Double.MAX_VALUE);
        TextField porcentaje = new TextField("20");
        ComboBox<ProductoConfiteria> producto = new ComboBox<>();
        producto.getItems().setAll(cine.getProductos());
        producto.setMaxWidth(Double.MAX_VALUE);

        VBox cajaDia = Formularios.campo("Día", dia);
        VBox cajaPorcentaje = Formularios.campo("Porcentaje (1 a 90)", porcentaje);
        VBox cajaProducto = Formularios.campo("Producto del 2x1", producto);
        cajaProducto.setVisible(false);
        cajaProducto.setManaged(false);
        tipo.setOnAction(evento -> {
            boolean dosPorUno = tipo.getValue() == TipoPromocion.DOS_POR_UNO;
            cajaDia.setVisible(!dosPorUno);
            cajaDia.setManaged(!dosPorUno);
            cajaPorcentaje.setVisible(!dosPorUno);
            cajaPorcentaje.setManaged(!dosPorUno);
            cajaProducto.setVisible(dosPorUno);
            cajaProducto.setManaged(dosPorUno);
        });

        Button crear = VistaBase.primario("Crear promoción");
        crear.setOnAction(evento -> {
            try {
                double fraccion = 0;
                String nombreProducto = null;
                if (tipo.getValue() == TipoPromocion.PORCENTAJE_DIA) {
                    fraccion = Lectura.entero(porcentaje.getText(), "Escribe el porcentaje, por ejemplo 20.") / 100.0;
                } else if (producto.getValue() != null) {
                    nombreProducto = producto.getValue().getNombre();
                }
                cine.agregarPromocion(Navegacion.getSesion(), nombre.getText(), tipo.getValue(),
                        dia.getValue(), fraccion, nombreProducto);
                lista.getItems().setAll(cine.getPromociones());
                nombre.clear();
                Avisos.info("Promoción creada",
                        "Se aplica al precio ya calculado. No vuelve a descontar lo de adentro del combo.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo crear", e.getMessage());
            }
        });

        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("El porcentaje del día solo toca confitería. El 2x1 solo toca el producto suelto."),
                lista,
                VistaBase.subtitulo("Nueva promoción"),
                Formularios.campo("Nombre", nombre),
                Formularios.campo("Tipo", tipo),
                cajaDia,
                cajaPorcentaje,
                cajaProducto,
                crear));
    }
}
