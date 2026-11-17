package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.excepciones.ExcepcionCinema;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.vista.Avisos;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/** Entrada al cine: iniciar sesión o crear una cuenta. */
public class LoginControlador {

    @FXML
    private TextField identificacionLogin;
    @FXML
    private PasswordField claveLogin;
    @FXML
    private TextField nombreRegistro;
    @FXML
    private TextField identificacionRegistro;
    @FXML
    private PasswordField claveRegistro;

    @FXML
    public void iniciarSesion() {
        String identificacion = identificacionLogin.getText();
        String clave = claveLogin.getText();
        if (identificacion == null || identificacion.isBlank() || clave == null || clave.isBlank()) {
            Avisos.error("Faltan datos", "Escribe la identificación y la clave.");
            return;
        }
        try {
            Cliente cliente = CinemaUQ.getInstancia().iniciarSesion(identificacion, clave);
            if (cliente == null) {
                Avisos.error("No pudimos entrar",
                        "La identificación o la clave no coinciden. Revisa los usuarios de prueba bajo el título.");
                return;
            }
            Navegacion.entrar(cliente);
        } catch (ExcepcionCinema e) {
            Avisos.error("No pudimos entrar", e.getMessage());
        }
    }

    @FXML
    public void registrarme() {
        try {
            Cliente nuevo = CinemaUQ.getInstancia().registrarCliente(
                    nombreRegistro.getText(),
                    identificacionRegistro.getText(),
                    claveRegistro.getText());
            Navegacion.entrar(nuevo);
            Avisos.info("Cuenta creada",
                    "Tu tarjeta empieza en $0. Cuando el administrador la recargue, el saldo aparece en Mi tarjeta.");
        } catch (ExcepcionCinema e) {
            Avisos.error("No se pudo crear la cuenta", e.getMessage());
        }
    }
}
