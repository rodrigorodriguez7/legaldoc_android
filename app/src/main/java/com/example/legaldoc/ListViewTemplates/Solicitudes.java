package com.example.legaldoc.ListViewTemplates;

public class Solicitudes {

    private String montoOrden;
    private String fechaCreacionOrden;
    private String totalServicios;
    private String id;

    public Solicitudes(String i, String tS, String mO, String fCO){
        this.id=i;
        this.totalServicios=tS;
        this.montoOrden=mO;
        this.fechaCreacionOrden=fCO;
    }

    public String getId() {
        return id;
    }

    public String getMontoOrden() {
        return montoOrden;
    }

    public String getFechaCreacionOrden() {
        return fechaCreacionOrden;
    }

    public String getTotalServiciosn() {
        return totalServicios;
    }

}
