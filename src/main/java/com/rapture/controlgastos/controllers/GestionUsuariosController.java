package com.rapture.controlgastos.controllers;

import com.rapture.controlgastos.App;
import com.rapture.controlgastos.services.ControlIglesias;
import com.rapture.controlgastos.models.Iglesia;
import com.rapture.controlgastos.services.IglesiaRepository;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class GestionUsuariosController implements Initializable {

    @FXML
    private FlowPane gridContainer;
    @FXML
    private TextField nombre;
    @FXML
    private TextField encargado;
    @FXML
    private TextField tesorero;
    @FXML
    private TextField municipio;
    @FXML
    private TextField registro;
    
    private boolean editando = false;
    
    private Iglesia iglesia;
    @FXML
    private Button agregarBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarios();
    } 
    
    private void cargarUsuarios(){
        gridContainer.getChildren().clear();
        HBox usuario = new HBox();
        List<Iglesia> usuarios = IglesiaRepository.getIglesias();
        for (Iglesia iglesia : usuarios){
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("TarjetaUsuario.fxml"));
                usuario = loader.load();
                TarjetaUsuarioController controller = loader.getController();
                controller.setIglesia(iglesia);
                gridContainer.getChildren().add(usuario);
            } catch (Exception ex) {
                System.out.println("error de carga");
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        if (editando){
            iglesia.setLocalidad(nombre.getText());
            iglesia.setRegistroInterno(registro.getText());
            iglesia.setMunicipio(municipio.getText());
            iglesia.setResponsable(encargado.getText());
            iglesia.setTesorero(tesorero.getText());
            IglesiaRepository.editar(iglesia);
            editando = false;
            limpiarCampos();
            cargarUsuarios();
            agregarBtn.setText("Agregar Iglesia");
            App.ui.setActivo(IglesiaRepository.getIglesiaByActivo());
        } else {
           ControlIglesias.agregarIglesia(nombre.getText(), registro.getText(), municipio.getText(), encargado.getText(), tesorero.getText(),"Chiapas");
           cargarUsuarios();
           limpiarCampos(); 
        }   
    }
    
    public void editar(Iglesia iglesia){
        nombre.setText(iglesia.getLocalidad());
        registro.setText(iglesia.getRegistroInterno());
        encargado.setText(iglesia.getResponsable());
        tesorero.setText(iglesia.getTesorero());
        municipio.setText(iglesia.getMunicipio());
        this.iglesia = iglesia;
        editando = true;
        agregarBtn.setText("Guardar");
    }
    
    private void limpiarCampos(){
        nombre.setText("");
        registro.setText("");
        encargado.setText("");
        tesorero.setText("");
        municipio.setText("");
    }

    void eliminar(Iglesia objetoIglesia) {
        IglesiaRepository.eliminar(objetoIglesia);
        cargarUsuarios();        ControlIglesias.setActivo(IglesiaRepository.getIglesiaByActivo());
    }
}
