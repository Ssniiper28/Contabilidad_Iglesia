package com.rapture.controlgastos;

import com.rapture.controlgastos.models.PDF;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class GenerarController implements Initializable {

    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private Spinner<Integer> yearSpinner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCombo.setItems(FXCollections.observableArrayList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
        monthCombo.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, LocalDate.now().getYear()));
    }    

    @FXML
    private void generar(ActionEvent event) throws IOException {
        String nombreArchivo = monthCombo.getValue() + "-" + yearSpinner.getValue();
        InventarioController.nombreArchivo = nombreArchivo;
        InventarioController.mes = monthCombo.getValue();
        InventarioController.anio = yearSpinner.getValue();
//        InventarioController.inv.limpiar();
//        InventarioController.inv.cargarArchivo();
        PDF pdf = new PDF();
         try {
             pdf.createPDF();
         } catch (Exception ex) {
             
         }
    }
    
}
