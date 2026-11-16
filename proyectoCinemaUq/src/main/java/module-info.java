module co.uniquindio.proyectocinemauq {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.uniquindio.proyectocinemauq to javafx.fxml;
    exports co.uniquindio.proyectocinemauq;
    exports co.uniquindio.proyectocinemauq.decorator;
    opens co.uniquindio.proyectocinemauq.decorator to javafx.fxml;
}