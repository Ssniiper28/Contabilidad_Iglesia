
import com.rapture.controlgastos.App;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author edgar
 */
public class TestDeFunciones {
    public static void main(String[] args){
        //ControlIglesias.activo = IglesiaRepository.getIglesiaByActivo();
        //ConceptoRepository.getSaldoAnterior(LocalDate.of(2023, Month.SEPTEMBER, Month.SEPTEMBER.maxLength()));
//        var datos = ConceptoRepository.getSaldoByMes(2023);
//        datos.forEach(dato -> System.out.println(dato.getIngresos() + " " + dato.getEgresos()));

        String url = App.class.getResource("").getPath();
        
        System.out.println(url);
    }
}
