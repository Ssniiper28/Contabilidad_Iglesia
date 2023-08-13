package com.rapture.controlgastos;

import com.rapture.controlgastos.models.ControlIglesias;
import com.rapture.controlgastos.models.Iglesia;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUsuarios();
    } 
    
    private void cargarUsuarios(){
        HBox usuario = new HBox();
        for (Iglesia iglesia : ControlIglesias.usuarios){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TarjetaUsuario.fxml"));
                usuario = loader.load();
                TarjetaUsuarioController controller = loader.getController();
                controller.setIglesia(iglesia);
                gridContainer.getChildren().add(usuario);
            } catch (IOException ex) {}
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        ControlIglesias.usuarios.add(new Iglesia(nombre.getText(), registro.getText(), municipio.getText(), encargado.getText(), tesorero.getText(),"Chiapas"));
        cargarUsuarios();
        limpiarCampos();
    }
    
    private void limpiarCampos(){
        nombre.setText("");
        encargado.setText("");
        tesorero.setText("");
        municipio.setText("");
    }
}
