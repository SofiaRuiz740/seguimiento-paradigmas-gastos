package eventos;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada. Crea el bus, conecta la lógica y la ventana a él,
 * y deja el programa esperando las acciones del usuario.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BusEventos bus = new BusEventos();
            new ManejadorGastos(bus).escucharSolicitudes();
            new VentanaGastos(bus).setVisible(true);
        });
    }
}
