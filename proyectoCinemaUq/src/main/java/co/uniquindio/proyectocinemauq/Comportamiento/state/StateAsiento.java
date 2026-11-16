
package co.uniquindio.proyectocinemauq.Comportamiento.state;

public class StateAsiento {

    // Interfaz State
    public interface EstadoAsiento {
        boolean reservar(AsientoContexto asiento);
        boolean confirmar(AsientoContexto asiento);
        boolean liberar(AsientoContexto asiento);
        String getNombre();
    }

    // Contexto
    public static class AsientoContexto {
        private EstadoAsiento estado;

        public AsientoContexto() {
            this.estado = new Disponible();
        }

        public void setEstado(EstadoAsiento estado) {
            if (estado != null) {
                this.estado = estado;
            }
        }

        public boolean reservar() {
            return estado.reservar(this);
        }

        public boolean confirmar() {
            return estado.confirmar(this);
        }

        public boolean liberar() {
            return estado.liberar(this);
        }

        public String getEstado() {
            return estado.getNombre();
        }
    }

    // Estado disponible
    public static class Disponible implements EstadoAsiento {

        @Override
        public boolean reservar(AsientoContexto asiento) {
            asiento.setEstado(new Reservado());
            System.out.println("Asiento reservado.");
            return true;
        }

        @Override
        public boolean confirmar(AsientoContexto asiento) {
            System.out.println("Primero debes reservar.");
            return false;
        }

        @Override
        public boolean liberar(AsientoContexto asiento) {
            System.out.println("El asiento ya esta disponible.");
            return false;
        }

        @Override
        public String getNombre() {
            return "Disponible";
        }
    }

    // Estado reservado
    public static class Reservado implements EstadoAsiento {

        @Override
        public boolean reservar(AsientoContexto asiento) {
            System.out.println("El asiento ya esta reservado.");
            return false;
        }

        @Override
        public boolean confirmar(AsientoContexto asiento) {
            asiento.setEstado(new Ocupado());
            System.out.println("Reserva confirmada.");
            return true;
        }

        @Override
        public boolean liberar(AsientoContexto asiento) {
            asiento.setEstado(new Disponible());
            System.out.println("Reserva cancelada.");
            return true;
        }

        @Override
        public String getNombre() {
            return "Reservado";
        }
    }

    // Estado ocupado
    public static class Ocupado implements EstadoAsiento {

        @Override
        public boolean reservar(AsientoContexto asiento) {
            System.out.println("El asiento esta ocupado.");
            return false;
        }

        @Override
        public boolean confirmar(AsientoContexto asiento) {
            System.out.println("El asiento ya esta ocupado.");
            return false;
        }

        @Override
        public boolean liberar(AsientoContexto asiento) {
            System.out.println("No se puede liberar un asiento ocupado.");
            return false;
        }

        @Override
        public String getNombre() {
            return "Ocupado";
        }
    }
}
