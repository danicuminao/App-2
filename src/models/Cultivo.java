package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cultivo {
    private String nombre;
    private String variedad;
    private double superficie;
    private Parcela parcela;
    private LocalDate fechaSiembra;
    private Estado estado;
    private List<Actividad> actividades;

    public Cultivo(String nombre,
                   String variedad,
                   double superficie,
                   Parcela parcela,
                   LocalDate fechaSiembra,
                   Estado estado) {
        this.nombre = nombre;
        this.variedad = variedad;
        this.superficie = superficie;
        this.parcela = parcela;
        this.fechaSiembra = fechaSiembra;
        this.estado = estado;
        this.actividades = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getVariedad() { return variedad; }
    public void setVariedad(String variedad) { this.variedad = variedad; }
    public double getSuperficie() { return superficie; }
    public void setSuperficie(double superficie) { this.superficie = superficie; }
    public Parcela getParcela() { return parcela; }
    public void setParcela(Parcela parcela) { this.parcela = parcela; }
    public LocalDate getFechaSiembra() { return fechaSiembra; }
    public void setFechaSiembra(LocalDate fechaSiembra) { this.fechaSiembra = fechaSiembra; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public List<Actividad> getActividades() { return actividades; }
    public void addActividad(Actividad a) { actividades.add(a); }
    public void removeActividad(Actividad a) { actividades.remove(a); }

    @Override
    public String toString() {
        return nombre + " - " + variedad +
               " (" + superficie + " ha) - " + estado +
               " - Siembra: " + fechaSiembra +
               " - Parcela: " + parcela.getCodigo();
    }
}

