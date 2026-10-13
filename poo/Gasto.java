import java.util.List;
import java.util.Locale;

public class Gasto {
    public static final List<String> CATEGORIAS = List.of(
            "Alimentación",
            "Transporte",
            "Servicios",
            "Entretenimiento"
    );

    private String concepto;
    private double monto;
    private String categoria;

    public Gasto(String concepto, double monto, String categoria) {
        setConcepto(concepto);
        setMonto(monto);
        setCategoria(categoria);
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        if (concepto == null || concepto.isBlank()) {
            throw new IllegalArgumentException("El concepto no puede estar vacío.");
        }
        this.concepto = concepto.trim();
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0.");
        }
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || !CATEGORIAS.contains(categoria)) {
            throw new IllegalArgumentException(
                    "La categoría debe ser: " + String.join(", ", CATEGORIAS) + "."
            );
        }
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return concepto + " | $" + String.format(Locale.US, "%.2f", monto) + " | " + categoria;
    }
}
