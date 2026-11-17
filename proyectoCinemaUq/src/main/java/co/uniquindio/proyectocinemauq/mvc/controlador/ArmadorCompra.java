package co.uniquindio.proyectocinemauq.mvc.controlador;

import co.uniquindio.proyectocinemauq.creacional.builder.CompraBuilder;
import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.mvc.modelo.CalculadoraPromocion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.estructural.composite.Comprable;
import co.uniquindio.proyectocinemauq.mvc.modelo.ResultadoPromocion;

import java.time.LocalDate;
import java.util.List;

/**
 * Junta la fábrica, el decorador, la promoción y el builder.
 * La fachada cobra después, cuando el cliente confirma.
 */
public final class ArmadorCompra {

    private ArmadorCompra() {
    }

    public static Compra armar(Cliente cliente, CompraEnCurso curso, MedioPago medio, String id) {
        List<Comprable> items = curso.armarItems();
        ResultadoPromocion promocion = new CalculadoraPromocion()
                .calcular(items, CinemaUQ.getInstancia().getPromociones(), LocalDate.now());
        return new CompraBuilder()
                .id(id)
                .cliente(cliente)
                .funcion(curso.getFuncion())
                .asientos(curso.getAsientos())
                .items(items)
                .descuento(promocion.getDescuento(), promocion.getDescripcion())
                .medioPago(medio)
                .build();
    }
}
