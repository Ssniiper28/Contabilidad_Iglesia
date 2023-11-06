/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.rapture.controlgastos.controllers;

import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.services.Inventario;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
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
import javafx.scene.control.TableRow;
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
    List dias = List.of("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO");
    
    public static Categoria categoria = Categoria.EGRESOS;
    @FXML
    private Label tituloLabel;
    @FXML
    private Label mesLabel;
    @FXML
    private Label anioLabel;
    @FXML
    private VBox conceptoVbox;
    @FXML
    private Label conceptoLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloLabel.setText(categoria.toString());
        editando = false;
        setSpinners();
        setColumnsValueFactory();
        mes = month.getValue(); // necesario inicialializar para otras funciones
        anio = year.getValue();
        mesLabel.setText(mes);
        anioLabel.setText(anio + "");
        cargarInventario();
        
        if (categoria == Categoria.OFRENDAS_GENERALES){
            conceptoVbox.setManaged(false);
            conceptoVbox.setVisible(false);
        }
        

        // formato para el ultimo concepto de la tabla
        PseudoClass lastRow = PseudoClass.getPseudoClass("last-row");
        
        tablaConceptos.setRowFactory(tv -> new TableRow<Concepto>(){
            @Override
            public void updateIndex(int index){
                super.updateIndex(index);
                pseudoClassStateChanged(lastRow, index >= 0 && index == tablaConceptos.getItems().size() - 1);
            }
        });
        
    }

    private void cargarInventario() {
        List<Concepto> conceptos = inv.cargarArchivo(categoria, getLocalDate());
        double total = Inventario.getTotal(conceptos);
        tablaConceptos.getItems().clear();
        tablaConceptos.getItems().addAll(conceptos);
        
        if (conceptos.size() == 0 ){
            return;
        }
        tablaConceptos.getItems().add(new Concepto("TOTAL", total));
    }
    
    @FXML
    private void agregar(ActionEvent event) {
        
        if (editando){
            LocalDate fecha = getLocalDate();
            try{
                seleccionado.setFecha(fecha);
                if (categoria != Categoria.OFRENDAS_GENERALES) {
                    seleccionado.setDescripcion(conceptoInput.getText().toUpperCase());
                } else {
                    seleccionado.setDescripcion(String.format("OFRENDA GENERAL %s", dias.get(fecha.getDayOfWeek().getValue() - 1)));
                }
                seleccionado.setCantidad(Double.valueOf(cantidadInput.getText()));
                inv.editar(seleccionado);
                cargarInventario();
                editando = false;
                agregarBtn.setText("Agregar");
            } catch (Exception e){
                editando = false;
                agregarBtn.setText("Agregar");
            }
            
        } else {
            try{
                LocalDate fecha = getLocalDate();
                String concepto = conceptoInput.getText();
                if (categoria == Categoria.OFRENDAS_GENERALES){
                    concepto = String.format("OFRENDA GENERAL %s", dias.get(fecha.getDayOfWeek().getValue() - 1));
                }
                double cantidad = Double.valueOf(cantidadInput.getText());
                Concepto c = new Concepto(fecha, concepto, cantidad, categoria);
                inv.add(c);
                cargarInventario();
            } catch (NumberFormatException e){
                System.out.println("faltan campos");
            } 
        }  
        limpiar();
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
        cargarInventario();
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
        tablaConceptos.getItems().clear();
        cargarInventario();
        tablaConceptos.refresh();
        mesLabel.setText(month.getValue());
        anioLabel.setText(year.getValue() + "");
    }
}
