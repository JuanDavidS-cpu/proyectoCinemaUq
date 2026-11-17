package co.uniquindio.proyectocinemauq.estructural.composite;

/**
 * Entrada, producto y combo se cobran igual.
 * La interfaz se queda corta a propósito: nombre, precio y si es entrada o combo.
 */
public interface Comprable {
    String getNombre();

    double getPrecio();

    int getPuntosRedencion();

    boolean esEntrada();

    boolean esCombo();
}
