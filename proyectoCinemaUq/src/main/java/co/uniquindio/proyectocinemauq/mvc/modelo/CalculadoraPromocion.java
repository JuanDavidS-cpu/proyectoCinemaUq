package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Aplica las promociones una sola vez sobre el precio ya calculado del ítem.
 * No abre el combo para volver a descontar sus productos.
 */
public class CalculadoraPromocion {

    public ResultadoPromocion calcular(List<Comprable> items, List<Promocion> promociones, LocalDate hoy) {
        double descuentoDosPorUno = 0;
        List<String> nombres = new ArrayList<>();
        Map<String, List<Comprable>> sueltos = new LinkedHashMap<>();
        for (Comprable item : items) {
            if (item.esEntrada() || item.esCombo()) {
                continue;
            }
            List<Comprable> grupo = sueltos.get(item.getNombre());
            if (grupo == null) {
                grupo = new ArrayList<>();
                sueltos.put(item.getNombre(), grupo);
            }
            grupo.add(item);
        }
        for (Promocion promocion : promociones) {
            if (promocion.getTipo() != TipoPromocion.DOS_POR_UNO) {
                continue;
            }
            List<Comprable> grupo = sueltos.get(promocion.getNombreProducto());
            if (grupo == null || grupo.size() < 2) {
                continue;
            }
            int gratis = grupo.size() / 2;
            descuentoDosPorUno += Math.round(gratis * grupo.get(0).getPrecio());
            nombres.add(promocion.getNombre());
        }

        double baseConfiteria = 0;
        for (Comprable item : items) {
            if (!item.esEntrada()) {
                baseConfiteria += item.getPrecio();
            }
        }
        baseConfiteria = Math.max(0, Math.round(baseConfiteria) - descuentoDosPorUno);

        double descuentoPorcentaje = 0;
        for (Promocion promocion : promociones) {
            if (promocion.getTipo() != TipoPromocion.PORCENTAJE_DIA) {
                continue;
            }
            if (promocion.getDia() != hoy.getDayOfWeek() || baseConfiteria <= 0) {
                continue;
            }
            descuentoPorcentaje += Math.round(baseConfiteria * promocion.getPorcentaje());
            nombres.add(promocion.getNombre());
        }

        double total = descuentoDosPorUno + descuentoPorcentaje;
        String descripcion = String.join(", ", nombres);
        return new ResultadoPromocion(total, descripcion);
    }
}
