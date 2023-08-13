package com.rapture.controlgastos.models;

import java.time.LocalDate;

/**
 *
 * @author edgar
 */
public class Concepto {
    LocalDate fecha;
    String descripcion;
    Double cantidad;
    
    public Concepto(LocalDate fecha, String descripcion, double cantidad){
        this.fecha = fecha;
        this.descripcion = descripcion.toUpperCase();
        this.cantidad = cantidad;
    }
    
    // Constructor que genera un Concepto a partir de un String en formato csv
    public Concepto(String csv){
        String[] datos = csv.split(",");
        String[] fecha = datos[0].split("-");
        this.fecha = LocalDate.of(Integer.parseInt(fecha[0]),Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
        this.descripcion = datos[1];
        this.cantidad = Double.valueOf(datos[2]);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    
    // Convierte el Concepto en un String en formate csv
    public String toCSV() {
        return this.fecha.toString() + "," + this.descripcion + "," + this.cantidad;
    }
}
