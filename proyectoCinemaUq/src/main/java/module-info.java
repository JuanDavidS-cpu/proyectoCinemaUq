module co.uniquindio.proyectocinemauq {
    requires javafx.controls;
    requires javafx.fxml;
    requires static org.junit.jupiter.api;

    exports co.uniquindio.proyectocinemauq;
    exports co.uniquindio.proyectocinemauq.excepciones;
    exports co.uniquindio.proyectocinemauq.mvc.modelo;
    exports co.uniquindio.proyectocinemauq.mvc.vista;
    exports co.uniquindio.proyectocinemauq.mvc.controlador;
    exports co.uniquindio.proyectocinemauq.creacional.builder;
    exports co.uniquindio.proyectocinemauq.creacional.factory;
    exports co.uniquindio.proyectocinemauq.creacional.prototype;
    exports co.uniquindio.proyectocinemauq.creacional.singleton;
    exports co.uniquindio.proyectocinemauq.estructural.adapter;
    exports co.uniquindio.proyectocinemauq.estructural.composite;
    exports co.uniquindio.proyectocinemauq.estructural.decorator;
    exports co.uniquindio.proyectocinemauq.estructural.facade;
    exports co.uniquindio.proyectocinemauq.estructural.proxy;
    exports co.uniquindio.proyectocinemauq.comportamiento.chain;
    exports co.uniquindio.proyectocinemauq.comportamiento.command;
    exports co.uniquindio.proyectocinemauq.comportamiento.observer;
    exports co.uniquindio.proyectocinemauq.comportamiento.state;
    exports co.uniquindio.proyectocinemauq.comportamiento.strategy;

    opens co.uniquindio.proyectocinemauq.mvc.controlador to javafx.fxml;
}
