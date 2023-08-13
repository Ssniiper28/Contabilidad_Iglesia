/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.rapture.controlgastos;

import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.models.Inventario;
import com.rapture.controlgastos.models.PDF;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String nombreArchivo = Inventario.meses[LocalDate.now().getMonthValue() - 1] + "-" + LocalDate.now().getYear();
        System.out.println(nombreArchivo);
                
        ArrayList<Concepto> egresos = Inventario.cargarDatos(nombreArchivo, "egresos");
        ArrayList<Concepto> ofrendasGenerales = Inventario.cargarDatos(nombreArchivo, "ofrendas generales");
        ArrayList<Concepto> ofrendasEspeciales = Inventario.cargarDatos(nombreArchivo, "ofrendas especiales");
        ArrayList<Concepto> talentos = Inventario.cargarDatos(nombreArchivo, "talentos");
        ArrayList<Concepto> primicias = Inventario.cargarDatos(nombreArchivo, "primicias");
        ArrayList<Concepto> diezmos = Inventario.cargarDatos(nombreArchivo, "diezmos");
        ArrayList<Concepto> saldoAnterior = Inventario.cargarDatos(nombreArchivo, "saldo actual");
        
        double totalEgresos = Inventario.getTotal(egresos);
        double totalOfrendasGenerales = Inventario.getTotal(ofrendasGenerales);
        double totalOfrendasEspeciales = Inventario.getTotal(ofrendasEspeciales);
        double totalTalentos = Inventario.getTotal(talentos);
        double totalPrimicias = Inventario.getTotal(primicias);
        double totalDiezmos = Inventario.getTotal(diezmos);
        double totalAnterior = Inventario.getTotal(saldoAnterior);
        
        double totalIngresos = totalOfrendasGenerales + totalOfrendasEspeciales + totalTalentos + totalPrimicias + totalDiezmos;
        
        double ofrendasGeneralesPorcentaje = (totalOfrendasGenerales / totalIngresos) * 100;
        double ofrendasEspecialesPorcentaje = (totalOfrendasEspeciales / totalIngresos) * 100;
        double diezmosPorcentaje = (totalDiezmos / totalIngresos) * 100;
        double talentosPorcentaje = (totalTalentos / totalIngresos) * 100;
        double primiciasPorcentaje = (totalPrimicias / totalIngresos) * 100;
        
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Ofrendas Gen. " + String.format(Locale.US, "%.2f", ofrendasGeneralesPorcentaje) + "%", ofrendasGeneralesPorcentaje),
                new PieChart.Data("Ofrendas Esp. " + String.format(Locale.US, "%.2f", ofrendasEspecialesPorcentaje) + "%", ofrendasEspecialesPorcentaje),
                new PieChart.Data("Diezmos " + String.format(Locale.US, "%.2f", diezmosPorcentaje) + "%", diezmosPorcentaje),
                new PieChart.Data("Talentos " + String.format(Locale.US, "%.2f", talentosPorcentaje) + "%", talentosPorcentaje),
                new PieChart.Data("Primicias " + String.format(Locale.US, "%.2f", primiciasPorcentaje) + "%", primiciasPorcentaje)
        );
                
        pieChart.setData(data);
        
        PDF.darFormato(totalIngresos);
        
        totalIngresosLabel.setText(PDF.darFormato(totalIngresos));
        totalEgresosLabel.setText(PDF.darFormato(totalEgresos));
        saldoActualLabel.setText(PDF.darFormato(totalIngresos - totalEgresos + totalAnterior));
    }    
    
}
