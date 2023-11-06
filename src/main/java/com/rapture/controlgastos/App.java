package com.rapture.controlgastos;

import com.rapture.controlgastos.controllers.UIController;
import com.rapture.controlgastos.services.ControlIglesias;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static UIController ui = null;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        ControlIglesias ci = new ControlIglesias();
        scene = new Scene(loadFXML("UI"), 1920, 1080);
        ui.setActivo(ControlIglesias.activo);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);
        stage.show();    
    }
    
    
    @Override
    public void stop() throws Exception {
        // InventarioController.inv.guardarArchivo();
    }
    
    public static void maximizar() {
       stage.setMaximized(!stage.maximizedProperty().get());
       stage.setResizable(stage.maximizedProperty().get());
    }

    public static void minimizar() {
        stage.setIconified(true);
    }

    public static void cerrar() {
        stage.close();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent ventana = fxmlLoader.load();
        ui = fxmlLoader.getController();
        return ventana;
    }

    public static void main(String[] args) {
        launch();
    }

}