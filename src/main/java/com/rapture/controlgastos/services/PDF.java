package com.rapture.controlgastos.services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.rapture.controlgastos.controllers.InventarioController;
import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class PDF {
    PdfWriter writer = null;
    PdfDocument pdfDoc = null;
    Document document = null;
    LocalDate fecha;
    
    public double saldoMesAnterior;
    public double ofrendasGenerales;
    public double ofrendasEspeciales;
    public double talentos;
    public double primicias;
    public double diezmos;
    public double sumaIngresos;
    public double egresosMes;
    
    public void createPDF(LocalDate fecha) throws IOException{           
        this.fecha = fecha;
        saldoMesAnterior = ConceptoRepository.getSaldoAnterior(fecha);
        ofrendasGenerales = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.OFRENDAS_GENERALES));
        ofrendasEspeciales = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.OFRENDAS_ESPECIALES));
        talentos = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.TALENTOS));
        primicias = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.PRIMICIAS));
        diezmos = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.DIEZMOS));
        sumaIngresos = saldoMesAnterior + ofrendasGenerales + ofrendasEspeciales + talentos + primicias + diezmos;    
        egresosMes = Inventario.getTotal(ConceptoRepository.getConceptoByCategoria(fecha, Categoria.EGRESOS));
        
        try {
            writer = new PdfWriter("reporte.pdf");
        } catch (FileNotFoundException ex) {
            
        }
        pdfDoc = new PdfDocument(writer);  
        pdfDoc.addNewPage(PageSize.LETTER);
        document = new Document(pdfDoc, PageSize.LETTER);
        
        // Primer hoja
        addHeader();
        addResumen();
        addPie();
        document.add(new Paragraph());
        document.add(new Paragraph());
        document.add(new Paragraph());
        document.add(new Paragraph("\"Ahora bien, se requiere de los administradores, que cada uno se hallado fiel\" - 1 Corintios 4 : 2").setItalic().setBold().setFontSize(8f).setTextAlignment(TextAlignment.CENTER));
        document.add(new AreaBreak());
        
        // Segunda hoja
        addHeader();
        addData(1);
        document.add(new AreaBreak());
        
        // Tercer Hoja
        addHeader();
        addData(2);
        document.add(new AreaBreak());
        
        // Cuarta Hoja
        addHeader();
        resumenMensual();
        addPie();
        document.close();
        Desktop.getDesktop().open(new File("reporte.pdf"));
        
    }
    
    public void addHeader(){
        // Create table for Header
        float[] pointColumnWidths = {125f, 500f};
        
        // load logo
        ImageData logoData = ImageDataFactory.create(getClass().getResource("/static/logo.png"));
        Image logo = new Image(logoData);
        
        // create Table
        Table table = new Table(pointColumnWidths);
        
        // Logo
        table.addCell(new Cell(5,1).add(logo.setWidth(75)).setVerticalAlignment(VerticalAlignment.MIDDLE).setRelativePosition(15, 0, 0, 0).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Iglesia Cristiana \"Solo Cristo Salva\" A.R.").setBold()
                        .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setFontSize(18));
        
        // Nombre de la Empresa
        table.addCell(new Cell().add("Registro en Gubernacion: SGAR 223/93").setBold().setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        
        // rfc
        table.addCell(new Cell().add("R.F.C ICS 930811U1A").setBold().setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        
        // Direccion
        table.addCell(new Cell().add("3a. Oriente Sur No. 1816, Tuxtla Gutierrez, Chiapas. C.P. 29000").setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        
        // Titulo
        table.addCell(new Cell().add("DEPARTAMENTO DE CONTABILIDAD").setFontSize(15).setBold()
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        
        table.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1f));
        // Informacion
        
        float[] infoColumnWidth = {95, 100, 100, 75, 75, 100, 125, 50};
        Table info = new Table(infoColumnWidth);
        // 1er fila info
        info.addCell(new Cell(1, 3).add("Iglesia Cristiana \"Solo Cristo Salva\" en:").setBorder(Border.NO_BORDER).setPadding(0));
        info.addCell(new Cell(1, 3).add(ControlIglesias.activo.getLocalidad()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)).setPadding(0));
        info.addCell(new Cell().add("Registro").setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(0.5f)).setBorderLeft(new SolidBorder(0.5f)).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBold());
        info.addCell(new Cell(2, 1).add(ControlIglesias.activo.getRegistroInterno()).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorderLeft(Border.NO_BORDER).setPadding(0).setBold());
        
        // 2da Fila Info
        info.addCell(new Cell().add("Municipio de:").setBorder(Border.NO_BORDER).setPadding(0));
        info.addCell(new Cell(1,5).add(ControlIglesias.activo.getMunicipio() + ", CHIAPAS").setBorder(Border.NO_BORDER).setPadding(0).setBorderBottom(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
        info.addCell(new Cell().add("Interno No.").setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBold());
        
        // 3era fila info
        info.addCell(new Cell(1, 2).add("Correspondiente al mes de").setBorder(Border.NO_BORDER).setPadding(0));
        info.addCell(new Cell(1, 4).add(InventarioController.mes.toUpperCase()).setBorder(Border.NO_BORDER).setPadding(0).setBorderBottom(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
        info.addCell(new Cell().add("del Año").setBorder(Border.NO_BORDER).setPadding(0).setTextAlignment(TextAlignment.CENTER));
        info.addCell(new Cell().add(InventarioController.anio + "").setBorder(Border.NO_BORDER).setPadding(0));
        info.setFontSize(12);
        
        // Agregar a la tabla
        table.addCell(new Cell(1,3).add(info).setBorder(Border.NO_BORDER));
        
        table.setBorder(new SolidBorder(0.5f));
        document.add(table);    
    }
    
    public void addData(int x){
        String titulo;
        String codigo;
        String suma;
        Categoria categoria;
        if (x == 1){
            categoria = Categoria.EGRESOS;
            titulo = "FORMATO ANALITICO DE EGRESOS";
            codigo = "C02-FAE";
            suma = "EGRESOS";
        } else {
            categoria = Categoria.OFRENDAS_GENERALES;
            titulo = "FORMATO ANALITICO DE INGRESOS";
            codigo = "C01-FAI";
            suma = "INGRESOS";
        }
        
        float[] columsWidth = {125, 400, 125};
        Table tablaDatos = new Table(columsWidth);
        tablaDatos.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1f));
        
        tablaDatos.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(codigo).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell(1,3).add(titulo).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
        tablaDatos.addCell(new Cell().add("FECHA").setBold().setTextAlignment(TextAlignment.CENTER));
        tablaDatos.addCell(new Cell().add("CONCEPTO").setBold().setTextAlignment(TextAlignment.CENTER));
        tablaDatos.addCell(new Cell().add("CANTIDAD").setBold().setTextAlignment(TextAlignment.CENTER));
        
        
        List<Concepto> datos = ConceptoRepository.getAllConceptos(fecha, categoria);
        
        // Por cada concepto generar 3 celdas con sus valores
        for (Concepto concepto : datos){
            for (int i = 0; i < 3; i++){
                Cell cell = new Cell();
                cell.setFontSize(10f);
                switch(i){
                    case 0:
                        cell.add(concepto.getFecha().toString());
                        cell.setTextAlignment(TextAlignment.CENTER);
                        break;
                    case 1:
                        cell.add(concepto.getDescripcion());
                        break;
                    case 2: 
                        cell.add(darFormato(concepto.getCantidad()));
                        cell.setTextAlignment(TextAlignment.RIGHT);
                        break;
                    default:
                        break;
                }
                tablaDatos.addCell(cell);
            }
        }
        
        int celdas = 90 - (datos.size() * 3);
        
        for (int i = 0; i < celdas; i++){
            tablaDatos.addCell(new Cell().setHeight(10f));
        }
        
        // Agregar el total
        tablaDatos.addCell(new Cell(1, 2).add("SUMA DE " + suma).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(Inventario.getTotal(datos))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        
        document.add(tablaDatos);
    }
    
    public void addPie(){
        Table piePagina = new Table(new float[]{25,250,150,250,25});
        piePagina.addCell(new Cell().setHeight(50f).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        piePagina.addCell(new Cell(1,2).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().add("ATENTAMENTE").setBold().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell(1,2).setBorder(Border.NO_BORDER));
        
        piePagina.addCell(new Cell().setHeight(50f).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().add(ControlIglesias.activo.getResponsable()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.BOTTOM).setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().add(ControlIglesias.activo.getTesorero()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.BOTTOM).setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().add("Responsable de la Obra").setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP).setFontSize(10).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().add("Tesorero de la Iglesia").setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP).setFontSize(10).setBorder(Border.NO_BORDER));
        piePagina.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        document.add(piePagina);
    }
    
    public void resumenMensual(){
        float[] columsWidth = {525, 125};
        Table tablaDatos = new Table(columsWidth);
        tablaDatos.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1f));
        
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("C04RME").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell(1,2).add("RESUMEN MENSUAL DE EGRESOS").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold());
        tablaDatos.addCell(new Cell().add("CONCEPTO").setBold().setTextAlignment(TextAlignment.CENTER));
        tablaDatos.addCell(new Cell().add("IMPORTE").setBold().setTextAlignment(TextAlignment.CENTER));
        
        List<Concepto> datos = ConceptoRepository.getAllConceptos(fecha, Categoria.EGRESOS);
        
        for (Concepto concepto : datos){
            for (int i = 0; i < 2; i++){
                Cell cell = new Cell();
                cell.setFontSize(10f);
                switch(i){
                    case 0:
                        cell.add(concepto.getDescripcion());
                        break;
                    case 1:
                        cell.add(darFormato(concepto.getCantidad()));
                        cell.setTextAlignment(TextAlignment.RIGHT);
                        break;
                    default:
                        break;
                }
                tablaDatos.addCell(cell);
            }
        }
        
        int celdas = 46 - (datos.size() * 2);
        
        for (int i = 0; i < celdas; i++){
            tablaDatos.addCell(new Cell().setHeight(10f));
        }
        
        
        
        tablaDatos.addCell(new Cell().add("SUMA DE EGRESOS").setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(egresosMes)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        
        document.add(tablaDatos);
    }
    
    public void addResumen(){
        
        document.add(new Paragraph(""));
        document.add(new Paragraph("RESUMEN MENSUAL DE INGRESOS").setTextAlignment(TextAlignment.CENTER).setBold());
        
        float[] columsWidth = {125,250,150, 125};
        Table tablaDatos = new Table(columsWidth);
        tablaDatos.setBorder(new SolidBorder(0.5f));
        
        tablaDatos.addCell(new Cell(1,4).setHeight(15f).setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER).setHeight(20f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("SALDO DEL MES ANTERIOR").setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM));
        tablaDatos.addCell(new Cell().add(darFormato(saldoMesAnterior)).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setHeight(50f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("Ofrendas Generales").setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(ofrendasGenerales)).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setHeight(15f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("Ofrendas Especiales").setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(ofrendasEspeciales)).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setHeight(15f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("Talentos").setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(talentos)).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setHeight(15f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("Primicias").setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(primicias)).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell().setHeight(15f).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add("Diezmos").setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER));
        tablaDatos.addCell(new Cell().add(darFormato(diezmos)).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)));
        tablaDatos.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tablaDatos.addCell(new Cell(1,4).setHeight(15f).setBorder(Border.NO_BORDER));
        
        
        Table tabla = new Table(columsWidth);
        
        tablaDatos.addCell(new Cell(1,4).setHeight(15f).setBorder(Border.NO_BORDER));
        
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER).setHeight(20f).setBorder(Border.NO_BORDER));
        tabla.addCell(new Cell().add("Suma de Ingresos: ").setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.RIGHT));
        tabla.addCell(new Cell().add(darFormato(sumaIngresos)).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorderBottom(new SolidBorder(0.5f)));
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER).setHeight(20f).setBorder(Border.NO_BORDER));
        tabla.addCell(new Cell().add("Menos Egresos de este mes: ").setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.RIGHT));
        tabla.addCell(new Cell().add(darFormato(egresosMes)).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorderBottom(new SolidBorder(0.5f)));
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER).setHeight(20f).setBorder(Border.NO_BORDER));
        tabla.addCell(new Cell().add("Saldo en caja para el Proximo mes").setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.RIGHT));
        tabla.addCell(new Cell().add(darFormato(sumaIngresos - egresosMes)).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM).setBorderBottom(new SolidBorder(0.5f)));
        tabla.addCell(new Cell().setBorder(Border.NO_BORDER));
        
        document.add(tablaDatos);
        document.add(new Paragraph());
        document.add(new Paragraph());
        document.add(tabla);
    }
    
    
    public static String darFormato(Double cantidad){
        return String.format(Locale.US,"$%,.2f", cantidad);
    }
}