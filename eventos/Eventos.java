package eventos;

import eventos.Gasto.Categoria;

import java.util.List;

/**
 * Catálogo de todos los eventos de la aplicación.
 * Las solicitudes las publica la ventana cuando el usuario actúa;
 * los resultados los publica el manejador después de procesarlas.
 */
public final class Eventos {

    private Eventos() {
    }

    // ----- Solicitudes: lo que pide el usuario -----

    public record RegistrarGasto(String concepto, String monto, Categoria categoria) {
    }

    public record VerTodos() {
    }

    public record CalcularTotal() {
    }

    public record BuscarMayor() {
    }

    public record FiltrarPorMonto(String montoMinimo) {
    }

    public record BuscarPorConcepto(String texto) {
    }

    // ----- Resultados: lo que ocurrió -----

    public record GastoRegistrado(Gasto gasto, List<Gasto> todos) {
    }

    public record ListaActualizada(String titulo, List<Gasto> gastos) {
    }

    public record TotalCalculado(double total, int cantidad) {
    }

    public record MayorEncontrado(Gasto gasto) {
    }

    public record ErrorOcurrido(String mensaje, boolean enFormulario) {
    }
}
