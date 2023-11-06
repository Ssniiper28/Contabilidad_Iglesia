package com.rapture.controlgastos.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author edgar
 */
@Entity
@Table(name = "conceptos")
@Getter
@Setter
@NoArgsConstructor
public class Concepto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private LocalDate fecha;
    private String descripcion;
    private Double cantidad;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Iglesia iglesia;
    
    public Concepto(String descripcion, double total){
        this.descripcion = descripcion;
        this.cantidad = total;
    }
    
    public Concepto(LocalDate fecha, String descripcion, double cantidad,Categoria categoria){
        this.fecha = fecha;
        this.descripcion = descripcion.toUpperCase();
        this.cantidad = cantidad;
        this.categoria = categoria;
    }
    
    // Constructor que genera un Concepto a partir de un String en formato csv
    public Concepto(String csv){
        String[] datos = csv.split(",");
        String[] fecha = datos[0].split("-");
        this.fecha = LocalDate.of(Integer.parseInt(fecha[0]),Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
        this.descripcion = datos[1];
        this.cantidad = Double.valueOf(datos[2]);
    }
    
    // Convierte el Concepto en un String en formate csv
    public String toCSV() {
        return this.fecha.toString() + "," + this.descripcion + "," + this.cantidad;
    }
}
