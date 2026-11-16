package co.uniquindio.proyectocinemauq.proxy;

/**
 * Clase Intermediaria (Proxy).
 * Controla el acceso a la tarjeta real verificando que este activa y tenga saldo suficiente.
 */
public class ProxyTarjetaVirtual implements MetodoPago {
    private TarjetaVirtualReal tarjetaReal;

    public ProxyTarjetaVirtual(TarjetaVirtualReal tarjetaReal) {
        this.tarjetaReal = tarjetaReal;
    }

    @Override
    public boolean realizarPago(double monto) {
        boolean pagoAprobado = false;

        if (!tarjetaReal.estaActiva()) {
            System.out.println("Acceso denegado (Proxy): La tarjeta virtual esta inactiva.");
        } else if (tarjetaReal.getSaldo() < monto) {
            System.out.println("Acceso denegado (Proxy): Saldo insuficiente para la compra.");
        } else {
            pagoAprobado = tarjetaReal.realizarPago(monto);
        }

        return pagoAprobado;
    }
}