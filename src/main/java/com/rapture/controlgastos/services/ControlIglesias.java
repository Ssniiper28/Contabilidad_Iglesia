package com.rapture.controlgastos.services;

import com.rapture.controlgastos.App;
import com.rapture.controlgastos.models.Iglesia;
import java.util.List;

public class ControlIglesias {
    
    // lista de iglesias en el programa
    public static List<Iglesia> usuarios = List.of();
    public static String url = "usuarios/";
    
    // Usuario activo en el programa
    public static Iglesia activo = IglesiaRepository.getIglesiaByActivo();
    
    public static void agregarIglesia(String iglesia, String registro, String municipio, String encargado, String tesorero, String estado){
        Iglesia usuario = new Iglesia(iglesia, registro, municipio, encargado, tesorero, estado);
        IglesiaRepository.guardar(usuario);
    }
        
    public static void setActivo(Iglesia iglesia){
        IglesiaRepository.setIglesiaActivo(iglesia);
        activo = iglesia;
        if (App.ui != null) {
            App.ui.setActivo(iglesia);
        }
    }
}
