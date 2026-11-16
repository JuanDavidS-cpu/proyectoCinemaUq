package co.uniquindio.proyectocinemauq.model;

/**
 * Representa un asiento dentro de una sala de cine.
 */
public class Asiento {
    private String fila;
    private int columna;
    private String estado; // "Disponible" u "Ocupado"

    public Asiento(String fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.estado = "Disponible";
    }

    /**
     * Verifica si el asiento se encuentra libre usando una variable centinela.
     * @return true si esta disponible, false si ya esta ocupado.
     */
    public boolean estaDisponible() {
        boolean centinela = false;
        if (estado.equals("Disponible")) {
            centinela = true;
        }
        return centinela;
    }

    /**
     * Cambia el estado del asiento a ocupado una vez confirmada la compra.
     */
    public void ocupar() {
        this.estado = "Ocupado";
    }

    public String getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getEstado() {
        return estado;
    }
}