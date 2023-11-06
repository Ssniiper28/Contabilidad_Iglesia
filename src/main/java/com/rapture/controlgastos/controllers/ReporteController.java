package com.rapture.controlgastos.controllers;

import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.models.SaldoPorMes;
import com.rapture.controlgastos.services.ConceptoRepository;
import com.rapture.controlgastos.services.Inventario;
import com.rapture.controlgastos.services.PDF;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author edgar
 */
public class ReporteController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private Label totalIngresosLabel;
    @FXML
    private Label totalEgresosLabel;
    @FXML
    private Label saldoActualLabel;
    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private Label saldoAnteriorLabel;
    @FXML
    private BarChart<?, ?> barChart;
    
    List meses = List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
    List dias = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
    int year;
    @FXML
    private Label ingresoMayorCantidad;
    @FXML
    private Label ingresoMayorDescripcion;
    @FXML
    private Label ingresoMayorFecha;
    @FXML
    private Label egresoMayorCantidad;
    @FXML
    private Label egresoMayorDescripcion;
    @FXML
    private Label egresoMayorFecha;
    @FXML
    private Label saldoMesLabel;
    
    private double totalIngresos, totalEgresos;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCombo.setItems(FXCollections.observableArrayList(meses));
        monthCombo.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, 2100, LocalDate.now().getYear()));
        year = yearSpinner.getValue();
        cargarDatos();
        cargarDatosTabla();
        cargarIngresoMayor();
        cargarDatosSaldoMes();
    }    

    private void cargarIngresoMayor() {
        Concepto ingresoMayor = ConceptoRepository.getMayorConcepto(getFecha(),false);
        Concepto egresoMayor = ConceptoRepository.getMayorConcepto(getFecha(), true);
        
        if (ingresoMayor != null){
            ingresoMayorCantidad.setText(PDF.darFormato(ingresoMayor.getCantidad()));
            ingresoMayorDescripcion.setText(ingresoMayor.getDescripcion());
            LocalDate fecha = ingresoMayor.getFecha();
            ingresoMayorFecha.setText(String.format("%s %d de %s", dias.get(fecha.getDayOfWeek().getValue() - 1),fecha.getDayOfMonth(), meses.get(fecha.getMonthValue() - 1)));
        } else {
            ingresoMayorCantidad.setText("$0.00");
            ingresoMayorDescripcion.setText("");
            ingresoMayorFecha.setText("");
        }
        
        if (egresoMayor != null) {
            egresoMayorCantidad.setText(PDF.darFormato(egresoMayor.getCantidad()));
            egresoMayorDescripcion.setText(egresoMayor.getDescripcion());
            LocalDate fecha = egresoMayor.getFecha();
            egresoMayorFecha.setText(String.format("%s %d %s", dias.get(fecha.getDayOfWeek().getValue()-1), fecha.getDayOfMonth(), meses.get(fecha.getMonthValue() - 1)));
        } else {
            egresoMayorCantidad.setText("$0.00");
            egresoMayorDescripcion.setText("");
            egresoMayorFecha.setText("");
        }
    }    

    @FXML
    private void generar(ActionEvent event) {
        PDF pdf = new PDF();
         try {
             pdf.createPDF(getFecha());
         } catch (Exception ex) {
             
         }
    }

    private LocalDate getFecha() {
        return LocalDate.of(yearSpinner.getValue(), monthCombo.getSelectionModel().getSelectedIndex() + 1, 1);
    }

    @FXML
    private void mostrar(ActionEvent event) {
        cargarDatos();
        cargarIngresoMayor();
        cargarDatosSaldoMes();
        
        if (year != yearSpinner.getValue()){
            cargarDatosTabla();
            year = yearSpinner.getValue();
        }
    }

    private void cargarDatos() {
        List<Concepto> egresos = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.EGRESOS);
        List<Concepto> ofrendasGenerales = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.OFRENDAS_GENERALES);
        List<Concepto> ofrendasEspeciales = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.OFRENDAS_ESPECIALES);
        List<Concepto> talentos = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.TALENTOS);
        List<Concepto> primicias = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.PRIMICIAS);
        List<Concepto> diezmos = ConceptoRepository.getConceptoByCategoria(getFecha(), Categoria.DIEZMOS);
                
        totalEgresos = Inventario.getTotal(egresos);
        double totalOfrendasGenerales = Inventario.getTotal(ofrendasGenerales);
        double totalOfrendasEspeciales = Inventario.getTotal(ofrendasEspeciales);
        double totalTalentos = Inventario.getTotal(talentos);
        double totalPrimicias = Inventario.getTotal(primicias);
        double totalDiezmos = Inventario.getTotal(diezmos);
        double totalAnterior = ConceptoRepository.getSaldoAnterior(getFecha());
        
        totalIngresos = totalOfrendasGenerales + totalOfrendasEspeciales + totalTalentos + totalPrimicias + totalDiezmos;
        double ofrendasGeneralesPorcentaje = (totalOfrendasGenerales / totalIngresos) * 100;
        double ofrendasEspecialesPorcentaje = (totalOfrendasEspeciales / totalIngresos) * 100;
        double diezmosPorcentaje = (totalDiezmos / totalIngresos) * 100;
        double talentosPorcentaje = (totalTalentos / totalIngresos) * 100;
        double primiciasPorcentaje = (totalPrimicias / totalIngresos) * 100;
        
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Ofrendas Gen. " + PDF.darFormato(totalOfrendasGenerales), ofrendasGeneralesPorcentaje),
                new PieChart.Data("Ofrendas Esp. " + PDF.darFormato(totalOfrendasEspeciales), ofrendasEspecialesPorcentaje),
                new PieChart.Data("Diezmos " + PDF.darFormato(totalDiezmos), diezmosPorcentaje),
                new PieChart.Data("Talentos " + PDF.darFormato(totalTalentos), talentosPorcentaje),
                new PieChart.Data("Primicias " + PDF.darFormato(totalPrimicias), primiciasPorcentaje)
        );
        pieChart.setData(data);
         
        PDF.darFormato(totalIngresos);
        
        totalIngresosLabel.setText(PDF.darFormato(totalIngresos));
        totalEgresosLabel.setText(PDF.darFormato(totalEgresos));
        saldoAnteriorLabel.setText(PDF.darFormato(totalAnterior));
        saldoActualLabel.setText(PDF.darFormato(totalIngresos - totalEgresos + totalAnterior));
    }

    private void cargarDatosTabla() {
        barChart.getData().clear();
        
        List<SaldoPorMes> datosPorMes = ConceptoRepository.getSaldoByMes(yearSpinner.getValue());
        
        XYChart.Series datosIngresos = new XYChart.Series();
        XYChart.Series datosEgresos = new XYChart.Series();
        datosIngresos.setName("Ingresos");
        datosEgresos.setName("Egresos");
        
        int id = 1;
        meses.forEach(mes -> {
            datosIngresos.getData().add(new XYChart.Data(mes, 0));
            datosEgresos.getData().add(new XYChart.Data(mes,0));
            datosPorMes.forEach(dato -> {
                if (meses.get(dato.getMes() - 1).equals(mes)){
                    datosIngresos.getData().add(new XYChart.Data(mes, dato.getIngresos()));
                    datosEgresos.getData().add(new XYChart.Data(mes, dato.getEgresos()));
                    return;
                }
            });
        });
        
        
        barChart.getData().addAll(datosIngresos, datosEgresos);
    }

    private void cargarDatosSaldoMes() {
        double saldoMes = totalIngresos - totalEgresos;
        saldoMesLabel.setText(PDF.darFormato(totalIngresos - totalEgresos));
        
        if (saldoMes < 0) {
            saldoMesLabel.setTextFill(Color.color(0.85,0.08,0.08));
        } else {
            saldoMesLabel.setTextFill(Color.color(0.288,0.776,0.076));
        }
    }
    
}
