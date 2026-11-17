package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.comportamiento.observer.Observador;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Función real de cartelera: película, sala, horario y sillas clonadas.
 * Los compradores quedan suscritos. Si el admin la cancela, se les avisa una vez.
 */
public class Funcion {

    private final String id;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDateTime fechaHora;
    private final List<Asiento> asientos;
    private final List<Observador> observadores;
    private boolean cancelada;

    public Funcion(String id, Pelicula pelicula, Sala sala, LocalDateTime fechaHora) {
        if (pelicula == null || sala == null || fechaHora == null) {
            throw new CompraInvalidaException("Faltan la película, la sala o la fecha de la función.");
        }
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHora = fechaHora;
        this.asientos = sala.clonarPlantilla();
        this.observadores = new ArrayList<>();
    }

    public void suscribir(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void notificar(String mensaje) {
        List<Observador> copia = new ArrayList<>(observadores);
        for (Observador observador : copia) {
            observador.actualizar(mensaje);
        }
    }

    public void marcarCancelada() {
        this.cancelada = true;
    }

    public boolean estaCancelada() {
        return cancelada;
    }

    public String getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public List<Asiento> getAsientos() {
        return List.copyOf(asientos);
    }

    public int contarLibres() {
        int libres = 0;
        for (Asiento asiento : asientos) {
            if (asiento.estaDisponible()) {
                libres++;
            }
        }
        return libres;
    }

    public int contarOcupados() {
        int ocupados = 0;
        for (Asiento asiento : asientos) {
            if (asiento.estaOcupado()) {
                ocupados++;
            }
        }
        return ocupados;
    }

    public String textoCartelera() {
        return pelicula.getTitulo() + " (" + pelicula.getDuracion() + " min) · " + pelicula.getGenero()
                + "\n" + sala.getNombre() + " · " + Textos.fechaHora(fechaHora)
                + " · " + contarLibres() + " asientos libres";
    }

    public String textoAdmin() {
        String estado = cancelada ? "Cancelada" : "Programada";
        return pelicula.getTitulo() + " · " + sala.getNombre() + " · " + Textos.fechaHora(fechaHora)
                + " · " + contarOcupados() + "/" + asientos.size() + " ocupados · " + estado;
    }
}
