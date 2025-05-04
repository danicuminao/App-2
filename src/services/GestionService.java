package services;

import models.*;
import java.util.*;

public class GestionService {
    private Map<String, Parcela> parcelas = new HashMap<>();
    private List<Cultivo> cultivos    = new ArrayList<>();

    public void loadData(String file)   { CSVService.readData(file, this); }
    public void saveData(String file)   { CSVService.writeData(file, this); }

    public Parcela getOrCreateParcela(String codigo) {
        return parcelas.computeIfAbsent(codigo, Parcela::new);
    }
    public void addCultivo(Cultivo c) { cultivos.add(c); }
    public List<Cultivo> getCultivos() { return cultivos; }
    public List<Parcela> getParcelas() { return new ArrayList<>(parcelas.values()); }

    // --- Gestión de Cultivos ---
    public void listarCultivos() {
        if (cultivos.isEmpty()) {
            System.out.println("No hay cultivos.");
            return;
        }
        for (int i = 0; i < cultivos.size(); i++) {
            System.out.println((i+1) + ". " + cultivos.get(i));
        }
    }
    public void crearCultivo(String nom, String var, double sup,
                             String codPar, String fecha, Estado est) {
        Parcela p = getOrCreateParcela(codPar);
        Cultivo c = new Cultivo(nom, var, sup, p,
                                java.time.LocalDate.parse(fecha), est);
        p.addCultivo(c);
        cultivos.add(c);
        System.out.println(">> Creado: " + c);
    }
    public void eliminarCultivo(int idx) {
        if (idx < 1 || idx > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        Cultivo c = cultivos.get(idx-1);
        if (!c.getActividades().isEmpty()) {
            System.out.println("Actividades pendientes. No se puede eliminar.");
            return;
        }
        c.getParcela().removeCultivo(c);
        cultivos.remove(c);
        System.out.println(">> Cultivo eliminado: " + c.getNombre());
    }
    public void editarCultivo(int idx, String nuevoNom, String nuevaVar) {
        if (idx < 1 || idx > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        Cultivo c = cultivos.get(idx-1);
        c.setNombre(nuevoNom);
        c.setVariedad(nuevaVar);
        System.out.println(">> Cultivo actualizado: " + c);
    }
    public void asignarCultivoAParcela(int idx, String codPar) {
        if (idx < 1 || idx > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        Cultivo c = cultivos.get(idx-1);
        Parcela old = c.getParcela();
        Parcela neu = getOrCreateParcela(codPar);
        old.removeCultivo(c);
        neu.addCultivo(c);
        c.setParcela(neu);
        System.out.println(">> Reasignado a parcela " + codPar);
    }

    // --- Gestión de Parcelas ---
    public void listarParcelas() {
        if (parcelas.isEmpty()) {
            System.out.println("No hay parcelas.");
            return;
        }
        int i = 1;
        for (Parcela p : parcelas.values()) {
            System.out.println((i++) + ". " + p);
        }
    }
    public void crearParcela(String cod) {
        if (parcelas.containsKey(cod)) {
            System.out.println("Ya existe."); return;
        }
        parcelas.put(cod, new Parcela(cod));
        System.out.println(">> Parcela creada: " + cod);
    }
    public void eliminarParcela(String cod) {
        Parcela p = parcelas.get(cod);
        if (p == null) {
            System.out.println("No existe."); return;
        }
        if (!p.getCultivos().isEmpty()) {
            System.out.println("Tiene cultivos. No se puede eliminar."); return;
        }
        parcelas.remove(cod);
        System.out.println(">> Parcela eliminada: " + cod);
    }
    public void editarParcela(String viejo, String nuevo) {
        Parcela p = parcelas.remove(viejo);
        if (p == null) {
            System.out.println("No existe."); return;
        }
        if (parcelas.containsKey(nuevo)) {
            System.out.println("Código en uso."); return;
        }
        p.setCodigo(nuevo);
        parcelas.put(nuevo, p);
        for (Cultivo c : p.getCultivos()) {
            c.setParcela(p);
        }
        System.out.println(">> Parcela renombrada a: " + nuevo);
    }

    // --- Gestión de Actividades ---
    public void listarActividades(int idxCult) {
        if (idxCult < 1 || idxCult > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        Cultivo c = cultivos.get(idxCult-1);
        var acts = c.getActividades();
        if (acts.isEmpty()) {
            System.out.println("Sin actividades."); return;
        }
        for (int i = 0; i < acts.size(); i++) {
            System.out.println((i+1) + ". " + acts.get(i));
        }
    }
    public void registrarActividad(int idxCult, String tipo, String fecha) {
        if (idxCult < 1 || idxCult > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        Actividad a = new Actividad(tipo, java.time.LocalDate.parse(fecha));
        cultivos.get(idxCult-1).addActividad(a);
        System.out.println(">> Actividad registrada: " + a);
    }
    public void eliminarActividad(int idxCult, int idxAct) {
        if (idxCult < 1 || idxCult > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        var acts = cultivos.get(idxCult-1).getActividades();
        if (idxAct < 1 || idxAct > acts.size()) {
            System.out.println("Índice inválido."); return;
        }
        Actividad a = acts.remove(idxAct-1);
        System.out.println(">> Actividad eliminada: " + a);
    }
    public void marcarActividadCompletada(int idxCult, int idxAct) {
        if (idxCult < 1 || idxCult > cultivos.size()) {
            System.out.println("Índice inválido."); return;
        }
        var acts = cultivos.get(idxCult-1).getActividades();
        if (idxAct < 1 || idxAct > acts.size()) {
            System.out.println("Índice inválido."); return;
        }
        acts.get(idxAct-1).setCompletada(true);
        System.out.println(">> Marcada como completada.");
    }

    // --- Búsqueda / Reporte ---
    public List<Cultivo> buscarCultivos(String crit) {
        crit = crit.toLowerCase();
        List<Cultivo> res = new ArrayList<>();
        for (Cultivo c : cultivos) {
            if (c.getNombre().toLowerCase().contains(crit) ||
                c.getVariedad().toLowerCase().contains(crit)) {
                res.add(c);
            }
        }
        return res;
    }
    public void reporteCultivos() {
        System.out.println("=== Estado de Cultivos ===");
        for (Estado e : Estado.values()) {
            long cnt = cultivos.stream()
                               .filter(c -> c.getEstado() == e)
                               .count();
            System.out.println(e + ": " + cnt);
        }
    }
}
