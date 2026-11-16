
package co.uniquindio.proyectocinemauq.Comportamiento.observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverFuncion {

    // Interfaz Observer
    public interface Observador {
        void actualizar(String mensaje);
    }

    // Observador concreto
    public static class ClienteObservador implements Observador {
        private final String nombre;

        public ClienteObservador(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public void actualizar(String mensaje) {
            System.out.println("Notificacion para "
                    + nombre + ": " + mensaje);
        }
    }

    // Sujeto observable
    public static class FuncionObservable {
        private final String pelicula;
        private String estado;

        private final List<Observador> observadores =
                new ArrayList<>();

        public FuncionObservable(String pelicula) {
            this.pelicula = pelicula;
            this.estado = "Programada";
        }

        public void agregarObservador(Observador observador) {
            if (observador != null
                    && !observadores.contains(observador)) {
                observadores.add(observador);
            }
        }

        public void eliminarObservador(Observador observador) {
            observadores.remove(observador);
        }

        public void cambiarEstado(String nuevoEstado) {
            if (nuevoEstado == null || nuevoEstado.isBlank()) {
                return;
            }

            if (nuevoEstado.equals(estado)) {
                return;
            }

            this.estado = nuevoEstado;
            notificarObservadores();
        }

        private void notificarObservadores() {
            for (Observador observador : observadores) {
                observador.actualizar(
                        "La funcion de " + pelicula
                                + " ahora esta: " + estado);
            }
        }

        public String getEstado() {
            return estado;
        }
    }
}
