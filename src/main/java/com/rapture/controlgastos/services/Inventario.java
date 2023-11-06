package com.rapture.controlgastos.services;

import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    public static String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
    public void add(Concepto c){
        ConceptoRepository.guardar(c);
    }
        
    public void eliminar(Concepto c){
        ConceptoRepository.eliminar(c);
    }
        
    public List<Concepto> cargarArchivo(Categoria categoria, LocalDate fecha){
        return ConceptoRepository.getConceptoByCategoria(fecha, categoria);
    }
    
    public static double getTotal(List<Concepto> lista){
        double sum = 0.0;
        for (Concepto c : lista){
            sum += c.getCantidad();
        }
        
        return sum;
    }

    public void editar(Concepto c) {
        ConceptoRepository.editar(c);
    }
}
