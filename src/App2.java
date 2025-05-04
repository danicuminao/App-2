import services.GestionService;
import ui.Menu;

public class App2 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java App2 <archivo_csv>");
            System.exit(1);
        }
        String filename = args[0];
        GestionService service = new GestionService();
        service.loadData(filename);
        Menu menu = new Menu(service);
        menu.start();
        service.saveData(filename);
        System.out.println("Datos guardados en " + filename);
    }
}
