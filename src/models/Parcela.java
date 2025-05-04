package models;

import java.util.ArrayList;
import java.util.List;

public class Parcela {
    private String codigo;
    private List<Cultivo> cultivos;

    public Parcela(String codigo) {
        this.codigo = codigo;
        this.cultivos = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public List<Cultivo> getCultivos() { return cultivos; }
    public void addCultivo(Cultivo c) { cultivos.add(c); }
    public void removeCultivo(Cultivo c) { cultivos.remove(c); }

    @Override
    public String toString() {
        return "Parcela " + codigo + " - Cultivos: " + cultivos.size();
    }
}
