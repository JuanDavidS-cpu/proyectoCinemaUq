
package co.uniquindio.proyectocinemauq.Comportamiento.chain;

public class ChainCompra {

    // Solicitud de compra
    public static class SolicitudCompra {
        public boolean clienteActivo;
        public int asientosDisponibles;
        public int cantidadEntradas;
        public double total;

        public SolicitudCompra(boolean clienteActivo,
                               int asientosDisponibles,
                               int cantidadEntradas,
                               double total) {
            this.clienteActivo = clienteActivo;
            this.asientosDisponibles = asientosDisponibles;
            this.cantidadEntradas = cantidadEntradas;
            this.total = total;
        }
    }

    // Manejador abstracto
    public static abstract class Validador {
        protected Validador siguiente;

        public Validador establecerSiguiente(Validador siguiente) {
            this.siguiente = siguiente;
            return siguiente;
        }

        public final boolean validar(SolicitudCompra solicitud) {
            if (solicitud == null) {
                System.out.println("Solicitud invalida.");
                return false;
            }

            if (!comprobar(solicitud)) {
                return false;
            }

            if (siguiente != null) {
                return siguiente.validar(solicitud);
            }

            return true;
        }

        protected abstract boolean comprobar(
                SolicitudCompra solicitud);
    }

    // Validar cliente
    public static class ValidarCliente extends Validador {

        @Override
        protected boolean comprobar(SolicitudCompra solicitud) {
            if (!solicitud.clienteActivo) {
                System.out.println("Cliente inactivo.");
                return false;
            }

            System.out.println("Cliente validado.");
            return true;
        }
    }

    // Validar asientos
    public static class ValidarAsientos extends Validador {

        @Override
        protected boolean comprobar(SolicitudCompra solicitud) {
            if (solicitud.cantidadEntradas <= 0
                    || solicitud.asientosDisponibles
                    < solicitud.cantidadEntradas) {
                System.out.println("Asientos insuficientes.");
                return false;
            }

            System.out.println("Asientos validados.");
            return true;
        }
    }

    // Validar monto
    public static class ValidarMonto extends Validador {

        @Override
        protected boolean comprobar(SolicitudCompra solicitud) {
            if (!Double.isFinite(solicitud.total)
                    || solicitud.total <= 0) {
                System.out.println("Monto invalido.");
                return false;
            }

            System.out.println("Monto validado.");
            return true;
        }
    }
}
