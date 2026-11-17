package co.uniquindio.proyectocinemauq.estructural.facade;

import co.uniquindio.proyectocinemauq.comportamiento.chain.CadenaValidacionCompra;
import co.uniquindio.proyectocinemauq.comportamiento.strategy.EstrategiaPago;
import co.uniquindio.proyectocinemauq.comportamiento.strategy.PagoConPuntos;
import co.uniquindio.proyectocinemauq.comportamiento.strategy.PagoConTarjeta;
import co.uniquindio.proyectocinemauq.comportamiento.strategy.ProcesadorPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasPuntos;

/**
 * Orquesta una compra que ya armó el Builder: cadena, pago, sillas, puntos.
 * No construye combos ni decide si hay gafas.
 */
public class CompraFacade {

    private final CadenaValidacionCompra cadena;

    public CompraFacade() {
        this.cadena = new CadenaValidacionCompra();
    }

    public Compra procesarCompra(Compra compra) {
        cadena.validar(compra);
        ProcesadorPago procesador = new ProcesadorPago();
        EstrategiaPago estrategia;
        if (compra.getMedioPago() == MedioPago.TARJETA) {
            estrategia = new PagoConTarjeta(compra.getCliente().tarjetaParaPago());
        } else {
            estrategia = new PagoConPuntos(compra.getCliente().getCuentaPuntos());
        }
        procesador.setEstrategia(estrategia);
        procesador.pagar(compra);

        for (Asiento asiento : compra.getAsientos()) {
            asiento.ocupar();
        }

        int puntosUsados = 0;
        int puntosAcumulados = 0;
        if (compra.getMedioPago() == MedioPago.TARJETA) {
            puntosAcumulados = ReglasPuntos.porPagoConTarjeta(compra.getTotal());
            if (puntosAcumulados > 0) {
                compra.getCliente().getCuentaPuntos()
                        .acumular(puntosAcumulados, "Compra " + compra.getId());
            }
        } else {
            puntosUsados = compra.getPuntosRequeridos();
        }
        compra.marcarPagada(puntosUsados, puntosAcumulados);
        compra.getFuncion().suscribir(compra.getCliente());
        compra.getCliente().agregarCompra(compra);
        return compra;
    }
}
