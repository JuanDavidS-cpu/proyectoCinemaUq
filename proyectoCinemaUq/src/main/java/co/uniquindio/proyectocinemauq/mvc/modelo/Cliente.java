package co.uniquindio.proyectocinemauq.mvc.modelo;

import co.uniquindio.proyectocinemauq.comportamiento.observer.Observador;
import co.uniquindio.proyectocinemauq.estructural.proxy.ProxyTarjetaVirtual;
import co.uniquindio.proyectocinemauq.estructural.proxy.SujetoTarjeta;
import co.uniquindio.proyectocinemauq.estructural.proxy.TarjetaVirtualReal;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;

import java.util.ArrayList;
import java.util.List;

/**
 * Persona que entra al cine. También observa las funciones que compró.
 */
public class Cliente implements Observador {

    private final String nombre;
    private final String identificacion;
    private final String clave;
    private final Rol rol;
    private boolean activo;
    private final TarjetaVirtualReal tarjeta;
    private final CuentaPuntos cuentaPuntos;
    private final List<Compra> compras;
    private final List<String> avisos;

    public Cliente(String nombre, String identificacion, String clave, Rol rol, double saldoInicial) {
        if (nombre == null || nombre.isBlank()) {
            throw new CompraInvalidaException("Escribe el nombre.");
        }
        if (identificacion == null || identificacion.isBlank()) {
            throw new CompraInvalidaException("Escribe la identificación.");
        }
        if (clave == null || clave.isBlank()) {
            throw new CompraInvalidaException("Escribe la clave.");
        }
        this.nombre = nombre.trim();
        this.identificacion = identificacion.trim();
        this.clave = clave;
        this.rol = rol;
        this.activo = true;
        this.tarjeta = new TarjetaVirtualReal(saldoInicial);
        this.cuentaPuntos = new CuentaPuntos();
        this.compras = new ArrayList<>();
        this.avisos = new ArrayList<>();
    }

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    public SujetoTarjeta tarjetaParaPago() {
        return new ProxyTarjetaVirtual(tarjeta);
    }

    @Override
    public void actualizar(String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            avisos.add(mensaje);
        }
    }

    public List<String> tomarAvisos() {
        List<String> copia = List.copyOf(avisos);
        avisos.clear();
        return copia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getClave() {
        return clave;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }

    public TarjetaVirtualReal getTarjeta() {
        return tarjeta;
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public List<Compra> getCompras() {
        return List.copyOf(compras);
    }

    public String textoAdmin() {
        String cuenta = activo ? "Cuenta activa" : "Cuenta inactiva";
        String tarjetaTexto = tarjeta.estaActiva() ? "Tarjeta activa" : "Tarjeta inactiva";
        return nombre + " · " + identificacion + " · " + Textos.pesos(tarjeta.getSaldo())
                + " · " + tarjetaTexto + " · " + cuenta;
    }
}
