package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.excepciones.PuntosInsuficientesException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Los puntos no viven en la tarjeta: se ganan, vencen y se gastan aparte del saldo.
 * Al redimir se usa primero el lote que vence antes. Los vencidos se rechazan.
 */
public class CuentaPuntos {

    private final List<LotePuntos> lotes;
    private final List<MovimientoPuntos> historial;

    public CuentaPuntos() {
        this.lotes = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    public void acumular(int cantidad, String motivo) {
        if (cantidad <= 0) {
            return;
        }
        LocalDateTime ahora = LocalDateTime.now();
        lotes.add(new LotePuntos(cantidad, ahora, ahora.plusYears(1), motivo));
        historial.add(new MovimientoPuntos(ahora, TipoMovimientoPuntos.ACUMULADO, cantidad, motivo));
    }

    public void cargarLote(int cantidad, LocalDateTime obtencion, LocalDateTime vencimiento, String motivo) {
        if (cantidad <= 0) {
            return;
        }
        lotes.add(new LotePuntos(cantidad, obtencion, vencimiento, motivo));
        historial.add(new MovimientoPuntos(obtencion, TipoMovimientoPuntos.ACUMULADO, cantidad, motivo));
    }

    public int puntosDisponibles(LocalDateTime ahora) {
        int total = 0;
        for (LotePuntos lote : lotes) {
            if (!lote.estaVencido(ahora)) {
                total += lote.getMonto();
            }
        }
        return total;
    }

    public int puntosVencidos(LocalDateTime ahora) {
        int total = 0;
        for (LotePuntos lote : lotes) {
            if (lote.estaVencido(ahora)) {
                total += lote.getMonto();
            }
        }
        return total;
    }

    public void redimir(int cantidad, LocalDateTime ahora, String motivo) {
        if (cantidad <= 0) {
            throw new PuntosInsuficientesException("Esta compra no tiene un precio en puntos.");
        }
        int vigentes = puntosDisponibles(ahora);
        if (vigentes < cantidad) {
            if (vigentes == 0 && puntosVencidos(ahora) > 0) {
                throw new PuntosInsuficientesException(
                        "Tus puntos están vencidos y no se pueden usar en esta compra.");
            }
            throw new PuntosInsuficientesException(
                    "No te alcanzan los puntos vigentes. Necesitas " + cantidad + " y tienes " + vigentes + ".");
        }
        int falta = cantidad;
        List<LotePuntos> orden = lotesOrdenados();
        for (LotePuntos lote : orden) {
            if (lote.estaVencido(ahora) || lote.getMonto() == 0) {
                continue;
            }
            int toma = Math.min(lote.getMonto(), falta);
            lote.restar(toma);
            falta -= toma;
            if (falta == 0) {
                break;
            }
        }
        for (int i = lotes.size() - 1; i >= 0; i--) {
            if (lotes.get(i).getMonto() <= 0) {
                lotes.remove(i);
            }
        }
        historial.add(new MovimientoPuntos(ahora, TipoMovimientoPuntos.REDIMIDO, cantidad, motivo));
    }

    public LotePuntos proximoAVencer(LocalDateTime ahora) {
        LotePuntos elegido = null;
        for (LotePuntos lote : lotes) {
            if (lote.estaVencido(ahora) || lote.getMonto() <= 0) {
                continue;
            }
            if (elegido == null || lote.getFechaVencimiento().isBefore(elegido.getFechaVencimiento())) {
                elegido = lote;
            }
        }
        return elegido;
    }

    public List<LotePuntos> getLotes() {
        return new ArrayList<>(lotes);
    }

    public List<LotePuntos> lotesOrdenados() {
        List<LotePuntos> orden = new ArrayList<>(lotes);
        Collections.sort(orden, new Comparator<LotePuntos>() {
            @Override
            public int compare(LotePuntos primero, LotePuntos segundo) {
                return primero.getFechaVencimiento().compareTo(segundo.getFechaVencimiento());
            }
        });
        return orden;
    }

    public List<MovimientoPuntos> getHistorial() {
        return List.copyOf(historial);
    }
}
