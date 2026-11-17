package co.uniquindio.proyectocinemauq.estructural.proxy;

import co.uniquindio.proyectocinemauq.excepciones.SaldoInsuficienteException;
import co.uniquindio.proyectocinemauq.excepciones.TarjetaInactivaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;

/**
 * Impide cobrar una tarjeta inactiva o sin saldo.
 * La tarjeta real no hace esa revisión: solo descuenta si el proxy ya dejó pasar.
 */
public class ProxyTarjetaVirtual implements SujetoTarjeta {

    private final TarjetaVirtualReal tarjetaReal;

    public ProxyTarjetaVirtual(TarjetaVirtualReal tarjetaReal) {
        this.tarjetaReal = tarjetaReal;
    }

    @Override
    public boolean realizarPago(double monto) {
        if (!tarjetaReal.estaActiva()) {
            throw new TarjetaInactivaException(
                    "Tu tarjeta está inactiva. Pide al administrador que la active en Clientes.");
        }
        if (tarjetaReal.getSaldo() < monto) {
            throw new SaldoInsuficienteException(
                    "No te alcanza el saldo. La compra vale " + Textos.pesos(monto)
                            + " y tu tarjeta tiene " + Textos.pesos(tarjetaReal.getSaldo()) + ".");
        }
        return tarjetaReal.realizarPago(monto);
    }
}
