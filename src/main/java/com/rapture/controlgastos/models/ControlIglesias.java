package com.rapture.controlgastos.models;

import com.rapture.controlgastos.App;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlIglesias {
    
    // lista de iglesias en el programa
    public static ArrayList<Iglesia> usuarios = new ArrayList();
    public static String url = "usuarios/";
    
    // Usuario activo en el programa
    public static Iglesia activo = null;
    
    public static void agregarIglesia(String iglesia, String registro, String municipio, String encargado, String tesorero, String estado){
        Iglesia usuario = new Iglesia(iglesia, registro, municipio, encargado, tesorero, estado);
        usuarios.add(usuario);
    }
    
    // lee archivo usuarios.txt en la ruta ./usuarios/ y los carga en la lista de usuarios
    public void cargarUsuarios() throws IOException{
        FileReader fr;
        try {
            File f = new File(url + "usuarios.txt");
            fr = new FileReader(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
            
            // Leer el primer usuario
            String csv = br.readLine();
            
            // si no hay usuarios salir de la carga de usuarios
            if (csv == null){
                return;
            }
            
            // establecer el primer usuario como el usuario principal
            setActivo(new Iglesia(csv));
            
            // cargar lista de usuarios
            while ((csv = br.readLine()) != null) {
                Iglesia item = new Iglesia(csv);
                usuarios.add(item);
            }
            br.close();        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlIglesias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void guardarUsuarios(){
        // si no hay usuario activo retornar
        if (activo == null){
            return;
        }
        
        try {
            // crear el directorio ./usuarios/ si no existe
            Files.createDirectories(Paths.get(url));
            
            FileWriter file = new FileWriter(new File(url + "usuarios.txt").getAbsolutePath()); //"resources/" + nombre + ".txt"
            BufferedWriter bw = new BufferedWriter(file);
            
            // escribir el usuario activo como primer usuario en el archivo
            bw.write(activo.toCSV());
            bw.newLine();
            
            // guardar los demas usuarios en la lista
            for (Iglesia item : usuarios) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e){
            System.out.println("Error al guardar");
            System.out.println(e);
        }
    }
    
    public static void setActivo(Iglesia iglesia){
        // establecer el usuario principal solo si ya esta inicializada la interfaz
        activo = iglesia;
        if (App.ui != null) {
            App.ui.setActivo(iglesia);
        }
    }
}
