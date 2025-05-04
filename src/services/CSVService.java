package services;

import models.*;
import java.io.*;
import java.time.LocalDate;

public class CSVService {
    public static void readData(String filename, GestionService service) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 8);
                if (parts.length < 8 || !stripQuotes(parts[0]).equals("Cultivo")) continue;


                String nombre        = stripQuotes(parts[1]);
                String variedad      = stripQuotes(parts[2]);
                double superficie    = Double.parseDouble(parts[3]);
                String codParcela    = stripQuotes(parts[4]);
                LocalDate fechaSiem  = LocalDate.parse(stripQuotes(parts[5]));
                Estado estado        = Estado.valueOf(stripQuotes(parts[6]));

                Parcela parcela = service.getOrCreateParcela(codParcela);
                Cultivo cultivo = new Cultivo(nombre, variedad, superficie,
                                              parcela, fechaSiem, estado);
                parcela.addCultivo(cultivo);
                service.addCultivo(cultivo);

                String actsStr = parts[7].trim();
                if (actsStr.startsWith("[") && actsStr.endsWith("]")) {
                    actsStr = actsStr.substring(1, actsStr.length()-1);
                    if (!actsStr.isEmpty()) {
                        String[] actParts = actsStr.split(", (?=\")");
                        for (String ap : actParts) {
                            String act = stripQuotes(ap.trim());
                            String[] tp = act.split(":", 2);
                            cultivo.addActividad(
                              new Actividad(tp[0], LocalDate.parse(tp[1]))
                            );
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo CSV: " + e.getMessage());
        }
    }

    public static void writeData(String filename, GestionService service) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Cultivo c : service.getCultivos()) {
                bw.write("Cultivo");
                bw.write(",\"" + c.getNombre() + "\"");
                bw.write(",\"" + c.getVariedad() + "\"");
                bw.write(","  + c.getSuperficie());
                bw.write(",\"" + c.getParcela().getCodigo() + "\"");
                bw.write(",\"" + c.getFechaSiembra() + "\"");
                bw.write(",\"" + c.getEstado().name() + "\"");
                bw.write(",[");
                for (int i = 0; i < c.getActividades().size(); i++) {
                    Actividad a = c.getActividades().get(i);
                    bw.write("\"" + a.getTipo() + ":" + a.getFecha() + "\"");
                    if (i < c.getActividades().size() - 1) bw.write(", ");
                }
                bw.write("]");
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo CSV: " + e.getMessage());
        }
    }

    private static String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length()-1);
        }
        return s;
    }
}
