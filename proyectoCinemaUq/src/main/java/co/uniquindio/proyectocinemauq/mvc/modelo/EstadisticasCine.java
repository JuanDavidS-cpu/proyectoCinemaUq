package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Cifras que el administrador ve en pantalla, aparte del archivo de reporte. */
public class EstadisticasCine {

    private final double ventasTarjeta;
    private final int comprasActivas;
    private final int entradasActivas;
    private final int reembolsos;
    private final double dineroDevuelto;
    private final String peliculaTop;
    private final String productoTop;
    private final int asientosOcupados;
    private final int asientosTotales;
    private final Map<String, Integer> entradasPorPelicula;
    private final Map<LocalDate, Double> ventasPorDia;
    private final Map<LocalDate, Double> recargasPorDia;

    public EstadisticasCine(double ventasTarjeta, int comprasActivas, int entradasActivas, int reembolsos,
                            double dineroDevuelto, String peliculaTop, String productoTop, int asientosOcupados,
                            int asientosTotales, Map<String, Integer> entradasPorPelicula,
                            Map<LocalDate, Double> ventasPorDia, Map<LocalDate, Double> recargasPorDia) {
        this.ventasTarjeta = ventasTarjeta;
        this.comprasActivas = comprasActivas;
        this.entradasActivas = entradasActivas;
        this.reembolsos = reembolsos;
        this.dineroDevuelto = dineroDevuelto;
        this.peliculaTop = peliculaTop;
        this.productoTop = productoTop;
        this.asientosOcupados = asientosOcupados;
        this.asientosTotales = asientosTotales;
        this.entradasPorPelicula = entradasPorPelicula;
        this.ventasPorDia = ventasPorDia;
        this.recargasPorDia = recargasPorDia;
    }

    public static EstadisticasCine calcular(List<Compra> compras, List<Funcion> funciones,
                                            List<Pelicula> peliculas, List<Cliente> clientes) {
        double ventas = 0;
        int activas = 0;
        int entradas = 0;
        int reembolsos = 0;
        double devuelto = 0;
        Map<String, Integer> porPelicula = new LinkedHashMap<>();
        for (Pelicula pelicula : peliculas) {
            porPelicula.put(pelicula.getTitulo(), 0);
        }
        Map<String, Integer> porProducto = new LinkedHashMap<>();
        Map<LocalDate, Double> ventasDia = new LinkedHashMap<>();
        for (Compra compra : compras) {
            if (compra.getEstado() == EstadoCompra.CANCELADA || compra.getEstado() == EstadoCompra.REEMBOLSADA) {
                reembolsos++;
                devuelto += compra.getMontoReembolsado();
            }
            if (compra.getEstado() != EstadoCompra.ACTIVA) {
                continue;
            }
            activas++;
            if (compra.getMedioPago() == MedioPago.TARJETA) {
                ventas += compra.getTotal();
                sumarDecimal(ventasDia, compra.getFecha().toLocalDate(), compra.getTotal());
            }
            for (Comprable item : compra.getItems()) {
                if (item.esEntrada()) {
                    entradas++;
                    sumarEntero(porPelicula, compra.getFuncion().getPelicula().getTitulo(), 1);
                } else {
                    sumarEntero(porProducto, item.getNombre(), 1);
                }
            }
        }
        int ocupados = 0;
        int cupo = 0;
        for (Funcion funcion : funciones) {
            ocupados += (int) funcion.contarOcupados();
            cupo += funcion.getAsientos().size();
        }
        Map<LocalDate, Double> recargasDia = new LinkedHashMap<>();
        for (Cliente cliente : clientes) {
            for (MovimientoTarjeta movimiento : cliente.getTarjeta().getMovimientos()) {
                if (movimiento.getTipo() == TipoMovimiento.RECARGA) {
                    sumarDecimal(recargasDia, movimiento.getFecha().toLocalDate(), movimiento.getValor());
                }
            }
        }
        return new EstadisticasCine(ventas, activas, entradas, reembolsos, devuelto,
                ganador(porPelicula, "Aún no hay entradas vendidas."),
                ganador(porProducto, "Aún no se ha vendido confitería."),
                ocupados, cupo, porPelicula, ventasDia, recargasDia);
    }

    private static String ganador(Map<String, Integer> conteo, String vacio) {
        String mejor = null;
        int maximo = 0;
        for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
            if (entrada.getValue() > maximo) {
                maximo = entrada.getValue();
                mejor = entrada.getKey();
            }
        }
        if (mejor == null || maximo == 0) {
            return vacio;
        }
        return mejor + " (" + maximo + ")";
    }

    public double getVentasTarjeta() {
        return ventasTarjeta;
    }

    public int getComprasActivas() {
        return comprasActivas;
    }

    public int getEntradasActivas() {
        return entradasActivas;
    }

    public int getReembolsos() {
        return reembolsos;
    }

    public double getDineroDevuelto() {
        return dineroDevuelto;
    }

    public String getPeliculaTop() {
        return peliculaTop;
    }

    public String getProductoTop() {
        return productoTop;
    }

    public int getAsientosOcupados() {
        return asientosOcupados;
    }

    public int getAsientosTotales() {
        return asientosTotales;
    }

    public Map<String, Integer> getEntradasPorPelicula() {
        return entradasPorPelicula;
    }

    public Map<LocalDate, Double> getVentasPorDia() {
        return ventasPorDia;
    }

    public Map<LocalDate, Double> getRecargasPorDia() {
        return recargasPorDia;
    }

    private static void sumarEntero(Map<String, Integer> mapa, String clave, int valor) {
        Integer actual = mapa.get(clave);
        if (actual == null) {
            mapa.put(clave, valor);
        } else {
            mapa.put(clave, actual + valor);
        }
    }

    private static void sumarDecimal(Map<LocalDate, Double> mapa, LocalDate clave, double valor) {
        Double actual = mapa.get(clave);
        if (actual == null) {
            mapa.put(clave, valor);
        } else {
            mapa.put(clave, actual + valor);
        }
    }
}
