package com.rapture.controlgastos.models;

import com.rapture.controlgastos.InventarioController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Inventario {
    private ArrayList<Concepto> listaConceptos;
    public static String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    
    public Inventario(){
        listaConceptos = new ArrayList();
    }
    
    public void add(Concepto c){
        listaConceptos.add(c);
    }
    
    public ArrayList<Concepto> getConceptos(){
        return listaConceptos;
    }
    
    public void eliminar(Concepto c){
        listaConceptos.remove(c);
    }
    
    public void limpiar(){
        listaConceptos.clear();
    }
    
    public void cargarArchivo(){
        File f = null;
        
        // buscar los datos dependiendo del usuario y la interfaz que se encuentren activos
        String url = "datos/"+ControlIglesias.activo.getLocalidad()+"/"+InventarioController.categoria.toLowerCase()+"/";
        try {
            f = new File(new File(url + InventarioController.nombreArchivo + ".txt").getAbsolutePath());
            FileReader file = new FileReader(f);
            BufferedReader bf = new BufferedReader(file);
            String csv;
            while ((csv = bf.readLine()) != null) {
                Concepto item = new Concepto(csv);
                listaConceptos.add(item);
            }
            bf.close();
        } catch (IOException e){
            System.out.println("Error al cargar");
            System.out.println(e);
        }
    }
    
    public static ArrayList<Concepto> cargarDatos(String nombreArchivo, String categoria){
        
        if (ControlIglesias.activo == null){
            return new ArrayList<Concepto>();
        }
        
        File f = null;
        String url = url = "datos/"+ControlIglesias.activo.getLocalidad()+"/" + categoria + "/";
        ArrayList<Concepto> datos = new ArrayList();
        try {
            f = new File(new File(url + nombreArchivo + ".txt").getAbsolutePath());
            FileReader file = new FileReader(f.getAbsolutePath());
            BufferedReader bf = new BufferedReader(file);
            String csv;
            while ((csv = bf.readLine()) != null) {
                Concepto item = new Concepto(csv);
                datos.add(item);
            }
            bf.close();
        } catch (IOException e){
            System.out.println("Error al cargar");
            System.out.println(e);
        }
        
        return datos;
    }
    
    public void guardarArchivo(){
        
        // si no se encuentra seleccionado un usuario regresar
        if (ControlIglesias.activo == null) {
            return;
        }
        
        String categoria = InventarioController.categoria.toLowerCase();
        
        String url = "datos/"+ControlIglesias.activo.getLocalidad() + "/" + categoria +"/";
        
        //        if (listaConceptos.size() == 0){
        //            return;
        //        }
        
        try {
            Files.createDirectories(Paths.get(url));
            FileWriter file = new FileWriter(new File(url + InventarioController.nombreArchivo + ".txt").getAbsolutePath()); //"resources/" + nombre + ".txt"
            BufferedWriter bw = new BufferedWriter(file);
            for (Concepto item : listaConceptos) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e){
            System.out.println("Error al guardar");
            System.out.println(e);
        }
    }
    
    public Double getSuma(){
        double sum = 0.0;
        for (Concepto concepto : listaConceptos){
            sum += concepto.getCantidad();
        }
        return sum;
    }
    
    public static Double getMesAnterior(){
        String mesAnterior = "enero";
        
        // Loop de todos los meses
        for (int i = 0; i < meses.length; i++){
            
            // si el mes del loop equivale al mes del inventario y si 
            if (meses[i].equals(InventarioController.mes)){
                
                // es enero el mes anterior es diciembre
                if (i == 0){
                    mesAnterior = meses[11];
                    break;
                }
                
                // si no es enero el mes anterior es el del loop pasado
                mesAnterior = meses[i - 1];
                break;
            }
        }
        
        int anio = InventarioController.anio;
        
        // si el mes anterior es diciembre regresar un anio
        if (mesAnterior.equals("Diciembre")){
            anio--;
        }
        String nombreArchivo = mesAnterior + "-" + anio;
        ArrayList<Concepto> cajaMesAnterior = cargarDatos(nombreArchivo, "saldo actual");

        return getTotal(cajaMesAnterior);
    }
    
    public static double getTotal(ArrayList<Concepto> lista){
        double sum = 0.0;
        for (Concepto c : lista){
            sum += c.getCantidad();
        }
        
        return sum;
    }
}
