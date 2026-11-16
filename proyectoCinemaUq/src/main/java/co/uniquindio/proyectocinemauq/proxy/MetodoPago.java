package co.uniquindio.proyectocinemauq.proxy;

/**
 * Interfaz Sujeto (Subject) del patron Proxy.
 * Define la operacion comun entre la tarjeta real y su intermediario de seguridad.
 */
public interface MetodoPago {
    boolean realizarPago(double monto);
}
