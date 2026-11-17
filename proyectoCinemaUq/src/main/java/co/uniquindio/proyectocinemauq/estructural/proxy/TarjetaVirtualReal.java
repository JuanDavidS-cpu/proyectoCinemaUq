package co.uniquindio.proyectocinemauq.estructural.proxy;

import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.excepciones.SaldoInsuficienteException;
import co.uniquindio.proyectocinemauq.mvc.modelo.MovimientoTarjeta;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarjeta real del cliente: saldo, si está activa, recargas y movimientos.
 * Si se llama directo y no alcanza, lanza excepción y no deja el saldo negativo.
 * La revisión de "activa" la hace el proxy antes de llegar aquí.
 * La recarga la usa el administrador, a través de CinemaUQ.
 */
public class TarjetaVirtualReal implements SujetoTarjeta {

    private double saldo;
    private boolean activa;
    private final List<MovimientoTarjeta> movimientos;

    public TarjetaVirtualReal(double saldoInicial) {
        if (saldoInicial < 0) {
            throw new CompraInvalidaException("El saldo inicial no puede ser negativo.");
        }
        this.saldo = saldoInicial;
        this.activa = true;
        this.movimientos = new ArrayList<>();
        if (saldoInicial > 0) {
            movimientos.add(new MovimientoTarjeta(
                    LocalDateTime.now(),
                    saldoInicial,
                    "Saldo inicial",
                    TipoMovimiento.RECARGA));
        }
    }

    @Override
    public boolean realizarPago(double monto) {
        if (monto <= 0 || !Double.isFinite(monto)) {
            throw new CompraInvalidaException("El cobro tiene que ser mayor que cero.");
        }
        if (saldo < monto) {
            throw new SaldoInsuficienteException(
                    "La tarjeta no puede quedar en saldo negativo. Tiene "
                            + Textos.pesos(saldo) + " y el cobro es " + Textos.pesos(monto) + ".");
        }
        saldo -= monto;
        movimientos.add(new MovimientoTarjeta(
                LocalDateTime.now(), monto, "Compra en CinemaUQ", TipoMovimiento.COMPRA));
        return true;
    }

    public void recargar(double monto) {
        if (monto <= 0 || !Double.isFinite(monto)) {
            throw new CompraInvalidaException("La recarga tiene que ser mayor que cero.");
        }
        saldo += monto;
        movimientos.add(new MovimientoTarjeta(
                LocalDateTime.now(), monto, "Recarga en taquilla", TipoMovimiento.RECARGA));
    }

    public void abonar(double monto, String descripcion) {
        if (monto <= 0 || !Double.isFinite(monto)) {
            return;
        }
        saldo += monto;
        movimientos.add(new MovimientoTarjeta(
                LocalDateTime.now(), monto, descripcion, TipoMovimiento.REEMBOLSO));
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean estaActiva() {
        return activa;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    public List<MovimientoTarjeta> getMovimientos() {
        return List.copyOf(movimientos);
    }
}
