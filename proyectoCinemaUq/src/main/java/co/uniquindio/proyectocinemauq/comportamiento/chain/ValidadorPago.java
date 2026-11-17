package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.excepciones.PuntosInsuficientesException;
import co.uniquindio.proyectocinemauq.excepciones.SaldoInsuficienteException;
import co.uniquindio.proyectocinemauq.excepciones.TarjetaInactivaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;

import java.time.LocalDateTime;

/** Tarjeta activa con saldo, o puntos vigentes suficientes. Todavía no cobra. */
public class ValidadorPago extends ValidadorCompra {

    @Override
    protected void comprobar(Compra compra) {
        if (compra.getMedioPago() == null) {
            throw new CompraInvalidaException("Elige si vas a pagar con tarjeta o con puntos.");
        }
        if (compra.getMedioPago() == MedioPago.TARJETA) {
            if (!compra.getCliente().getTarjeta().estaActiva()) {
                throw new TarjetaInactivaException(
                        "Tu tarjeta está inactiva. Pide al administrador que la active en Clientes.");
            }
            double saldo = compra.getCliente().getTarjeta().getSaldo();
            if (saldo < compra.getTotal()) {
                throw new SaldoInsuficienteException(
                        "No te alcanza el saldo. La compra vale " + Textos.pesos(compra.getTotal())
                                + " y tu tarjeta tiene " + Textos.pesos(saldo) + ".");
            }
            return;
        }
        int vigentes = compra.getCliente().getCuentaPuntos().puntosDisponibles(LocalDateTime.now());
        int necesarios = compra.getPuntosRequeridos();
        if (vigentes < necesarios) {
            int vencidos = compra.getCliente().getCuentaPuntos().puntosVencidos(LocalDateTime.now());
            if (vigentes == 0 && vencidos > 0) {
                throw new PuntosInsuficientesException(
                        "Tus puntos están vencidos y no se pueden usar en esta compra.");
            }
            throw new PuntosInsuficientesException(
                    "No te alcanzan los puntos vigentes. Esta compra vale " + necesarios
                            + " puntos y tienes " + vigentes + ".");
        }
    }
}
