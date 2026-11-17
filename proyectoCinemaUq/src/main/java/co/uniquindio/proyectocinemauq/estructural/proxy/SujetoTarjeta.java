package co.uniquindio.proyectocinemauq.estructural.proxy;

/**
 * Operación de cobro de la tarjeta.
 * La estrategia de tarjeta delega aquí; no guarda otro saldo.
 * La recarga no está en esta interfaz: solo la hace el administrador sobre la tarjeta real.
 */
public interface SujetoTarjeta {
    boolean realizarPago(double monto);
}
