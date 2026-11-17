package co.uniquindio.proyectocinemauq.creacional.singleton;

import co.uniquindio.proyectocinemauq.creacional.builder.CompraBuilder;
import co.uniquindio.proyectocinemauq.creacional.factory.CreadorEntradaGeneral;
import co.uniquindio.proyectocinemauq.estructural.composite.Combo;
import co.uniquindio.proyectocinemauq.estructural.composite.ProductoConfiteria;
import co.uniquindio.proyectocinemauq.estructural.facade.CompraFacade;
import co.uniquindio.proyectocinemauq.excepciones.CompraInvalidaException;
import co.uniquindio.proyectocinemauq.excepciones.OperacionSoloAdminException;
import co.uniquindio.proyectocinemauq.mvc.modelo.Asiento;
import co.uniquindio.proyectocinemauq.mvc.modelo.Cliente;
import co.uniquindio.proyectocinemauq.mvc.modelo.Compra;
import co.uniquindio.proyectocinemauq.mvc.modelo.EstadisticasCine;
import co.uniquindio.proyectocinemauq.mvc.modelo.EstadoCompra;
import co.uniquindio.proyectocinemauq.mvc.modelo.Funcion;
import co.uniquindio.proyectocinemauq.mvc.modelo.GeneradorReporte;
import co.uniquindio.proyectocinemauq.mvc.modelo.MedioPago;
import co.uniquindio.proyectocinemauq.mvc.modelo.Pelicula;
import co.uniquindio.proyectocinemauq.mvc.modelo.Promocion;
import co.uniquindio.proyectocinemauq.mvc.modelo.ReglasCancelacion;
import co.uniquindio.proyectocinemauq.mvc.modelo.Reporte;
import co.uniquindio.proyectocinemauq.mvc.modelo.Rol;
import co.uniquindio.proyectocinemauq.mvc.modelo.Sala;
import co.uniquindio.proyectocinemauq.mvc.modelo.Textos;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoPromocion;
import co.uniquindio.proyectocinemauq.mvc.modelo.TipoReporte;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Un solo CinemaUQ en memoria: cartelera, salas, clientes, compras, reglas y datos semilla.
 * El cine es un solo sistema; dos instancias duplicarían asientos, saldo y puntos.
 */
public final class CinemaUQ {

    private static CinemaUQ instancia;

    private final List<Pelicula> peliculas;
    private final List<Sala> salas;
    private final List<Funcion> funciones;
    private final List<Cliente> clientes;
    private final List<Compra> compras;
    private final List<ProductoConfiteria> productos;
    private final List<Combo> combos;
    private final List<Promocion> promociones;
    private final CompraFacade fachada;
    private int seqPelicula;
    private int seqSala;
    private int seqFuncion;
    private int seqProducto;
    private int seqCombo;
    private int seqPromo;
    private int seqCompra;

    private CinemaUQ() {
        peliculas = new ArrayList<>();
        salas = new ArrayList<>();
        funciones = new ArrayList<>();
        clientes = new ArrayList<>();
        compras = new ArrayList<>();
        productos = new ArrayList<>();
        combos = new ArrayList<>();
        promociones = new ArrayList<>();
        fachada = new CompraFacade();
        seqPelicula = 1;
        seqSala = 1;
        seqFuncion = 1;
        seqProducto = 1;
        seqCombo = 1;
        seqPromo = 1;
        seqCompra = 1001;
        cargarSemilla();
    }

    public static CinemaUQ getInstancia() {
        if (instancia == null) {
            instancia = new CinemaUQ();
        }
        return instancia;
    }

    public Cliente iniciarSesion(String identificacion, String clave) {
        if (identificacion == null || clave == null) {
            return null;
        }
        Cliente cliente = buscarCliente(identificacion.trim());
        if (cliente == null || !cliente.getClave().equals(clave)) {
            return null;
        }
        if (!cliente.isActivo()) {
            throw new CompraInvalidaException("Tu cuenta está inactiva. Pide ayuda en taquilla.");
        }
        return cliente;
    }

    public Cliente registrarCliente(String nombre, String identificacion, String clave) {
        if (nombre == null || nombre.isBlank() || identificacion == null || identificacion.isBlank()
                || clave == null || clave.isBlank()) {
            throw new CompraInvalidaException("Completa nombre, identificación y clave.");
        }
        String id = identificacion.trim();
        if (id.contains(" ")) {
            throw new CompraInvalidaException("Usa una identificación sin espacios.");
        }
        if (buscarCliente(id) != null) {
            throw new CompraInvalidaException("Esa identificación ya existe. Inicia sesión o elige otra.");
        }
        Cliente cliente = new Cliente(nombre, id, clave, Rol.CLIENTE, 0);
        clientes.add(cliente);
        return cliente;
    }

    public Compra comprar(Compra compra) {
        Compra confirmada = fachada.procesarCompra(compra);
        compras.add(confirmada);
        return confirmada;
    }

    public String nuevoIdCompra() {
        return "C-" + seqCompra++;
    }

    public void cancelarCompra(Cliente cliente, Compra compra) {
        if (cliente == null || compra == null || compra.getCliente() != cliente) {
            throw new CompraInvalidaException("Esta compra no es tuya.");
        }
        if (compra.getFuncion().estaCancelada()) {
            throw new CompraInvalidaException("El cine ya canceló esta función y devolvió el dinero.");
        }
        double porcentaje = ReglasCancelacion.porcentaje(LocalDateTime.now(), compra.getFuncion().getFechaHora());
        compra.reembolsar(porcentaje, EstadoCompra.CANCELADA, "Cancelación del cliente");
    }

    public void cancelarFuncion(Cliente admin, Funcion funcion) {
        exigirAdmin(admin);
        if (funcion == null) {
            throw new CompraInvalidaException("Elige una función.");
        }
        if (funcion.estaCancelada()) {
            throw new CompraInvalidaException("Esta función ya está cancelada.");
        }
        List<Compra> afectadas = new ArrayList<>();
        for (Compra compra : compras) {
            if (compra.getFuncion() == funcion && compra.getEstado() == EstadoCompra.ACTIVA) {
                afectadas.add(compra);
            }
        }
        for (Compra compra : afectadas) {
            compra.reembolsar(1.0, EstadoCompra.REEMBOLSADA, "El cine canceló la función");
        }
        funcion.marcarCancelada();
        funcion.notificar("Se canceló la función de " + funcion.getPelicula().getTitulo()
                + " del " + Textos.fechaHora(funcion.getFechaHora())
                + ". Devolvimos el 100%. Revisa Mi tarjeta o Mis puntos.");
    }

    public void recargarTarjeta(Cliente admin, Cliente destino, double monto) {
        exigirAdmin(admin);
        if (destino == null) {
            throw new CompraInvalidaException("Elige el cliente que va a recibir la recarga.");
        }
        destino.getTarjeta().recargar(monto);
    }

    public void cambiarTarjetaActiva(Cliente admin, Cliente destino, boolean activa) {
        exigirAdmin(admin);
        if (destino == null) {
            throw new CompraInvalidaException("Elige un cliente.");
        }
        if (activa) {
            destino.getTarjeta().activar();
        } else {
            destino.getTarjeta().desactivar();
        }
    }

    public void cambiarCuentaActiva(Cliente admin, Cliente destino, boolean activa) {
        exigirAdmin(admin);
        if (destino == null) {
            throw new CompraInvalidaException("Elige un cliente.");
        }
        if (destino.esAdmin()) {
            throw new CompraInvalidaException("La cuenta del administrador no se desactiva.");
        }
        destino.setActivo(activa);
    }

    public Pelicula agregarPelicula(Cliente admin, String titulo, int duracion, String genero) {
        exigirAdmin(admin);
        Pelicula pelicula = new Pelicula("P" + seqPelicula++, titulo, duracion, genero);
        peliculas.add(pelicula);
        return pelicula;
    }

    public Sala agregarSala(Cliente admin, String nombre, int filas, int columnas) {
        exigirAdmin(admin);
        Sala sala = new Sala("S" + seqSala++, nombre, filas, columnas);
        salas.add(sala);
        return sala;
    }

    public Funcion agregarFuncion(Cliente admin, Pelicula pelicula, Sala sala, LocalDateTime fechaHora) {
        exigirAdmin(admin);
        if (pelicula == null || sala == null) {
            throw new CompraInvalidaException("Elige la película y la sala.");
        }
        if (fechaHora == null) {
            throw new CompraInvalidaException("Elige la fecha y la hora.");
        }
        Funcion funcion = new Funcion("F" + seqFuncion++, pelicula, sala, fechaHora);
        funciones.add(funcion);
        return funcion;
    }

    public ProductoConfiteria agregarProducto(Cliente admin, String nombre, double precio) {
        exigirAdmin(admin);
        if (nombre == null || nombre.isBlank()) {
            throw new CompraInvalidaException("Escribe el nombre del producto.");
        }
        if (precio <= 0) {
            throw new CompraInvalidaException("El precio tiene que ser mayor que cero.");
        }
        ProductoConfiteria producto = new ProductoConfiteria("PR" + seqProducto++, nombre.trim(), Math.round(precio));
        productos.add(producto);
        return producto;
    }

    public Combo agregarCombo(Cliente admin, String nombre, double descuentoFraccion, List<ProductoConfiteria> elegidos) {
        exigirAdmin(admin);
        if (nombre == null || nombre.isBlank()) {
            throw new CompraInvalidaException("Escribe el nombre del combo.");
        }
        if (elegidos == null || elegidos.size() < 2) {
            throw new CompraInvalidaException("Elige al menos dos productos para el combo.");
        }
        if (descuentoFraccion < 0 || descuentoFraccion > 0.5) {
            throw new CompraInvalidaException("El descuento del combo va de 0 a 50. Es aparte de la promoción del día.");
        }
        Combo combo = new Combo("CB" + seqCombo++, nombre.trim(), descuentoFraccion);
        for (ProductoConfiteria producto : elegidos) {
            combo.agregarProducto(producto);
        }
        combos.add(combo);
        return combo;
    }

    public Promocion agregarPromocion(Cliente admin, String nombre, TipoPromocion tipo, DayOfWeek dia,
                                      double porcentaje, String nombreProducto) {
        exigirAdmin(admin);
        if (nombre == null || nombre.isBlank()) {
            throw new CompraInvalidaException("Escribe el nombre de la promoción.");
        }
        Promocion promocion;
        if (tipo == TipoPromocion.DOS_POR_UNO) {
            if (nombreProducto == null || nombreProducto.isBlank()) {
                throw new CompraInvalidaException("Elige el producto del 2x1.");
            }
            promocion = new Promocion("PM" + seqPromo++, nombre.trim(), tipo, null, 0, nombreProducto);
        } else {
            if (dia == null) {
                throw new CompraInvalidaException("Elige el día de la promoción.");
            }
            if (porcentaje <= 0 || porcentaje > 0.9) {
                throw new CompraInvalidaException("Usa un porcentaje entre 1 y 90.");
            }
            promocion = new Promocion("PM" + seqPromo++, nombre.trim(), TipoPromocion.PORCENTAJE_DIA,
                    dia, porcentaje, null);
        }
        promociones.add(promocion);
        return promocion;
    }

    public Reporte generarReporte(TipoReporte tipo, LocalDate desde, LocalDate hasta) {
        if (tipo == null || desde == null || hasta == null) {
            throw new CompraInvalidaException("Elige el tipo de reporte y las dos fechas.");
        }
        if (desde.isAfter(hasta)) {
            throw new CompraInvalidaException("La fecha inicial no puede ser posterior a la final.");
        }
        return new GeneradorReporte().generar(tipo, desde, hasta, compras, funciones, clientes, peliculas);
    }

    public EstadisticasCine estadisticas() {
        return EstadisticasCine.calcular(compras, funciones, peliculas, clientes);
    }

    public Cliente buscarCliente(String identificacion) {
        if (identificacion == null) {
            return null;
        }
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equalsIgnoreCase(identificacion.trim())) {
                return cliente;
            }
        }
        return null;
    }

    public List<Pelicula> getPeliculas() {
        return List.copyOf(peliculas);
    }

    public List<Sala> getSalas() {
        return List.copyOf(salas);
    }

    public List<Funcion> getFunciones() {
        return List.copyOf(funciones);
    }

    public List<Cliente> getClientes() {
        return List.copyOf(clientes);
    }

    public List<Compra> getCompras() {
        return List.copyOf(compras);
    }

    public List<ProductoConfiteria> getProductos() {
        return List.copyOf(productos);
    }

    public List<Combo> getCombos() {
        return List.copyOf(combos);
    }

    public List<Promocion> getPromociones() {
        return List.copyOf(promociones);
    }

    public List<Cliente> getClientesRegistrados() {
        List<Cliente> soloClientes = new ArrayList<>();
        for (Cliente cliente : clientes) {
            if (!cliente.esAdmin()) {
                soloClientes.add(cliente);
            }
        }
        return soloClientes;
    }

    private void exigirAdmin(Cliente actor) {
        if (actor == null || !actor.esAdmin()) {
            throw new OperacionSoloAdminException("Esta acción es solo del administrador.");
        }
    }

    private void cargarSemilla() {
        LocalDateTime ahora = LocalDateTime.now();
        Cliente admin = new Cliente("Administrador", "admin", "admin123", Rol.ADMIN, 0);
        Cliente ana = new Cliente("Ana Gómez", "ana", "ana123", Rol.CLIENTE, 200000);
        Cliente luis = new Cliente("Luis Pérez", "luis", "luis123", Rol.CLIENTE, 90000);
        clientes.add(admin);
        clientes.add(ana);
        clientes.add(luis);
        ana.getCuentaPuntos().acumular(300, "Puntos de bienvenida");
        luis.getCuentaPuntos().cargarLote(500, ahora.minusDays(400), ahora.minusDays(20), "Puntos vencidos");
        luis.getCuentaPuntos().cargarLote(40, ahora.minusDays(30), ahora.plusDays(12), "Puntos que vencen pronto");
        luis.getCuentaPuntos().cargarLote(80, ahora.minusDays(5), ahora.plusDays(200), "Puntos del mes");

        Pelicula condor = new Pelicula("P" + seqPelicula++, "El viaje del cóndor", 118, "Aventura");
        Pelicula rio = new Pelicula("P" + seqPelicula++, "Río Quindío", 95, "Drama");
        Pelicula medianoche = new Pelicula("P" + seqPelicula++, "Medianoche en Armenia", 110, "Suspenso");
        peliculas.add(condor);
        peliculas.add(rio);
        peliculas.add(medianoche);

        Sala sala1 = new Sala("S" + seqSala++, "Sala 1", 4, 6);
        Sala sala2 = new Sala("S" + seqSala++, "Sala 2", 4, 6);
        salas.add(sala1);
        salas.add(sala2);

        Funcion lejana = new Funcion("F" + seqFuncion++, condor, sala1, ahora.plusHours(30));
        Funcion media = new Funcion("F" + seqFuncion++, rio, sala2, ahora.plusHours(10));
        Funcion pronto = new Funcion("F" + seqFuncion++, medianoche, sala1, ahora.plusHours(1));
        Funcion otra = new Funcion("F" + seqFuncion++, condor, sala2, ahora.plusHours(50));
        funciones.add(lejana);
        funciones.add(media);
        funciones.add(pronto);
        funciones.add(otra);

        ProductoConfiteria crispetas = new ProductoConfiteria("PR" + seqProducto++, "Crispetas grandes", 15000);
        ProductoConfiteria gaseosa = new ProductoConfiteria("PR" + seqProducto++, "Gaseosa", 6000);
        ProductoConfiteria nachos = new ProductoConfiteria("PR" + seqProducto++, "Nachos", 12000);
        productos.add(crispetas);
        productos.add(gaseosa);
        productos.add(nachos);

        Combo combo = new Combo("CB" + seqCombo++, "Combo clásico", 0.10);
        combo.agregarProducto(crispetas);
        combo.agregarProducto(gaseosa);
        combos.add(combo);

        promociones.add(new Promocion(
                "PM" + seqPromo++,
                "Confitería del día",
                TipoPromocion.PORCENTAJE_DIA,
                ahora.getDayOfWeek(),
                0.20,
                null));

        Asiento silla = null;
        for (Asiento asiento : lejana.getAsientos()) {
            if ("A1".equals(asiento.getCodigo())) {
                silla = asiento;
            }
        }
        Compra semilla = new CompraBuilder()
                .id(nuevoIdCompra())
                .cliente(ana)
                .funcion(lejana)
                .asientos(List.of(silla))
                .items(List.of(new CreadorEntradaGeneral().crearEntrada()))
                .descuento(0, "")
                .medioPago(MedioPago.TARJETA)
                .build();
        comprar(semilla);
    }
}
