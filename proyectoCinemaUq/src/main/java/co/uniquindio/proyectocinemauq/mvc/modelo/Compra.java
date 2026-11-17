package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.excepciones.ReembolsoDuplicadoException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Compra armada por el Builder. La fachada la cobra; este objeto no cobra solo.
 * El reembolso vuelve a la misma tarjeta, o a puntos con fecha de hoy si se pagó con puntos.
 * No se ejecuta dos veces sobre la misma compra.
 */
public class Compra {

    private final String id;
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Asiento> asientos;
    private final List<Comprable> items;
    private final double subtotal;
    private final double descuentoPromocion;
    private final String descripcionPromocion;
    private final double total;
    private final int puntosRequeridos;
    private final LocalDateTime fecha;
    private final MedioPago medioPago;
    private EstadoCompra estado;
    private int puntosUsados;
    private int puntosAcumulados;
    private double montoReembolsado;
    private int puntosDevueltos;
    private LocalDateTime fechaReembolso;

    public Compra(String id, Cliente cliente, Funcion funcion, List<Asiento> asientos, List<Comprable> items,
                  double subtotal, double descuentoPromocion, String descripcionPromocion, double total,
                  int puntosRequeridos, MedioPago medioPago) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = List.copyOf(asientos);
        this.items = List.copyOf(items);
        this.subtotal = subtotal;
        this.descuentoPromocion = descuentoPromocion;
        this.descripcionPromocion = descripcionPromocion == null ? "" : descripcionPromocion;
        this.total = total;
        this.puntosRequeridos = puntosRequeridos;
        this.medioPago = medioPago;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoCompra.POR_COBRAR;
    }

    public void marcarPagada(int puntosUsados, int puntosAcumulados) {
        this.estado = EstadoCompra.ACTIVA;
        this.puntosUsados = puntosUsados;
        this.puntosAcumulados = puntosAcumulados;
    }

    public void reembolsar(double porcentaje, EstadoCompra nuevoEstado, String motivo) {
        if (estado != EstadoCompra.ACTIVA) {
            throw new ReembolsoDuplicadoException(
                    "Esta compra ya fue reembolsada. No se vuelve a devolver el dinero ni los puntos.");
        }
        if (medioPago == MedioPago.TARJETA) {
            montoReembolsado = Math.round(total * porcentaje);
            int porciento = (int) Math.round(porcentaje * 100);
            cliente.getTarjeta().abonar(montoReembolsado, "Reembolso " + porciento + "%: " + motivo);
        } else {
            puntosDevueltos = (int) Math.round(puntosUsados * porcentaje);
            if (puntosDevueltos > 0) {
                cliente.getCuentaPuntos().acumular(puntosDevueltos, "Reembolso: " + motivo);
            }
        }
        for (Asiento asiento : asientos) {
            if (asiento.estaOcupado() || asiento.estaReservado()) {
                asiento.liberarPorReembolso();
            }
        }
        this.estado = nuevoEstado;
        this.fechaReembolso = LocalDateTime.now();
    }

    public String textoCliente() {
        return id + " · " + funcion.getPelicula().getTitulo()
                + "\n" + Textos.fechaHora(funcion.getFechaHora())
                + " · Sillas " + textoAsientos()
                + " · " + textoTotal()
                + " · " + textoEstado();
    }

    public String textoAsientos() {
        StringBuilder texto = new StringBuilder();
        for (Asiento asiento : asientos) {
            if (!texto.isEmpty()) {
                texto.append(", ");
            }
            texto.append(asiento.getCodigo());
        }
        return texto.toString();
    }

    public String textoTotal() {
        if (medioPago == MedioPago.PUNTOS) {
            return puntosUsados + " puntos";
        }
        return Textos.pesos(total);
    }

    public String textoEstado() {
        return switch (estado) {
            case POR_COBRAR -> "Por cobrar";
            case ACTIVA -> "Activa";
            case CANCELADA -> "Cancelada";
            case REEMBOLSADA -> "Reembolsada";
        };
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public List<Comprable> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuentoPromocion() {
        return descuentoPromocion;
    }

    public String getDescripcionPromocion() {
        return descripcionPromocion;
    }

    public double getTotal() {
        return total;
    }

    public int getPuntosRequeridos() {
        return puntosRequeridos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public int getPuntosUsados() {
        return puntosUsados;
    }

    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public double getMontoReembolsado() {
        return montoReembolsado;
    }

    public int getPuntosDevueltos() {
        return puntosDevueltos;
    }

    public LocalDateTime getFechaReembolso() {
        return fechaReembolso;
    }
}
