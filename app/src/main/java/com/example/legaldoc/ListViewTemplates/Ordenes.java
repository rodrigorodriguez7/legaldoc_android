package com.example.legaldoc.ListViewTemplates;

public class Ordenes {

    private String nombreServicio;
    private String descriptionServicio;
    private String estado;
    private String apellido;
    private String nombre;

    public Ordenes(String nS, String dS, String e,String a,String n){
        this.nombreServicio=nS;
        this.descriptionServicio=dS;
        this.estado=e;
        this.apellido=a;
        this.nombre=n;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescriptionServicio() {
        return descriptionServicio;
    }

    public String getEstado() {
        return estado;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }
}
