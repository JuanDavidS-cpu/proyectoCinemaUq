package co.uniquindio.proyectocinemauq.mvc.modelo;

import java.time.LocalDateTime;

/** Lote de puntos: cuántos quedan, cuándo se ganaron y cuándo vencen. */
public class LotePuntos {

    private int monto;
    private final int montoInicial;
    private final LocalDateTime fechaObtencion;
    private final LocalDateTime fechaVencimiento;
    private final String descripcion;

    public LotePuntos(int monto, LocalDateTime fechaObtencion, LocalDateTime fechaVencimiento, String descripcion) {
        this.monto = monto;
        this.montoInicial = monto;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaVencimiento;
        this.descripcion = descripcion;
    }

    public void restar(int cantidad) {
        this.monto -= cantidad;
    }

    public boolean estaVencido(LocalDateTime ahora) {
        return !fechaVencimiento.isAfter(ahora);
    }

    public int getMonto() {
        return monto;
    }

    public int getMontoInicial() {
        return montoInicial;
    }

    public LocalDateTime getFechaObtencion() {
        return fechaObtencion;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
