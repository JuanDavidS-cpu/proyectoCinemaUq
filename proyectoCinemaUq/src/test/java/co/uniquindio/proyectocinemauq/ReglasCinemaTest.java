package co.uniquindio.proyectocinemauq;

import co.uniquindio.proyectocinemauq.creacional.builder.CompraBuilder;
import co.uniquindio.proyectocinemauq.creacional.factory.CreadorEntradaGeneral;
import co.uniquindio.proyectocinemauq.creacional.singleton.CinemaUQ;
import co.uniquindio.proyectocinemauq.estructural.proxy.ProxyTarjetaVirtual;
import co.uniquindio.proyectocinemauq.estructural.proxy.TarjetaVirtualReal;
import co.uniquindio.proyectocinemauq.excepciones.AsientoOcupadoException;
import co.uniquindio.proyectocinemauq.excepciones.CancelacionFueraDeTiempoException;
import co.uniquindio.proyectocinemauq.excepciones.PuntosInsuficientesException;
import co.uniquindio.proyectocinemauq.excepciones.ReembolsoDuplicadoException;
import co.uniquindio.proyectocinemauq.excepciones.SaldoInsuficienteException;
import co.uniquindio.proyectocinemauq.excepciones.TarjetaInactivaException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.CuentaPuntos;
import co.uniquindio.proyectocinemauq.mvc.modelo.EstadoCompra;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.Pelicula;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasCancelacion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Rol;
import co.uniquindio.proyectocinemauq.mvc.modelo.Sala;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReglasCinemaTest {

    @Test
    void reembolsoRespetaLasHorasYNoSeAplicaDosVeces() {
        LocalDateTime funcion = LocalDateTime.of(2026, 11, 20, 18, 0);
        assertEquals(1.0, ReglasCancelacion.porcentaje(funcion.minusHours(24).minusMinutes(1), funcion));
        assertEquals(0.8, ReglasCancelacion.porcentaje(funcion.minusHours(24), funcion));
        assertEquals(0.8, ReglasCancelacion.porcentaje(funcion.minusHours(2), funcion));
        assertThrows(CancelacionFueraDeTiempoException.class,
                () -> ReglasCancelacion.porcentaje(funcion.minusMinutes(119), funcion));
        assertThrows(CancelacionFueraDeTiempoException.class,
                () -> ReglasCancelacion.porcentaje(funcion.plusHours(1), funcion));

        Compra compra = compraDePrueba(0);
        compra.getAsientos().get(0).ocupar();
        compra.marcarPagada(0, 0);
        compra.reembolsar(1.0, EstadoCompra.REEMBOLSADA, "prueba");
        assertEquals(12000, compra.getCliente().getTarjeta().getSaldo(), 0.01);
        assertTrue(compra.getAsientos().get(0).estaDisponible());
        assertThrows(ReembolsoDuplicadoException.class,
                () -> compra.reembolsar(1.0, EstadoCompra.REEMBOLSADA, "otra vez"));
        assertEquals(12000, compra.getCliente().getTarjeta().getSaldo(), 0.01);

        Compra parcial = compraDePrueba(0);
        parcial.getAsientos().get(0).ocupar();
        parcial.marcarPagada(0, 0);
        parcial.reembolsar(0.8, EstadoCompra.CANCELADA, "80");
        assertEquals(9600, parcial.getCliente().getTarjeta().getSaldo(), 0.01);
    }

    @Test
    void puntosGastanPrimeroElLoteQueVenceAntes() {
        LocalDateTime ahora = LocalDateTime.of(2026, 11, 17, 12, 0);
        CuentaPuntos cuenta = new CuentaPuntos();
        cuenta.cargarLote(100, ahora.minusDays(400), ahora.minusDays(35), "viejos");
        cuenta.cargarLote(30, ahora.minusDays(20), ahora.plusDays(10), "pronto");
        cuenta.cargarLote(50, ahora.minusDays(10), ahora.plusDays(200), "lejos");
        assertEquals(80, cuenta.puntosDisponibles(ahora));
        cuenta.redimir(40, ahora, "prueba");
        assertEquals(40, cuenta.puntosDisponibles(ahora));
        assertEquals(100, cuenta.puntosVencidos(ahora));
        assertThrows(PuntosInsuficientesException.class, () -> cuenta.redimir(100, ahora, "no"));
        assertEquals(40, cuenta.puntosDisponibles(ahora));
    }

    @Test
    void elClonNoComparteElEstadoDelAsiento() {
        Asiento original = new Asiento("B", 2);
        Asiento copia = original.clonar();
        assertNotSame(original.getEstado(), copia.getEstado());
        original.reservar();
        assertTrue(original.estaReservado());
        assertTrue(copia.estaDisponible());
        copia.ocupar();
        assertTrue(original.estaReservado());
        assertThrows(AsientoOcupadoException.class, copia::liberarReserva);
        copia.liberarPorReembolso();
        assertTrue(copia.estaDisponible());
    }

    @Test
    void elProxyRechazaSaldoInsuficienteYLaTarjetaNoQuedaNegativa() {
        TarjetaVirtualReal real = new TarjetaVirtualReal(1000);
        ProxyTarjetaVirtual proxy = new ProxyTarjetaVirtual(real);
        assertThrows(SaldoInsuficienteException.class, () -> proxy.realizarPago(5000));
        assertEquals(1000, real.getSaldo(), 0.01);
        assertThrows(SaldoInsuficienteException.class, () -> real.realizarPago(5000));
        assertEquals(1000, real.getSaldo(), 0.01);
        real.desactivar();
        assertThrows(TarjetaInactivaException.class, () -> proxy.realizarPago(100));
        assertEquals(1000, real.getSaldo(), 0.01);
    }

    @Test
    void hayUnaSolaInstanciaConLosUsuariosSemilla() {
        assertSame(CinemaUQ.getInstancia(), CinemaUQ.getInstancia());
        Cliente ana = CinemaUQ.getInstancia().iniciarSesion("ana", "ana123");
        Cliente admin = CinemaUQ.getInstancia().iniciarSesion("admin", "admin123");
        assertTrue(admin.esAdmin());
        assertTrue(ana.getTarjeta().getSaldo() < 200000);
        assertTrue(ana.getCuentaPuntos().puntosDisponibles(LocalDateTime.now()) >= 12);
    }

    private Compra compraDePrueba(double saldo) {
        Sala sala = new Sala("ST", "Sala test", 1, 1);
        Pelicula pelicula = new Pelicula("PT", "Prueba", 90, "Drama");
        Funcion funcion = new Funcion("FT", pelicula, sala, LocalDateTime.now().plusDays(3));
        Asiento asiento = funcion.getAsientos().get(0);
        Cliente cliente = new Cliente("Prueba", "prueba-" + System.nanoTime(), "1234", Rol.CLIENTE, saldo);
        return new CompraBuilder()
                .id("T-" + System.nanoTime())
                .cliente(cliente)
                .funcion(funcion)
                .asientos(List.of(asiento))
                .items(List.of(new CreadorEntradaGeneral().crearEntrada()))
                .descuento(0, "")
                .medioPago(MedioPago.TARJETA)
                .build();
    }
}
