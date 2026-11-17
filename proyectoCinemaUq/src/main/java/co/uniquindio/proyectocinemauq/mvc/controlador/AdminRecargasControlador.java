package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.Formularios;
import co.uniquindio.proyectocinemauq.mvc.vista.Lectura;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/** Solo el administrador recarga. El movimiento queda en la tarjeta del cliente. */
public class AdminRecargasControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ComboBox<Cliente> clientes = new ComboBox<>();
        clientes.getItems().setAll(cine.getClientesRegistrados());
        clientes.setMaxWidth(Double.MAX_VALUE);
        clientes.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) {
                    return "";
                }
                return cliente.getNombre() + " · " + Textos.pesos(cliente.getTarjeta().getSaldo());
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
        TextField monto = new TextField();
        monto.setPromptText("50000");
        Button recargar = VistaBase.primario("Recargar tarjeta");
        recargar.setOnAction(evento -> {
            try {
                Cliente destino = clientes.getValue();
                double valor = Lectura.decimal(monto.getText(), "Escribe el valor en pesos, por ejemplo 50000.");
                cine.recargarTarjeta(Navegacion.getSesion(), destino, valor);
                double saldo = destino.getTarjeta().getSaldo();
                clientes.setValue(null);
                clientes.getItems().setAll(cine.getClientesRegistrados());
                monto.clear();
                Avisos.info("Recarga hecha",
                        "La tarjeta de " + destino.getNombre() + " quedó en " + Textos.pesos(saldo)
                                + ". El movimiento ya aparece en Mi tarjeta.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo recargar", e.getMessage());
            }
        });
        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("El cliente no puede recargarse solo. Esta acción queda registrada."),
                Formularios.campo("Cliente", clientes),
                Formularios.campo("Valor en pesos", monto),
                recargar));
    }
}
