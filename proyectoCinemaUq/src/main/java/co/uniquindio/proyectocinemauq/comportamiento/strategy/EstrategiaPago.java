package co.uniquindio.proyectocinemauq.comportamiento.strategy;

import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

/**
 * La fachada cobra igual, sea con tarjeta o con puntos.
 * El pago con tarjeta no guarda saldo propio: delega en el proxy.
 */
public interface EstrategiaPago {
    void pagar(Compra compra);
}
