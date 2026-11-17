package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Menú del administrador. Cada sección tiene su propio controlador. */
public class AdminControlador {

    @FXML
    private Label titulo;
    @FXML
    private Button botonEsquina;
    @FXML
    private StackPane contenido;

    private boolean enInicio = true;

    @FXML
    public void initialize() {
        mostrarInicio();
    }

    @FXML
    public void pulsarEsquina() {
        if (enInicio) {
            Navegacion.cerrarSesion();
        } else {
            mostrarInicio();
        }
    }

    @FXML
    public void mostrarInicio() {
        enInicio = true;
        titulo.setText("Panel del administrador");
        botonEsquina.setText("Cerrar sesión");
        Button funciones = VistaBase.primario("Ir a funciones");
        funciones.setOnAction(evento -> mostrarFunciones());
        Button recargas = VistaBase.grande("Recargar tarjeta");
        recargas.setOnAction(evento -> mostrarRecargas());
        Button estadisticas = VistaBase.grande("Ver estadísticas");
        estadisticas.setOnAction(evento -> mostrarEstadisticas());
        Button reportes = VistaBase.grande("Ver reportes");
        reportes.setOnAction(evento -> mostrarReportes());
        VBox bienvenida = VistaBase.conMargen(
                VistaBase.ayuda("Desde aquí programas la cartelera, recargas tarjetas y miras cómo va el cine."),
                funciones,
                recargas,
                estadisticas,
                reportes);
        contenido.getChildren().setAll(VistaBase.scroll(bienvenida));
    }

    @FXML
    public void mostrarPeliculas() {
        abrir("Películas", new AdminPeliculasControlador().crearVista());
    }

    @FXML
    public void mostrarSalas() {
        abrir("Salas", new AdminSalasControlador().crearVista());
    }

    @FXML
    public void mostrarFunciones() {
        abrir("Funciones", new AdminFuncionesControlador().crearVista());
    }

    @FXML
    public void mostrarConfiteria() {
        abrir("Confitería", new AdminConfiteriaControlador().crearVista());
    }

    @FXML
    public void mostrarPromociones() {
        abrir("Promociones", new AdminPromocionesControlador().crearVista());
    }

    @FXML
    public void mostrarClientes() {
        abrir("Clientes", new AdminClientesControlador().crearVista());
    }

    @FXML
    public void mostrarCompras() {
        abrir("Compras", new AdminComprasControlador().crearVista());
    }

    @FXML
    public void mostrarRecargas() {
        abrir("Recargas", new AdminRecargasControlador().crearVista());
    }

    @FXML
    public void mostrarEstadisticas() {
        abrir("Estadísticas", new AdminEstadisticasControlador().crearVista());
    }

    @FXML
    public void mostrarReportes() {
        abrir("Reportes", new AdminReportesControlador().crearVista());
    }

    private void abrir(String nombre, Parent vista) {
        enInicio = false;
        titulo.setText(nombre);
        botonEsquina.setText("Volver");
        contenido.getChildren().setAll(vista);
    }
}
