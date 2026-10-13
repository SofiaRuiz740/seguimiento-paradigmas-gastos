import java.util.Scanner;

public class GastosEstructurados {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        String[] conceptos = new String[100];
        double[] montos = new double[100];
        String[] categorias = new String[100];

        int cantidad = 0;
        int opcion;

        do {
            System.out.println("\n===== CONTROL DE GASTOS =====");
            System.out.println("1. Registrar gasto");
            System.out.println("2. Mostrar listado de gastos");
            System.out.println("3. Calcular gasto total");
            System.out.println("4. Mostrar gasto de mayor valor");
            System.out.println("5. Mostrar gastos mayores a un monto");
            System.out.println("6. Buscar gasto por concepto");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    if (cantidad < conceptos.length) {
                        cantidad = registrarGasto(
                                conceptos, montos, categorias, cantidad
                        );
                    } else {
                        System.out.println("No hay espacio para mas gastos.");
                    }
                    break;

                case 2:
                    mostrarGastos(conceptos, montos, categorias, cantidad);
                    break;

                case 3:
                    System.out.println("Gasto total: $"
                            + calcularTotal(montos, cantidad));
                    break;

                case 4:
                    mostrarMayor(conceptos, montos, categorias, cantidad);
                    break;

                case 5:
                    System.out.print("Ingrese el monto limite: ");
                    double limite = Double.parseDouble(sc.nextLine());
                    mostrarMayores(conceptos, montos, categorias,
                            cantidad, limite);
                    break;

                case 6:
                    System.out.print("Ingrese el concepto a buscar: ");
                    String palabra = sc.nextLine();
                    buscarConcepto(conceptos, montos, categorias,
                            cantidad, palabra);
                    break;

                case 0:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    public static int registrarGasto(
            String[] conceptos, double[] montos,
            String[] categorias, int cantidad) {

        System.out.print("Concepto: ");
        conceptos[cantidad] = sc.nextLine();

        System.out.print("Monto: ");
        montos[cantidad] = Double.parseDouble(sc.nextLine());

        System.out.print("Categoria: ");
        categorias[cantidad] = sc.nextLine();

        cantidad++;

        System.out.println("Gasto registrado correctamente.");

        return cantidad;
    }

    public static void mostrarGastos(
            String[] conceptos, double[] montos,
            String[] categorias, int cantidad) {

        if (cantidad == 0) {
            System.out.println("No hay gastos registrados.");
            return;
        }

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nGasto " + (i + 1));
            System.out.println("Concepto: " + conceptos[i]);
            System.out.println("Monto: $" + montos[i]);
            System.out.println("Categoria: " + categorias[i]);
        }
    }

    public static double calcularTotal(
            double[] montos, int cantidad) {

        double total = 0;

        for (int i = 0; i < cantidad; i++) {
            total += montos[i];
        }

        return total;
    }

    public static void mostrarMayor(
            String[] conceptos, double[] montos,
            String[] categorias, int cantidad) {

        if (cantidad == 0) {
            System.out.println("No hay gastos registrados.");
            return;
        }

        int posicionMayor = 0;

        for (int i = 1; i < cantidad; i++) {
            if (montos[i] > montos[posicionMayor]) {
                posicionMayor = i;
            }
        }

        System.out.println("Gasto de mayor valor:");
        System.out.println("Concepto: " + conceptos[posicionMayor]);
        System.out.println("Monto: $" + montos[posicionMayor]);
        System.out.println("Categoria: " + categorias[posicionMayor]);
    }

    public static void mostrarMayores(
            String[] conceptos, double[] montos,
            String[] categorias, int cantidad, double limite) {

        boolean encontrado = false;

        for (int i = 0; i < cantidad; i++) {
            if (montos[i] > limite) {
                System.out.println(conceptos[i] + " - $"
                        + montos[i] + " - " + categorias[i]);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay gastos superiores a ese monto.");
        }
    }

    public static void buscarConcepto(
            String[] conceptos, double[] montos,
            String[] categorias, int cantidad, String palabra) {

        boolean encontrado = false;

        for (int i = 0; i < cantidad; i++) {
            if (conceptos[i].toLowerCase()
                    .contains(palabra.toLowerCase())) {

                System.out.println(conceptos[i] + " - $"
                        + montos[i] + " - " + categorias[i]);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron coincidencias.");
        }
    }
}
