package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.comportamiento.state.Disponible;
import co.uniquindio.proyectocinemauq.comportamiento.state.EstadoAsiento;
import co.uniquindio.proyectocinemauq.creacional.prototype.Prototipo;

/**
 * Silla de una función y contexto del State.
 * El clon copia fila y columna y crea un estado Disponible nuevo,
 * para que dos funciones no compartan la misma silla ni el mismo estado.
 */
public class Asiento implements Prototipo {

    private final String fila;
    private final int columna;
    private EstadoAsiento estado;

    public Asiento(String fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.estado = new Disponible();
    }

    public void cambiarEstado(EstadoAsiento nuevoEstado) {
        if (nuevoEstado == null) {
            return;
        }
        this.estado = nuevoEstado;
    }

    public void reservar() {
        estado.reservar(this);
    }

    public void ocupar() {
        estado.ocupar(this);
    }

    public void liberarReserva() {
        estado.liberarReserva(this);
    }

    public void liberarPorReembolso() {
        estado.liberarPorReembolso(this);
    }

    public boolean estaDisponible() {
        return "Disponible".equals(estado.getNombre());
    }

    public boolean estaReservado() {
        return "Reservado".equals(estado.getNombre());
    }

    public boolean estaOcupado() {
        return "Ocupado".equals(estado.getNombre());
    }

    public String getNombreEstado() {
        return estado.getNombre();
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public String getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getCodigo() {
        return fila + columna;
    }

    @Override
    public Asiento clonar() {
        return new Asiento(fila, columna);
    }
}
