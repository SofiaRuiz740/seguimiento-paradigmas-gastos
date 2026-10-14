import java.util.ArrayList;
import java.util.List;

public class GestorGastos {
    private final List<Gasto> gastos = new ArrayList<>();

    public void agregarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo.");
        }
        gastos.add(gasto);
    }

    public List<Gasto> listarGastos() {
        return new ArrayList<>(gastos);
    }

    public double calcularTotal() {
        double total = 0;
        for (Gasto gasto : gastos) {
            total += gasto.getMonto();
        }
        return total;
    }

    public Gasto obtenerGastoMayor() {
        if (gastos.isEmpty()) {
            return null;
        }
        Gasto mayor = gastos.get(0);
        for (Gasto gasto : gastos) {
            if (gasto.getMonto() > mayor.getMonto()) {
                mayor = gasto;
            }
        }
        return mayor;
    }

    public List<Gasto> filtrarPorMonto(double minimo) {
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getMonto() > minimo) {
                resultado.add(gasto);
            }
        }
        return resultado;
    }

    public List<Gasto> buscarPorConcepto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto de búsqueda no puede estar vacío.");
        }
        String buscado = texto.trim().toLowerCase();
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getConcepto().toLowerCase().contains(buscado)) {
                resultado.add(gasto);
            }
        }
        return resultado;
    }
}
