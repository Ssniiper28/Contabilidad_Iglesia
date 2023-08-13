
package com.rapture.controlgastos.models;

public class Iglesia {
    String localidad;
    String registroInterno;
    String municipio;
    String responsable;
    String tesorero;
    String estado;
    
    // Genera un objecto Iglesia a partir de los datos en formato csv
    public Iglesia(String csv){
        String[] datos = csv.split(",");
        this.localidad = datos[0];
        this.registroInterno = datos[1];
        this.municipio = datos[2];
        this.responsable = datos[3];
        this.tesorero = datos[4];
        this.estado = datos[5];
    }

    public Iglesia(String localidad,String registroInterno, String municipio, String responsable, String tesorero, String estado) {
        this.localidad = localidad.toUpperCase();
        this.registroInterno = registroInterno;
        this.municipio = municipio.toUpperCase();
        this.responsable = responsable.toUpperCase();
        this.tesorero = tesorero.toUpperCase();
        this.estado = estado.toUpperCase();
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTesorero() {
        return tesorero;
    }

    public void setTesorero(String tesorero) {
        this.tesorero = tesorero;
    }

    public String getRegistroInterno() {
        return registroInterno;
    }

    public void setRegistroInterno(String registroInterno) {
        this.registroInterno = registroInterno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    public String toCSV(){
        return this.localidad
                +","+this.registroInterno
                +","+this.municipio
                +","+this.responsable
                +","+this.tesorero
                +","+this.estado;
    }
    
}
