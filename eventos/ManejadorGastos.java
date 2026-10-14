package eventos;

import eventos.Eventos.BuscarMayor;
import eventos.Eventos.BuscarPorConcepto;
import eventos.Eventos.CalcularTotal;
import eventos.Eventos.ErrorOcurrido;
import eventos.Eventos.FiltrarPorMonto;
import eventos.Eventos.GastoRegistrado;
import eventos.Eventos.ListaActualizada;
import eventos.Eventos.MayorEncontrado;
import eventos.Eventos.RegistrarGasto;
import eventos.Eventos.TotalCalculado;
import eventos.Eventos.VerTodos;

import java.util.ArrayList;
import java.util.List;

/**
 * Escucha las solicitudes del usuario, aplica la lógica del negocio
 * y publica el resultado como un nuevo evento. No conoce la ventana.
 */
public class ManejadorGastos {

    private final List<Gasto> gastos = new ArrayList<>();
    private final BusEventos bus;

    public ManejadorGastos(BusEventos bus) {
        this.bus = bus;
    }

    public void escucharSolicitudes() {
        bus.suscribir(RegistrarGasto.class, this::registrar);
        bus.suscribir(VerTodos.class, this::verTodos);
        bus.suscribir(CalcularTotal.class, this::calcularTotal);
        bus.suscribir(BuscarMayor.class, this::buscarMayor);
        bus.suscribir(FiltrarPorMonto.class, this::filtrarPorMonto);
        bus.suscribir(BuscarPorConcepto.class, this::buscarPorConcepto);
    }

    private void registrar(RegistrarGasto solicitud) {
        try {
            Gasto gasto = new Gasto(solicitud.concepto(), leerMonto(solicitud.monto()), solicitud.categoria());
            gastos.add(gasto);
            bus.publicar(new GastoRegistrado(gasto, List.copyOf(gastos)));
        } catch (IllegalArgumentException error) {
            bus.publicar(new ErrorOcurrido(error.getMessage(), true));
        }
    }

    private void verTodos(VerTodos solicitud) {
        bus.publicar(new ListaActualizada("Todos los gastos", List.copyOf(gastos)));
    }

    private void calcularTotal(CalcularTotal solicitud) {
        if (!hayGastos()) {
            return;
        }
        double total = 0;
        for (Gasto gasto : gastos) {
            total += gasto.monto();
        }
        bus.publicar(new TotalCalculado(total, gastos.size()));
    }

    private void buscarMayor(BuscarMayor solicitud) {
        if (!hayGastos()) {
            return;
        }
        Gasto mayor = gastos.get(0);
        for (Gasto gasto : gastos) {
            if (gasto.monto() > mayor.monto()) {
                mayor = gasto;
            }
        }
        bus.publicar(new MayorEncontrado(mayor));
    }

    private void filtrarPorMonto(FiltrarPorMonto solicitud) {
        if (!hayGastos()) {
            return;
        }
        try {
            double minimo = leerMonto(solicitud.montoMinimo());
            List<Gasto> resultado = new ArrayList<>();
            for (Gasto gasto : gastos) {
                if (gasto.monto() > minimo) {
                    resultado.add(gasto);
                }
            }
            bus.publicar(new ListaActualizada("Mayores a " + Estilo.dinero(minimo), resultado));
        } catch (IllegalArgumentException error) {
            bus.publicar(new ErrorOcurrido(error.getMessage(), false));
        }
    }

    private void buscarPorConcepto(BuscarPorConcepto solicitud) {
        String texto = solicitud.texto().strip().toLowerCase();
        if (texto.isEmpty()) {
            bus.publicar(new ErrorOcurrido("Escribe una palabra para buscar.", false));
            return;
        }
        if (!hayGastos()) {
            return;
        }
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.concepto().toLowerCase().contains(texto)) {
                resultado.add(gasto);
            }
        }
        bus.publicar(new ListaActualizada("Resultados para \"" + solicitud.texto().strip() + "\"", resultado));
    }

    private boolean hayGastos() {
        if (gastos.isEmpty()) {
            bus.publicar(new ErrorOcurrido("Aún no hay gastos registrados.", false));
            return false;
        }
        return true;
    }

    /** Convierte textos como "25.000" o "$ 1.500,50" en número. */
    private static double leerMonto(String texto) {
        String limpio = texto.replace("$", "").replace(" ", "").replace(".", "").replace(",", ".");
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("Ingresa un monto.");
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("El monto \"" + texto.strip() + "\" no es válido.");
        }
    }
}
