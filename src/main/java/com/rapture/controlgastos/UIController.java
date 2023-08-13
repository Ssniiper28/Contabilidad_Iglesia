package com.rapture.controlgastos;

import com.rapture.controlgastos.models.Iglesia;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class UIController implements Initializable {
    
    private VBox ventana;
    Timeline abrir, close;
    
    @FXML
    private VBox menu;
    @FXML
    private HBox menuBar;
    @FXML
    private AnchorPane container;
    @FXML
    private Label iglesiaLabel;
    @FXML
    private Label encargadoLabel;
    @FXML
    private Label tesoreroLabel;
    
    Double x, y;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // animaciones para abrir y cerrar menu
       menu.setPrefWidth(0);
       
       menu.opacityProperty().bind(menu.prefWidthProperty());
       abrir = new Timeline();
       close = new Timeline();
       KeyValue kv1 = new KeyValue(menu.prefWidthProperty(), 0);
       KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
       KeyValue kv2 = new KeyValue(menu.prefWidthProperty(), 250);
       KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
       KeyFrame kf3 = new KeyFrame(Duration.millis(200), kv1);
       KeyFrame kf4 = new KeyFrame(Duration.millis(0), kv2);
       abrir.getKeyFrames().add(kf1);
       abrir.getKeyFrames().add(kf2);
       close.getKeyFrames().add(kf4);
       close.getKeyFrames().add(kf3);
      
        try {
            cambiarVista("Reporte");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }    

    @FXML
    private void opciones(ActionEvent event) {
       if (menu.prefWidthProperty().get() == 0.0){
           abrir.play();
           InventarioController.inv.guardarArchivo();
           InventarioController.inv.limpiar();    
       } else {
           close.play();
       }
    } 
    
    public void cambiarVista(String fxml) throws IOException{
        ventana = new FXMLLoader(App.class.getResource(fxml + ".fxml")).load();
        container.setTopAnchor(ventana, 0.0);
        container.setLeftAnchor(ventana, 0.0);
        container.setRightAnchor(ventana, 0.0);
        container.setBottomAnchor(ventana, 0.0);
        container.getChildren().clear();
        container.getChildren().add(ventana);
    }

    @FXML
    private void setIngresos(ActionEvent e) throws IOException{
        InventarioController.categoria = "Ofrendas Generales";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    }
    
    @FXML
    private void setEgresos(ActionEvent e) throws IOException{
        InventarioController.categoria = "Egresos";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    } 

    @FXML
    private void generarReporte(ActionEvent event) throws IOException {
        cambiarVista("Generar");
        close.play();
    }

    @FXML
    private void setPerfil(ActionEvent event) throws IOException {
        cambiarVista("GestionUsuarios");
        close.play();
    }

    @FXML
    private void cerrarOpciones(MouseEvent event) {
        if (menu.prefWidthProperty().get() > 0){
            close.play();
        }
    }
    
    public void setActivo(Iglesia iglesia){
        if (iglesia != null){
            iglesiaLabel.setText(iglesia.getLocalidad());
            encargadoLabel.setText(iglesia.getResponsable());
            tesoreroLabel.setText(iglesia.getTesorero());
        }
    }

    @FXML
    private void maximizar(ActionEvent event) {
        App.maximizar();
    }

    @FXML
    private void minimizar(ActionEvent event) {
        App.minimizar();
    }

    @FXML
    private void cerrar(ActionEvent event) {
        App.cerrar();
    }

    @FXML
    private void moveWindow(MouseEvent event) {
        App.stage.setX(event.getScreenX()+ x);
        App.stage.setY(event.getScreenY() + y);
        
    }

    @FXML
    private void setCoord(MouseEvent event) {
        x = App.stage.getX() - event.getScreenX();
        y = App.stage.getY() - event.getScreenY();
    }

    @FXML
    private void setOfrendasEspeciales(ActionEvent event) throws IOException {
        InventarioController.categoria = "Ofrendas Especiales";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    }

    @FXML
    private void setDiezmos(ActionEvent event) throws IOException {
        InventarioController.categoria = "Diezmos";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    }

    @FXML
    private void setTalentos(ActionEvent event) throws IOException {
        InventarioController.categoria = "Talentos";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    }

    @FXML
    private void setPrimicias(ActionEvent event) throws IOException {
        InventarioController.categoria = "Primicias";
        InventarioController.inv.limpiar();
        cambiarVista("Inventario");
        close.play();
    }

    @FXML
    private void setReporte(ActionEvent event) throws IOException {
        cambiarVista("Reporte");
        close.play();
    }
}
