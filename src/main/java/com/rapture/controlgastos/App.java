package com.rapture.controlgastos;

import com.rapture.controlgastos.models.ControlIglesias;
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

    static void maximizar() {
       stage.setMaximized(!stage.maximizedProperty().get());
    }

    static void minimizar() {
        stage.setIconified(true);
    }

    static void cerrar() {
        stage.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        ControlIglesias ci = new ControlIglesias();
        ci.cargarUsuarios();
        scene = new Scene(loadFXML("UI"), 800, 850);
        ui.setActivo(ControlIglesias.activo);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        InventarioController.inv.guardarArchivo();
        ControlIglesias.guardarUsuarios();
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