package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.mvc.modelo.EstadisticasCine;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.vista.VistaBase;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

/** Ventas, ocupación y tres gráficas para el administrador. */
public class AdminEstadisticasControlador {

    public Parent crearVista() {
        EstadisticasCine datos = CinemaUQ.getInstancia().estadisticas();
        int libres = Math.max(0, datos.getAsientosTotales() - datos.getAsientosOcupados());
        String ocupacion = datos.getAsientosTotales() == 0
                ? "Todavía no hay funciones con sillas."
                : datos.getAsientosOcupados() + " de " + datos.getAsientosTotales() + " asientos ocupados.";

        VBox centro = VistaBase.conMargen(
                VistaBase.ayuda("Las ventas cuentan las compras que siguen activas y se pagaron con tarjeta."),
                VistaBase.ayuda("Ventas: " + Textos.pesos(datos.getVentasTarjeta())
                        + " · compras activas: " + datos.getComprasActivas()),
                VistaBase.ayuda("Entradas activas: " + datos.getEntradasActivas()),
                VistaBase.ayuda("Película con más entradas: " + datos.getPeliculaTop()),
                VistaBase.ayuda("Producto más vendido: " + datos.getProductoTop()),
                VistaBase.ayuda("Reembolsos: " + datos.getReembolsos()
                        + " · devuelto a tarjetas: " + Textos.pesos(datos.getDineroDevuelto())),
                VistaBase.ayuda("Ocupación: " + ocupacion),
                barras(datos),
                torta(datos, libres),
                linea(datos));
        return VistaBase.scroll(centro);
    }

    private BarChart<String, Number> barras(EstadisticasCine datos) {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Película");
        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Entradas");
        BarChart<String, Number> grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Entradas vendidas por película");
        grafica.setLegendVisible(false);
        grafica.setAnimated(false);
        grafica.setPrefHeight(320);
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Entradas");
        for (Map.Entry<String, Integer> entrada : datos.getEntradasPorPelicula().entrySet()) {
            serie.getData().add(new XYChart.Data<>(entrada.getKey(), entrada.getValue()));
        }
        grafica.getData().add(serie);
        return grafica;
    }

    private PieChart torta(EstadisticasCine datos, int libres) {
        PieChart grafica = new PieChart();
        grafica.setTitle("Ocupación de asientos");
        grafica.setAnimated(false);
        grafica.setPrefHeight(320);
        if (datos.getAsientosTotales() == 0) {
            grafica.setTitle("Ocupación: aún no hay sillas en funciones");
            return grafica;
        }
        grafica.getData().add(new PieChart.Data("Ocupados (" + datos.getAsientosOcupados() + ")", datos.getAsientosOcupados()));
        grafica.getData().add(new PieChart.Data("Libres (" + libres + ")", libres));
        return grafica;
    }

    private LineChart<String, Number> linea(EstadisticasCine datos) {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Día");
        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Pesos");
        LineChart<String, Number> grafica = new LineChart<>(ejeX, ejeY);
        grafica.setTitle("Ventas y recargas por día");
        grafica.setAnimated(false);
        grafica.setPrefHeight(320);
        TreeSet<LocalDate> dias = new TreeSet<>();
        dias.addAll(datos.getVentasPorDia().keySet());
        dias.addAll(datos.getRecargasPorDia().keySet());
        if (dias.isEmpty()) {
            dias.add(LocalDate.now());
        }
        XYChart.Series<String, Number> ventas = new XYChart.Series<>();
        ventas.setName("Ventas");
        XYChart.Series<String, Number> recargas = new XYChart.Series<>();
        recargas.setName("Recargas");
        for (LocalDate dia : dias) {
            String etiqueta = Textos.fechaCorta(dia);
            ventas.getData().add(new XYChart.Data<>(etiqueta, datos.getVentasPorDia().getOrDefault(dia, 0.0)));
            recargas.getData().add(new XYChart.Data<>(etiqueta, datos.getRecargasPorDia().getOrDefault(dia, 0.0)));
        }
        grafica.getData().add(ventas);
        grafica.getData().add(recargas);
        return grafica;
    }
}
