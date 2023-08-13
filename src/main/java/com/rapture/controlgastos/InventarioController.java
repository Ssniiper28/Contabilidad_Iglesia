/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.rapture.controlgastos;

import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.models.Inventario;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author edgar
 */
public class InventarioController implements Initializable {

    @FXML
    private TableView<Concepto> tablaConceptos;
    @FXML
    private TableColumn<Concepto, LocalDate> fechaCol;
    @FXML
    private TableColumn<Concepto, String> conceptoCol;
    @FXML
    private TableColumn<Concepto, Double> cantidadCol;
    @FXML
    private TextField conceptoInput;
    @FXML
    private TextField cantidadInput;
    @FXML
    private Button agregarBtn;
    @FXML
    private VBox container;
    @FXML
    private Label label;
    @FXML
    private Label label11;
    @FXML
    private Spinner<Integer> day;
    @FXML
    private Label label1;
    @FXML
    private ComboBox<String> month;
    @FXML
    private Spinner<Integer> year;    
    
    private boolean editando;
    private Concepto seleccionado;
    public static Inventario inv = new Inventario();
    public static String mes = "";
    public static int anio;
    public static String nombreArchivo = "db";
    
    public static String categoria = "Egresos";
    @FXML
    private Label tituloLabel;
    @FXML
    private Label mesLabel;
    @FXML
    private Label anioLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloLabel.setText(categoria);
        editando = false;
        setSpinners();
        setColumnsValueFactory();
        mes = month.getValue(); // necesario inizializar para otras funciones
        anio = year.getValue();
        mesLabel.setText(mes);
        anioLabel.setText(anio + "");
        nombreArchivo = mes + "-" + anio;
        inv.cargarArchivo();
        tablaConceptos.getItems().addAll(inv.getConceptos());
    }
    
    @FXML
    private void agregar(ActionEvent event) {
        if (editando){
            int id = tablaConceptos.getItems().indexOf(seleccionado);
            inv.eliminar(seleccionado);
            tablaConceptos.getItems().remove(seleccionado);
            seleccionado.setFecha(getLocalDate());
            seleccionado.setDescripcion(conceptoInput.getText());
            seleccionado.setCantidad(Double.valueOf(cantidadInput.getText()));
            inv.add(seleccionado);
            tablaConceptos.getItems().add(id, seleccionado);
            tablaConceptos.refresh();
            editando = false;
            agregarBtn.setText("Agregar");
            tablaConceptos.getSelectionModel().clearSelection();
        } else {
            try{
                LocalDate fecha = getLocalDate();
                String concepto = conceptoInput.getText();
                double cantidad = Double.valueOf(cantidadInput.getText());
                Concepto c = new Concepto(fecha, concepto, cantidad);
                inv.add(c);
                tablaConceptos.getItems().add(c);
            } catch (NumberFormatException e){
                System.out.println("faltan campos");
            } 
        }  
        limpiar();
    }
    
    private void llenarTabla(){
        for (Concepto c : inv.getConceptos()){
            tablaConceptos.getItems().add(c);
        }
    }
    
    private void limpiar(){
        conceptoInput.setText("");
        cantidadInput.setText("");
    }

    @FXML
    private void editar(ActionEvent event) {
        seleccionado = tablaConceptos.getSelectionModel().getSelectedItem();
        day.getValueFactory().setValue(seleccionado.getFecha().getDayOfMonth());
        conceptoInput.setText(seleccionado.getDescripcion());
        cantidadInput.setText(seleccionado.getCantidad() + "");
        agregarBtn.setText("Guardar");
        editando = true;
    }

    @FXML
    private void eliminar(ActionEvent event) {
        inv.eliminar(tablaConceptos.getSelectionModel().getSelectedItem());
        tablaConceptos.getItems().remove(tablaConceptos.getSelectionModel().getSelectedItem());
        tablaConceptos.refresh();
    }
    
    private void setSpinners(){
        month.setItems(FXCollections.observableArrayList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
        int monthId = LocalDate.now().getMonthValue() - 1;
        month.getSelectionModel().select(monthId);
        boolean leapYear = LocalDate.now().isLeapYear();
        day.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Month.of(monthId + 1).length(leapYear), LocalDate.now().getDayOfMonth()));
        year.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, LocalDate.now().getYear()));
    }
    
    private LocalDate getLocalDate(){
        return LocalDate.of(year.getValue(), month.getSelectionModel().getSelectedIndex() + 1, day.getValue());
    }
    
    private void clear(){
        conceptoInput.setText("");
        cantidadInput.setText("");
    }
    
    private void setColumnsValueFactory(){
        fechaCol.setCellValueFactory(new PropertyValueFactory("fecha"));
        conceptoCol.setCellValueFactory(new PropertyValueFactory("descripcion"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory("cantidad"));
        
        cantidadCol.setCellFactory(tc -> new TableCell<Concepto, Double>(){
           @Override
           protected void updateItem(Double t, boolean bln) {
               super.updateItem(t, bln);
               if (bln) {
                   setText(null);
               } else {
                   
                   setText(String.format(Locale.US,"$ %,.2f",t));
               }
           }
           
        });
    }

    @FXML
    private void cargarArchivo(ActionEvent event) {
        day.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Month.of(month.getSelectionModel().getSelectedIndex() + 1).length(LocalDate.now().isLeapYear()), LocalDate.now().getDayOfMonth()));
        nombreArchivo = mes + "-" + anio;
        inv.guardarArchivo();
        inv.limpiar();
        nombreArchivo = month.getValue() + "-" + year.getValue();
        inv.cargarArchivo();
        tablaConceptos.getItems().clear();
        tablaConceptos.getItems().addAll(inv.getConceptos());
        tablaConceptos.refresh();
        mesLabel.setText(month.getValue());
        anioLabel.setText(year.getValue() + "");
        mes = month.getValue();
        anio = year.getValue();
    }
}
