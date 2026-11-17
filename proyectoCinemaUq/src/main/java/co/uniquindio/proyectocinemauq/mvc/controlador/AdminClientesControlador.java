package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/** Consulta de clientes y activación de cuenta o tarjeta. */
public class AdminClientesControlador {

    public Parent crearVista() {
        CinemaUQ cine = CinemaUQ.getInstancia();
        ListView<Cliente> lista = new ListView<>();
        lista.setCellFactory(vista -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.textoAdmin());
            }
        });
        Runnable refrescar = () -> lista.getItems().setAll(cine.getClientesRegistrados());
        refrescar.run();
        lista.setPlaceholder(new javafx.scene.control.Label("Aún no hay clientes."));
        lista.setPrefHeight(320);

        Button tarjeta = VistaBase.primario("Activar o desactivar tarjeta");
        tarjeta.setOnAction(evento -> {
            Cliente cliente = lista.getSelectionModel().getSelectedItem();
            if (cliente == null) {
                Avisos.error("Elige un cliente", "Selecciona a la persona en la lista.");
                return;
            }
            try {
                boolean activar = !cliente.getTarjeta().estaActiva();
                cine.cambiarTarjetaActiva(Navegacion.getSesion(), cliente, activar);
                refrescar.run();
                Avisos.info("Tarjeta actualizada",
                        activar
                                ? "La tarjeta de " + cliente.getNombre() + " quedó activa. Ya puede pagar."
                                : "La tarjeta de " + cliente.getNombre() + " quedó inactiva. No podrá pagar hasta que la actives.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo cambiar", e.getMessage());
            }
        });
        Button cuenta = VistaBase.secundario("Activar o desactivar cuenta");
        cuenta.setOnAction(evento -> {
            Cliente cliente = lista.getSelectionModel().getSelectedItem();
            if (cliente == null) {
                Avisos.error("Elige un cliente", "Selecciona a la persona en la lista.");
                return;
            }
            try {
                boolean activar = !cliente.isActivo();
                cine.cambiarCuentaActiva(Navegacion.getSesion(), cliente, activar);
                refrescar.run();
                Avisos.info("Cuenta actualizada",
                        activar
                                ? cliente.getNombre() + " ya puede iniciar sesión."
                                : cliente.getNombre() + " no podrá entrar hasta que actives la cuenta.");
            } catch (ExcepcionCinema e) {
                Avisos.error("No se pudo cambiar", e.getMessage());
            }
        });
        return VistaBase.scroll(VistaBase.conMargen(
                VistaBase.ayuda("La recarga se hace en la sección Recargas. Aquí solo ves y activas."),
                lista,
                tarjeta,
                cuenta));
    }
}
