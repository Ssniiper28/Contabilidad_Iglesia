
package com.rapture.controlgastos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="iglesias")
@Setter
@Getter
@NoArgsConstructor
public class Iglesia {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String localidad;
    @Column(name="registro")
    private String registroInterno;
    private String municipio;
    private String responsable;
    private String tesorero;
    private String estado;
    private boolean activo = false;
  

    public Iglesia(String localidad,String registroInterno, String municipio, String responsable, String tesorero, String estado) {
        this.localidad = localidad.toUpperCase();
        this.registroInterno = registroInterno;
        this.municipio = municipio.toUpperCase();
        this.responsable = responsable.toUpperCase();
        this.tesorero = tesorero.toUpperCase();
        this.estado = estado.toUpperCase();
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
