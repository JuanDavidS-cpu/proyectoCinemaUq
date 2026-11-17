package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Rol;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/** Cambia de pantalla y guarda quién está dentro. */
public final class Navegacion {

    private static final String ESTILOS = "/co/uniquindio/proyectocinemauq/mvc/vista/estilos.css";

    private static Stage escenario;
    private static Cliente sesion;
    private static final CompraEnCurso compraEnCurso = new CompraEnCurso();

    private Navegacion() {
    }

    public static void iniciar(Stage stage) {
        escenario = stage;
        escenario.setTitle("CinemaUQ");
        escenario.setMinWidth(1000);
        escenario.setMinHeight(680);
        escenario.setWidth(1120);
        escenario.setHeight(740);
    }

    public static Stage getEscenario() {
        return escenario;
    }

    public static Cliente getSesion() {
        return sesion;
    }

    public static CompraEnCurso getCompraEnCurso() {
        return compraEnCurso;
    }

    public static void entrar(Cliente cliente) {
        sesion = cliente;
        if (cliente.getRol() == Rol.ADMIN) {
            irAdmin();
        } else {
            irInicioCliente();
        }
    }

    public static void cerrarSesion() {
        compraEnCurso.abandonar();
        sesion = null;
        irLogin();
    }

    public static void irLogin() {
        mostrar(cargar("/co/uniquindio/proyectocinemauq/mvc/vista/login.fxml"));
    }

    public static void irInicioCliente() {
        if (!exigirCliente()) {
            return;
        }
        InicioClienteControlador controlador = new InicioClienteControlador();
        mostrar(controlador.crearVista());
        controlador.mostrarAvisosSiHay();
    }

    public static void irCartelera() {
        if (!exigirCliente()) {
            return;
        }
        mostrar(cargar("/co/uniquindio/proyectocinemauq/mvc/vista/cartelera.fxml"));
    }

    public static void iniciarCompra(Funcion funcion) {
        if (!exigirCliente()) {
            return;
        }
        compraEnCurso.elegirFuncion(funcion);
        irAsientos();
    }

    public static void irAsientos() {
        if (!exigirCliente() || compraEnCurso.getFuncion() == null) {
            irCartelera();
            return;
        }
        mostrar(new AsientosControlador().crearVista());
    }

    public static void irConfiteria() {
        if (!exigirCliente() || compraEnCurso.getAsientos().isEmpty()) {
            irAsientos();
            return;
        }
        mostrar(new ConfiteriaControlador().crearVista());
    }

    public static void irPago() {
        if (!exigirCliente() || compraEnCurso.getAsientos().isEmpty()) {
            irAsientos();
            return;
        }
        mostrar(new PagoControlador().crearVista());
    }

    public static void irCompras() {
        if (!exigirCliente()) {
            return;
        }
        mostrar(new ComprasControlador().crearVista());
    }

    public static void irTarjeta() {
        if (!exigirCliente()) {
            return;
        }
        mostrar(new TarjetaControlador().crearVista());
    }

    public static void irPuntos() {
        if (!exigirCliente()) {
            return;
        }
        mostrar(new PuntosControlador().crearVista());
    }

    public static void irAdmin() {
        if (sesion == null || sesion.getRol() != Rol.ADMIN) {
            irLogin();
            return;
        }
        mostrar(cargar("/co/uniquindio/proyectocinemauq/mvc/vista/admin.fxml"));
    }

    private static boolean exigirCliente() {
        if (sesion == null || sesion.getRol() != Rol.CLIENTE) {
            irLogin();
            return false;
        }
        return true;
    }

    private static void mostrar(Parent raiz) {
        if (escenario.getScene() == null) {
            Scene escena = new Scene(raiz, 1120, 740);
            escena.getStylesheets().add(Objects.requireNonNull(Navegacion.class.getResource(ESTILOS)).toExternalForm());
            escenario.setScene(escena);
        } else {
            if (escenario.getScene().getStylesheets().isEmpty()) {
                escenario.getScene().getStylesheets()
                        .add(Objects.requireNonNull(Navegacion.class.getResource(ESTILOS)).toExternalForm());
            }
            escenario.getScene().setRoot(raiz);
        }
    }

    private static Parent cargar(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(ruta));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("No está la pantalla " + ruta);
            }
            return loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo abrir la pantalla " + ruta, e);
        }
    }
}
