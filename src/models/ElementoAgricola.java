package models;

import java.time.LocalDate;

public abstract class ElementoAgricola {
    private String nombre;
    private LocalDate fecha;

    public ElementoAgricola(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
