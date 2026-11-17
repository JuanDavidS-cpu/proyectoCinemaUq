package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Arma las cifras del reporte. No escribe el archivo: eso es del exportador.
 */
public class GeneradorReporte {

    public Reporte generar(TipoReporte tipo, LocalDate desde, LocalDate hasta,
                           List<Compra> compras, List<Funcion> funciones,
                           List<Cliente> clientes, List<Pelicula> peliculas) {
        return switch (tipo) {
            case VENTAS -> ventas(desde, hasta, compras);
            case ENTRADAS -> entradas(desde, hasta, compras, funciones, peliculas);
            case OCUPACION -> ocupacion(desde, hasta, funciones);
            case PRODUCTOS -> productos(desde, hasta, compras);
            case RECARGAS -> recargas(desde, hasta, clientes);
            case REEMBOLSOS -> reembolsos(desde, hasta, compras);
            case PUNTOS -> puntos(desde, hasta, clientes);
        };
    }

    private Reporte ventas(LocalDate desde, LocalDate hasta, List<Compra> compras) {
        List<String> lineas = new ArrayList<>();
        double dinero = 0;
        int activas = 0;
        for (Compra compra : compras) {
            if (!enPeriodo(compra.getFecha(), desde, hasta)) {
                continue;
            }
            lineas.add(compra.getId() + " · " + compra.getCliente().getNombre()
                    + " · " + compra.getFuncion().getPelicula().getTitulo()
                    + " · " + compra.textoTotal() + " · " + compra.textoEstado());
            if (compra.getEstado() == EstadoCompra.ACTIVA && compra.getMedioPago() == MedioPago.TARJETA) {
                dinero += compra.getTotal();
                activas++;
            }
        }
        return new Reporte("CinemaUQ — Ventas por periodo", desde, hasta, lineas,
                "Compras activas pagadas con tarjeta: " + activas + "\nDinero: " + Textos.pesos(dinero));
    }

    private Reporte entradas(LocalDate desde, LocalDate hasta, List<Compra> compras,
                             List<Funcion> funciones, List<Pelicula> peliculas) {
        Map<String, Integer> porPelicula = new LinkedHashMap<>();
        for (Pelicula pelicula : peliculas) {
            porPelicula.put(pelicula.getTitulo(), 0);
        }
        List<String> lineas = new ArrayList<>();
        int total = 0;
        for (Funcion funcion : funciones) {
            if (!enPeriodo(funcion.getFechaHora(), desde, hasta)) {
                continue;
            }
            int cantidad = contarEntradas(compras, funcion);
            total += cantidad;
            sumarEntero(porPelicula, funcion.getPelicula().getTitulo(), cantidad);
            lineas.add(funcion.getPelicula().getTitulo() + " · " + funcion.getSala().getNombre()
                    + " · " + Textos.fechaHora(funcion.getFechaHora()) + " · " + cantidad + " entradas");
        }
        lineas.add("— Por película —");
        for (Map.Entry<String, Integer> entrada : porPelicula.entrySet()) {
            lineas.add(entrada.getKey() + ": " + entrada.getValue() + " entradas");
        }
        return new Reporte("CinemaUQ — Entradas por película o función", desde, hasta, lineas,
                "Entradas en compras activas: " + total);
    }

    private Reporte ocupacion(LocalDate desde, LocalDate hasta, List<Funcion> funciones) {
        List<String> lineas = new ArrayList<>();
        long ocupados = 0;
        long cupo = 0;
        for (Funcion funcion : funciones) {
            if (!enPeriodo(funcion.getFechaHora(), desde, hasta)) {
                continue;
            }
            long ocupadas = funcion.contarOcupados();
            long total = funcion.getAsientos().size();
            ocupados += ocupadas;
            cupo += total;
            lineas.add(funcion.getSala().getNombre() + " · " + funcion.getPelicula().getTitulo()
                    + " · " + Textos.fechaHora(funcion.getFechaHora())
                    + " · " + ocupadas + " de " + total + " ocupados");
        }
        String totales = cupo == 0
                ? "No hay funciones en el periodo."
                : "Ocupados: " + ocupados + " de " + cupo;
        return new Reporte("CinemaUQ — Ocupación de salas", desde, hasta, lineas, totales);
    }

    private Reporte productos(LocalDate desde, LocalDate hasta, List<Compra> compras) {
        Map<String, Integer> unidades = new LinkedHashMap<>();
        Map<String, Double> dinero = new LinkedHashMap<>();
        for (Compra compra : compras) {
            if (compra.getEstado() != EstadoCompra.ACTIVA || !enPeriodo(compra.getFecha(), desde, hasta)) {
                continue;
            }
            for (Comprable item : compra.getItems()) {
                if (item.esEntrada()) {
                    continue;
                }
                String nombre = item.getNombre() + (item.esCombo() ? " (combo)" : "");
                sumarEntero(unidades, nombre, 1);
                sumarDecimal(dinero, nombre, item.getPrecio());
            }
        }
        List<String> lineas = new ArrayList<>();
        int unidadesTotal = 0;
        double dineroTotal = 0;
        for (Map.Entry<String, Integer> entrada : unidades.entrySet()) {
            lineas.add(entrada.getKey() + " · " + entrada.getValue() + " und. · "
                    + Textos.pesos(dinero.get(entrada.getKey())));
            unidadesTotal += entrada.getValue();
            dineroTotal += dinero.get(entrada.getKey());
        }
        return new Reporte("CinemaUQ — Productos y combos vendidos", desde, hasta, lineas,
                "Unidades: " + unidadesTotal + "\nDinero de confitería (sin repetir lo de adentro del combo): "
                        + Textos.pesos(dineroTotal));
    }

    private Reporte recargas(LocalDate desde, LocalDate hasta, List<Cliente> clientes) {
        List<String> lineas = new ArrayList<>();
        double total = 0;
        int cantidad = 0;
        for (Cliente cliente : clientes) {
            for (MovimientoTarjeta movimiento : cliente.getTarjeta().getMovimientos()) {
                if (movimiento.getTipo() != TipoMovimiento.RECARGA) {
                    continue;
                }
                if (!enPeriodo(movimiento.getFecha(), desde, hasta)) {
                    continue;
                }
                lineas.add(cliente.getNombre() + " · " + Textos.pesos(movimiento.getValor())
                        + " · " + movimiento.getDescripcion() + " · " + Textos.fechaHora(movimiento.getFecha()));
                total += movimiento.getValor();
                cantidad++;
            }
        }
        return new Reporte("CinemaUQ — Recargas", desde, hasta, lineas,
                "Recargas: " + cantidad + "\nDinero: " + Textos.pesos(total));
    }

    private Reporte reembolsos(LocalDate desde, LocalDate hasta, List<Compra> compras) {
        List<String> lineas = new ArrayList<>();
        double dinero = 0;
        int puntos = 0;
        int cantidad = 0;
        for (Compra compra : compras) {
            if (compra.getFechaReembolso() == null || !enPeriodo(compra.getFechaReembolso(), desde, hasta)) {
                continue;
            }
            lineas.add(compra.getId() + " · " + compra.getCliente().getNombre()
                    + " · " + compra.textoEstado()
                    + " · tarjeta " + Textos.pesos(compra.getMontoReembolsado())
                    + " · puntos " + compra.getPuntosDevueltos()
                    + " · " + Textos.fechaHora(compra.getFechaReembolso()));
            dinero += compra.getMontoReembolsado();
            puntos += compra.getPuntosDevueltos();
            cantidad++;
        }
        return new Reporte("CinemaUQ — Cancelaciones y reembolsos", desde, hasta, lineas,
                "Operaciones: " + cantidad + "\nDevuelto a tarjetas: " + Textos.pesos(dinero)
                        + "\nPuntos devueltos: " + puntos);
    }

    private Reporte puntos(LocalDate desde, LocalDate hasta, List<Cliente> clientes) {
        List<String> lineas = new ArrayList<>();
        int acumulados = 0;
        int redimidos = 0;
        for (Cliente cliente : clientes) {
            for (MovimientoPuntos movimiento : cliente.getCuentaPuntos().getHistorial()) {
                if (!enPeriodo(movimiento.getFecha(), desde, hasta)) {
                    continue;
                }
                String tipo = movimiento.getTipo() == TipoMovimientoPuntos.ACUMULADO ? "Acumulados" : "Redimidos";
                lineas.add(cliente.getNombre() + " · " + tipo + " · " + movimiento.getCantidad()
                        + " · " + movimiento.getDescripcion() + " · " + Textos.fechaHora(movimiento.getFecha()));
                if (movimiento.getTipo() == TipoMovimientoPuntos.ACUMULADO) {
                    acumulados += movimiento.getCantidad();
                } else {
                    redimidos += movimiento.getCantidad();
                }
            }
        }
        return new Reporte("CinemaUQ — Puntos acumulados y redimidos", desde, hasta, lineas,
                "Acumulados: " + acumulados + "\nRedimidos: " + redimidos);
    }

    private int contarEntradas(List<Compra> compras, Funcion funcion) {
        int cantidad = 0;
        for (Compra compra : compras) {
            if (compra.getEstado() != EstadoCompra.ACTIVA || compra.getFuncion() != funcion) {
                continue;
            }
            for (Comprable item : compra.getItems()) {
                if (item.esEntrada()) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    private void sumarEntero(Map<String, Integer> mapa, String clave, int valor) {
        Integer actual = mapa.get(clave);
        if (actual == null) {
            mapa.put(clave, valor);
        } else {
            mapa.put(clave, actual + valor);
        }
    }

    private void sumarDecimal(Map<String, Double> mapa, String clave, double valor) {
        Double actual = mapa.get(clave);
        if (actual == null) {
            mapa.put(clave, valor);
        } else {
            mapa.put(clave, actual + valor);
        }
    }

    private boolean enPeriodo(java.time.LocalDateTime fecha, LocalDate desde, LocalDate hasta) {
        if (fecha == null) {
            return false;
        }
        LocalDate dia = fecha.toLocalDate();
        return !dia.isBefore(desde) && !dia.isAfter(hasta);
    }
}
