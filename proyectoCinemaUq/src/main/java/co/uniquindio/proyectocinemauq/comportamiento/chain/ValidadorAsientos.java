package co.uniquindio.proyectocinemauq.comportamiento.chain;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.excepciones.AsientoOcupadoException;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Revisa las sillas de ESA función: libres o apartadas, y que no vayan repetidas. */
public class ValidadorAsientos extends ValidadorCompra {

    @Override
    protected void comprobar(Compra compra) {
        if (compra.getFuncion() == null) {
            throw new CompraInvalidaException("Elige una función antes de pagar.");
        }
        if (compra.getFuncion().estaCancelada()) {
            throw new CompraInvalidaException("Esta función fue cancelada. Elige otra.");
        }
        List<Asiento> asientos = compra.getAsientos();
        if (asientos.isEmpty()) {
            throw new CompraInvalidaException("Elige al menos un asiento.");
        }
        Set<Asiento> vistos = new HashSet<>();
        List<Asiento> deLaFuncion = compra.getFuncion().getAsientos();
        for (Asiento asiento : asientos) {
            if (!deLaFuncion.contains(asiento)) {
                throw new CompraInvalidaException("El asiento no pertenece a esta función.");
            }
            if (!vistos.add(asiento)) {
                throw new CompraInvalidaException(
                        "Elegiste el asiento " + asiento.getCodigo() + " más de una vez.");
            }
            if (asiento.estaOcupado()) {
                throw new AsientoOcupadoException(
                        "El asiento " + asiento.getCodigo() + " ya está vendido en esta función.");
            }
            if (!asiento.estaDisponible() && !asiento.estaReservado()) {
                throw new CompraInvalidaException(
                        "El asiento " + asiento.getCodigo() + " no se puede vender ahora.");
            }
        }
        int entradas = 0;
        for (Comprable item : compra.getItems()) {
            if (item.esEntrada()) {
                entradas++;
            }
        }
        if (entradas != asientos.size()) {
            throw new CompraInvalidaException("Cada entrada necesita su asiento. Revisa la selección.");
        }
    }
}
