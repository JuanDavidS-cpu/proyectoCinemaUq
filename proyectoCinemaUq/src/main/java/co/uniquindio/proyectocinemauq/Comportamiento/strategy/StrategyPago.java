
package co.uniquindio.proyectocinemauq.Comportamiento.strategy;

public class StrategyPago {

    // Interfaz de la estrategia
    public interface MetodoPago {
        boolean pagar(double monto);
    }

    // Estrategia: tarjeta virtual
    public static class PagoTarjeta implements MetodoPago {
        private double saldo;

        public PagoTarjeta(double saldo) {
            this.saldo = saldo;
        }

        @Override
        public boolean pagar(double monto) {
            if (!Double.isFinite(monto) || monto <= 0) {
                System.out.println("Monto invalido.");
                return false;
            }

            if (saldo < monto) {
                System.out.println("Saldo insuficiente.");
                return false;
            }

            saldo -= monto;
            System.out.println("Pago con tarjeta exitoso: $" + monto);
            return true;
        }

        public double getSaldo() {
            return saldo;
        }
    }

    // Estrategia: puntos
    public static class PagoPuntos implements MetodoPago {
        private int puntos;

        public PagoPuntos(int puntos) {
            this.puntos = puntos;
        }

        @Override
        public boolean pagar(double monto) {
            if (!Double.isFinite(monto) || monto <= 0) {
                System.out.println("Monto invalido.");
                return false;
            }

            // Regla de ejemplo: 1 punto equivale a 100 pesos
            double puntosRequeridos = Math.ceil(monto / 100.0);

            if (puntosRequeridos > puntos) {
                System.out.println("Puntos insuficientes.");
                return false;
            }

            int necesarios = (int) puntosRequeridos;
            puntos -= necesarios;

            System.out.println("Pago con puntos exitoso.");
            System.out.println("Puntos utilizados: " + necesarios);
            return true;
        }

        public int getPuntos() {
            return puntos;
        }
    }

    // Contexto del patron Strategy
    public static class ProcesadorPago {
        private MetodoPago metodoPago;

        public void setMetodoPago(MetodoPago metodoPago) {
            this.metodoPago = metodoPago;
        }

        public boolean realizarPago(double monto) {
            if (metodoPago == null) {
                System.out.println("No hay metodo de pago.");
                return false;
            }

            return metodoPago.pagar(monto);
        }
    }
}
