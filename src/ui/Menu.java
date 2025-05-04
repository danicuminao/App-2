package ui;

import services.GestionService;
import models.Estado;
import models.Cultivo;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final GestionService service;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(GestionService service) {
        this.service = service;
    }

    public void start() {
        int opc;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestión de Cultivos");
            System.out.println("2. Gestión de Parcelas");
            System.out.println("3. Gestión de Actividades");
            System.out.println("4. Búsqueda/Reporte");
            System.out.println("5. Salir");
            opc = readInt("Opción: ");
            switch (opc) {
                case 1 -> gestionCultivos();
                case 2 -> gestionParcelas();
                case 3 -> gestionActividades();
                case 4 -> busquedaReporte();
                case 5 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 5);
    }

    private void gestionCultivos() {
        int op;
        do {
            System.out.println("\n-- CULTIVOS --");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Eliminar");
            System.out.println("4. Editar");
            System.out.println("5. Asignar a Parcela");
            System.out.println("6. Volver");
            op = readInt("Opción: ");
            switch (op) {
                case 1 -> service.listarCultivos();
                case 2 -> {
                    String n = readString("Nombre: ");
                    String v = readString("Variedad: ");
                    double s = readDouble("Superficie (ha): ");
                    String p = readString("Código parcela: ");
                    String f = readString("Fecha siembra (YYYY-MM-DD): ");
                    Estado e = Estado.valueOf(readString("Estado (ACTIVO,EN_RIESGO,COSECHADO): "));
                    service.crearCultivo(n,v,s,p,f,e);
                }
                case 3 -> {
                    service.listarCultivos();
                    int i = readInt("Índice a eliminar: ");
                    service.eliminarCultivo(i);
                }
                case 4 -> {
                    service.listarCultivos();
                    int i = readInt("Índice a editar: ");
                    String nn = readString("Nuevo nombre: ");
                    String nv = readString("Nueva variedad: ");
                    service.editarCultivo(i, nn, nv);
                }
                case 5 -> {
                    service.listarCultivos();
                    int i = readInt("Índice a reasignar: ");
                    String np = readString("Nuevo código parcela: ");
                    service.asignarCultivoAParcela(i, np);
                }
                case 6 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op != 6);
    }

    private void gestionParcelas() {
        int op;
        do {
            System.out.println("\n-- PARCELAS --");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Eliminar");
            System.out.println("4. Editar");
            System.out.println("5. Volver");
            op = readInt("Opción: ");
            switch (op) {
                case 1 -> service.listarParcelas();
                case 2 -> {
                    String c = readString("Código parcela: ");
                    service.crearParcela(c);
                }
                case 3 -> {
                    String c = readString("Código a eliminar: ");
                    service.eliminarParcela(c);
                }
                case 4 -> {
                    String old = readString("Código actual: ");
                    String neu = readString("Nuevo código: ");
                    service.editarParcela(old, neu);
                }
                case 5 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op != 5);
    }

    private void gestionActividades() {
        int op;
        do {
            System.out.println("\n-- ACTIVIDADES --");
            System.out.println("1. Listar por cultivo");
            System.out.println("2. Registrar");
            System.out.println("3. Eliminar");
            System.out.println("4. Marcar completada");
            System.out.println("5. Volver");
            op = readInt("Opción: ");
            switch (op) {
                case 1 -> {
                    service.listarCultivos();
                    int i = readInt("Índice cultivo: ");
                    service.listarActividades(i);
                }
                case 2 -> {
                    service.listarCultivos();
                    int i = readInt("Índice cultivo: ");
                    String t = readString("Tipo actividad: ");
                    String f = readString("Fecha (YYYY-MM-DD): ");
                    service.registrarActividad(i, t, f);
                }
                case 3 -> {
                    service.listarCultivos();
                    int i = readInt("Cultivo: ");
                    service.listarActividades(i);
                    int a = readInt("Índice actividad: ");
                    service.eliminarActividad(i, a);
                }
                case 4 -> {
                    service.listarCultivos();
                    int i = readInt("Cultivo: ");
                    service.listarActividades(i);
                    int a = readInt("Actividad a marcar: ");
                    service.marcarActividadCompletada(i, a);
                }
                case 5 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op != 5);
    }

    private void busquedaReporte() {
        int op;
        do {
            System.out.println("\n-- BÚSQUEDA / REPORTE --");
            System.out.println("1. Buscar cultivos");
            System.out.println("2. Reporte de estados");
            System.out.println("3. Volver");
            op = readInt("Opción: ");
            switch (op) {
                case 1 -> {
                    String c = readString("Nombre/Variedad: ");
                    List<Cultivo> r = service.buscarCultivos(c);
                    if (r.isEmpty()) System.out.println("No hay resultados.");
                    else r.forEach(System.out::println);
                }
                case 2 -> service.reporteCultivos();
                case 3 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op != 3);
    }

    private int readInt(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Número válido: ");
            scanner.next();
        }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }
    private double readDouble(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextDouble()) {
            System.out.print("Número válido: ");
            scanner.next();
        }
        double v = scanner.nextDouble();
        scanner.nextLine();
        return v;
    }
    private String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}
