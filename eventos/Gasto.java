package eventos;

/** Un gasto registrado. Es inmutable y valida sus datos al crearse. */
public record Gasto(String concepto, double monto, Categoria categoria) {

    public enum Categoria {
        ALIMENTACION("Alimentación"),
        TRANSPORTE("Transporte"),
        SERVICIOS("Servicios"),
        ENTRETENIMIENTO("Entretenimiento");

        private final String nombre;

        Categoria(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public Gasto {
        if (concepto == null || concepto.isBlank()) {
            throw new IllegalArgumentException("Escribe el concepto del gasto.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        concepto = concepto.strip();
    }
}
