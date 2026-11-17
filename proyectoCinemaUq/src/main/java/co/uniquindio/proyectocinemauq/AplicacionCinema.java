package co.uniquindio.proyectocinemauq;

import co.uniquindio.proyectocinemauq.mvc.controlador.Navegacion;
import javafx.application.Application;
import javafx.stage.Stage;

public class AplicacionCinema extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) {
        Navegacion.iniciar(escenario);
        Navegacion.irLogin();
        escenario.show();
    }
}
