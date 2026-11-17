package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.factory.CreadorEntrada;
import co.uniquindio.proyectocinemauq.creacional.factory.CreadorEntradaGeneral;
import co.uniquindio.proyectocinemauq.creacional.factory.CreadorEntradaPreferencial;
import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.estructural.decorator.Gafas3DDecorator;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Promocion;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoPromocion;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lo que el cliente va armando entre los cuatro pasos.
 * Al salir sin pagar, suelta las sillas que había apartado.
 */
public class CompraEnCurso {

    private Funcion funcion;
    private final List<co.uniquindio.proyectocinemauq.mvc.modelo.Asiento> asientos = new ArrayList<>();
    private boolean preferencial;
    private boolean gafas;
    private final Map<Comprable, Integer> cantidades = new LinkedHashMap<>();

    public void elegirFuncion(Funcion nueva) {
        if (funcion != nueva) {
            liberarReservas();
            funcion = nueva;
            preferencial = false;
            gafas = false;
            cantidades.clear();
        }
    }

    public void agregarAsiento(co.uniquindio.proyectocinemauq.mvc.modelo.Asiento asiento) {
        if (!asientos.contains(asiento)) {
            asientos.add(asiento);
        }
    }

    public void quitarAsiento(co.uniquindio.proyectocinemauq.mvc.modelo.Asiento asiento) {
        asientos.remove(asiento);
    }

    public void setCantidad(Comprable producto, int cantidad) {
        if (cantidad <= 0) {
            cantidades.remove(producto);
        } else {
            cantidades.put(producto, cantidad);
        }
    }

    public int getCantidad(Comprable producto) {
        return cantidades.getOrDefault(producto, 0);
    }

    public List<Comprable> armarItems() {
        List<Comprable> items = new ArrayList<>();
        CreadorEntrada creador = preferencial ? new CreadorEntradaPreferencial() : new CreadorEntradaGeneral();
        for (int i = 0; i < asientos.size(); i++) {
            Comprable entrada = creador.crearEntrada();
            if (gafas) {
                entrada = new Gafas3DDecorator(entrada);
            }
            items.add(entrada);
        }
        for (Map.Entry<Comprable, Integer> entrada : cantidades.entrySet()) {
            for (int i = 0; i < entrada.getValue(); i++) {
                items.add(entrada.getKey());
            }
        }
        return items;
    }

    public void abandonar() {
        liberarReservas();
        funcion = null;
        preferencial = false;
        gafas = false;
        cantidades.clear();
    }

    public void cerrarCompraExitosa() {
        asientos.clear();
        cantidades.clear();
        funcion = null;
        preferencial = false;
        gafas = false;
    }

    public String textoAsientos() {
        if (asientos.isEmpty()) {
            return "Aún no apartas ninguna silla.";
        }
        StringBuilder texto = new StringBuilder("Apartadas: ");
        for (int i = 0; i < asientos.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(asientos.get(i).getCodigo());
        }
        return texto.toString();
    }

    private void liberarReservas() {
        List<co.uniquindio.proyectocinemauq.mvc.modelo.Asiento> copia = new ArrayList<>(asientos);
        for (co.uniquindio.proyectocinemauq.mvc.modelo.Asiento asiento : copia) {
            if (asiento.estaReservado()) {
                asiento.liberarReserva();
            }
        }
        asientos.clear();
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<co.uniquindio.proyectocinemauq.mvc.modelo.Asiento> getAsientos() {
        return List.copyOf(asientos);
    }

    public boolean isPreferencial() {
        return preferencial;
    }

    public void setPreferencial(boolean preferencial) {
        this.preferencial = preferencial;
    }

    public boolean isGafas() {
        return gafas;
    }

    public void setGafas(boolean gafas) {
        this.gafas = gafas;
    }

    public String textoPromocionHoy() {
        DayOfWeek hoy = LocalDate.now().getDayOfWeek();
        for (Promocion promocion : CinemaUQ.getInstancia().getPromociones()) {
            if (promocion.getTipo() == TipoPromocion.PORCENTAJE_DIA
                    && promocion.getDia() == hoy) {
                int porciento = (int) Math.round(promocion.getPorcentaje() * 100);
                return "Hoy, " + promocion.getNombre() + ": " + porciento
                        + "% en confitería. Las entradas no entran en ese descuento.";
            }
        }
        return "Hoy no hay un porcentaje de confitería. El combo ya trae su propio descuento.";
    }
}
