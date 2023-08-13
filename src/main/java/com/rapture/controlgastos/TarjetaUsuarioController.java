
package com.rapture.controlgastos;

import com.rapture.controlgastos.models.ControlIglesias;
import com.rapture.controlgastos.models.Iglesia;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class TarjetaUsuarioController implements Initializable {

    @FXML
    private Label iglesia;
    @FXML
    private Label encargado;
    @FXML
    private Label tesorero;
    @FXML
    private ImageView logo;
    
    private Iglesia objetoIglesia = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Circle c = new Circle(60);
        c.setTranslateX(60);
        c.setTranslateY(60);
        logo.setClip(c);
        
    }    

    @FXML
    private void setActivo(ActionEvent event) {
        ControlIglesias.setActivo(objetoIglesia);
    }

    public void setIglesia(Iglesia iglesia) {
        objetoIglesia = iglesia;
        this.iglesia.setText(iglesia.getLocalidad());
        encargado.setText(iglesia.getResponsable());
        tesorero.setText(iglesia.getTesorero());
    }
    
}
