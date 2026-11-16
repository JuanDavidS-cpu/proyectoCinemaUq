
package co.uniquindio.proyectocinemauq.Comportamiento.command;

import co.uniquindio.proyectocinemauq.Comportamiento.state.StateAsiento.AsientoContexto;

public class CommandReserva {

    // Interfaz Command
    public interface Comando {
        boolean ejecutar();
    }

    // Comando para reservar
    public static class ReservarAsiento implements Comando {
        private final AsientoContexto asiento;

        public ReservarAsiento(AsientoContexto asiento) {
            this.asiento = asiento;
        }

        @Override
        public boolean ejecutar() {
            System.out.println("Ejecutando reserva...");
            return asiento.reservar();
        }
    }

    // Comando para cancelar
    public static class CancelarReserva implements Comando {
        private final AsientoContexto asiento;

        public CancelarReserva(AsientoContexto asiento) {
            this.asiento = asiento;
        }

        @Override
        public boolean ejecutar() {
            System.out.println("Ejecutando cancelacion...");
            return asiento.liberar();
        }
    }

    // Invocador de comandos
    public static class GestorReservas {

        public boolean ejecutarComando(Comando comando) {
            if (comando == null) {
                System.out.println("Comando invalido.");
                return false;
            }

            return comando.ejecutar();
        }
    }
}

