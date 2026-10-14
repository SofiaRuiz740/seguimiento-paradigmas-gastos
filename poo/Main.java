import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestorGastos gestor = new GestorGastos();
        cargarEjemplos(gestor);

        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            switch (leerOpcion(scanner)) {
                case 1 -> registrar(gestor, scanner);
                case 2 -> mostrarLista("Gastos registrados", gestor.listarGastos());
                case 3 -> mostrarTotal(gestor.calcularTotal());
                case 4 -> mostrarMayor(gestor.obtenerGastoMayor());
                case 5 -> filtrar(gestor, scanner);
                case 6 -> buscar(gestor, scanner);
                case 0 -> seguir = false;
                default -> System.out.println("Opción no válida.");
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void cargarEjemplos(GestorGastos gestor) {
        gestor.agregarGasto(new Gasto("Almuerzo", 25, "Alimentación"));
        gestor.agregarGasto(new Gasto("Bus urbano", 8, "Transporte"));
        gestor.agregarGasto(new Gasto("Recibo de luz", 80, "Servicios"));
        gestor.agregarGasto(new Gasto("Cine", 35, "Entretenimiento"));
        gestor.agregarGasto(new Gasto("Mercado semanal", 120, "Alimentación"));
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== Control de gastos (POO) ===");
        System.out.println("1. Registrar gasto");
        System.out.println("2. Listar gastos");
        System.out.println("3. Calcular gasto total");
        System.out.println("4. Mostrar gasto mayor");
        System.out.println("5. Filtrar por monto");
        System.out.println("6. Buscar por concepto");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private static void registrar(GestorGastos gestor, Scanner scanner) {
        try {
            System.out.print("Concepto: ");
            String concepto = scanner.nextLine();
            System.out.print("Monto: ");
            double monto = leerMonto(scanner);
            String categoria = leerCategoria(scanner);
            gestor.agregarGasto(new Gasto(concepto, monto, categoria));
            System.out.println("Gasto registrado.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void filtrar(GestorGastos gestor, Scanner scanner) {
        System.out.print("Mostrar gastos mayores a: ");
        double minimo = leerMonto(scanner);
        mostrarLista("Gastos mayores a $" + String.format(Locale.US, "%.2f", minimo),
                gestor.filtrarPorMonto(minimo));
    }

    private static void buscar(GestorGastos gestor, Scanner scanner) {
        System.out.print("Concepto a buscar: ");
        String texto = scanner.nextLine();
        try {
            mostrarLista("Resultados para \"" + texto.trim() + "\"", gestor.buscarPorConcepto(texto));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void mostrarLista(String titulo, List<Gasto> gastos) {
        System.out.println(titulo + ":");
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos para mostrar.");
            return;
        }
        for (Gasto gasto : gastos) {
            System.out.println("- " + gasto);
        }
    }

    private static void mostrarTotal(double total) {
        System.out.println("Gasto total: $" + String.format(Locale.US, "%.2f", total));
    }

    private static void mostrarMayor(Gasto mayor) {
        if (mayor == null) {
            System.out.println("No hay gastos registrados.");
            return;
        }
        System.out.println("Gasto mayor: " + mayor);
    }

    private static String leerCategoria(Scanner scanner) {
        System.out.println("Categoría:");
        for (int i = 0; i < Gasto.CATEGORIAS.size(); i++) {
            System.out.println((i + 1) + ". " + Gasto.CATEGORIAS.get(i));
        }
        System.out.print("Número de categoría: ");
        int indice = leerOpcion(scanner);
        if (indice < 1 || indice > Gasto.CATEGORIAS.size()) {
            throw new IllegalArgumentException("Categoría no válida.");
        }
        return Gasto.CATEGORIAS.get(indice - 1);
    }

    private static int leerOpcion(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            if (!scanner.hasNext()) {
                return 0;
            }
            scanner.next();
            System.out.print("Ingresa un número: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static double leerMonto(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            if (!scanner.hasNext()) {
                return 0;
            }
            scanner.next();
            System.out.print("Ingresa un monto válido: ");
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }
}
