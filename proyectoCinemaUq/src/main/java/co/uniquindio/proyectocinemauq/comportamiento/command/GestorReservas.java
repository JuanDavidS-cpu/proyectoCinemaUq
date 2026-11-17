package co.uniquindio.proyectocinemauq.comportamiento.command;

/** La grilla le entrega el comando y él lo ejecuta. */
public class GestorReservas {

    public void ejecutar(Comando comando) {
        if (comando == null) {
            return;
        }
        comando.ejecutar();
    }
}
