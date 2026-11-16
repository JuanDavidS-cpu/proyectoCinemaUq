package co.uniquindio.proyectocinemauq.facade;


import co.uniquindio.proyectocinemauq.model.Asiento;
import co.uniquindio.proyectocinemauq.proxy.MetodoPago;

/**
 * Clase Fachada (Facade).
 * Simplifica el proceso de compra coordinando la validacion del asiento y el pago seguro.
 */
public class CompraFacade {
    private MetodoPago metodoPago;

    public CompraFacade(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Ejecuta todos los pasos necesarios para confirmar una compra en una sola llamada.
     */
    public boolean procesarCompra(double totalCompra, Asiento asientoSeleccionado) {
        boolean compraExitosa = false;
        String silla = asientoSeleccionado.getFila() + asientoSeleccionado.getColumna();

        if (asientoSeleccionado.estaDisponible()) {
            boolean pagoRealizado = metodoPago.realizarPago(totalCompra);

            if (pagoRealizado) {
                asientoSeleccionado.ocupar();
                System.out.println("Compra confirmada. Asiento " + silla + " reservado.");
                compraExitosa = true;
            }
        } else {
            System.out.println("Error: El asiento " + silla + " ya se encuentra ocupado.");
        }

        return compraExitosa;
    }
}
