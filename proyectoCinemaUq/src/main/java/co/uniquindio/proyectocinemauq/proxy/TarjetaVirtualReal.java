package co.uniquindio.proyectocinemauq.proxy;

/**
 * Sujeto Real (RealSubject) del patron Proxy.
 * Contiene el saldo real y ejecuta el descuento directo del dinero.
 */
public class TarjetaVirtualReal implements MetodoPago {
    private double saldo;
    private boolean activa;

    public TarjetaVirtualReal(double saldoInicial) {
        this.saldo = saldoInicial;
        this.activa = true;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean estaActiva() {
        return activa;
    }

    @Override
    public boolean realizarPago(double monto) {
        saldo = saldo - monto;
        System.out.println("Debito realizado. Nuevo saldo en tarjeta: $" + saldo);
        return true;
    }
}