package com.rapture.controlgastos.services;

import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.models.SaldoPorMes;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author edgar
 */

public class ConceptoRepository {
    private static Session session;
    private static SessionFactory factory = SessionFactoryMaker.getFactory();
    
    public static void guardar(Concepto concepto){
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        concepto.setIglesia(ControlIglesias.activo);
        try{
            session.persist(concepto);
            tx.commit();
            session.close();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }
    
    public static List<Concepto> getAllConceptos(LocalDate fecha, Categoria categoria){
        session = factory.openSession();
        LocalDate fechaInicial = LocalDate.of(fecha.getYear(), fecha.getMonth(), 1);
        LocalDate fechaFinal = LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getMonth().length(fecha.isLeapYear()));
        
        try {
            List<Concepto> lista = session.createQuery("SELECT c FROM Concepto c WHERE c.categoria = :categoria AND "
                    + "c.iglesia =: iglesia AND "
                    + "c.fecha BETWEEN :fecha1 AND :fecha2 ORDER BY c.fecha", Concepto.class)
                    .setParameter("categoria", categoria)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .setParameter("fecha1", fechaInicial)
                    .setParameter("fecha2", fechaFinal)
                    .list();
            return lista;
        } catch (Exception e){
            return List.of();
        }
    }

    public static List<Concepto> getConceptoByCategoria(LocalDate fecha, Categoria categoria) {
        session = factory.openSession();
        LocalDate fechaInicial = LocalDate.of(fecha.getYear(), fecha.getMonth(), 1);
        LocalDate fechaFinal = LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getMonth().length(fecha.isLeapYear()));
        try{
            return session.createQuery("SELECT c FROM Concepto c WHERE "
                    + "c.categoria=:categoria AND "
                    + "c.iglesia=:iglesia AND "
                    + "c.fecha BETWEEN :fecha1 AND :fecha2 ORDER BY c.fecha"
                    , Concepto.class)
                    .setParameter("categoria", categoria)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .setParameter("fecha1", fechaInicial)
                    .setParameter("fecha2", fechaFinal)
                    .list();  
        } catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }
    
    public static double getSaldoAnterior(LocalDate mesActual){
        Month mesAnterior = mesActual.getMonth().minus(1);
        LocalDate fecha;
        if (mesAnterior == Month.DECEMBER){
            fecha = LocalDate.of(mesActual.getYear() - 1, mesAnterior, mesAnterior.length(mesActual.isLeapYear()));
        } else {
            fecha = LocalDate.of(mesActual.getYear(), mesAnterior, mesAnterior.length(mesActual.isLeapYear()));
        }
        session = factory.openSession();
        try{
            Double ingresos = session.createQuery("SELECT SUM(c.cantidad) FROM Concepto c WHERE "
                    + "c.categoria!=:categoria AND "
                    + "c.fecha<=:fecha AND "
                    + "c.iglesia=:iglesia", Double.class)
                    .setParameter("categoria", Categoria.EGRESOS)
                    .setParameter("fecha", fecha)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .list().get(0);
            
            Double egresos = session.createQuery("SELECT SUM(c.cantidad) FROM Concepto c WHERE "
                    + "c.categoria=:categoria AND "
                    + "c.fecha<=:fecha AND "
                    + "c.iglesia=:iglesia", Double.class)
                    .setParameter("categoria", Categoria.EGRESOS)
                    .setParameter("fecha", fecha)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .list().get(0);
            session.close();
            if (ingresos != null && egresos != null){
                return ingresos - egresos;
            } else if (ingresos != null){
                return ingresos;
            } else if (egresos != null){
                return egresos * -1;
            } else {
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public static List<SaldoPorMes> getSaldoByMes(int anio){
        session = factory.openSession();
        try{
            List lista = session.createQuery("SELECT EXTRACT(MONTH FROM c.fecha) AS mes, " +
                "SUM(CASE WHEN c.categoria != 'EGRESOS' THEN c.cantidad else 0 END) AS ingresos, " +
                "SUM(CASE WHEN c.categoria = 'EGRESOS' THEN c.cantidad else 0 END) AS egresos " +
                "FROM Concepto c WHERE EXTRACT(YEAR FROM c.fecha)=:anio AND c.iglesia = :iglesia GROUP BY mes", SaldoPorMes.class)
                    .setParameter("anio", anio)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .list();
            
            session.close();
            return lista;
        } catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }
    
    public static Concepto getMayorConcepto(LocalDate fecha, Boolean getEgreso){
        session = factory.openSession();
        LocalDate fechaInicial = LocalDate.of(fecha.getYear(), fecha.getMonth(), 1);
        LocalDate fechaFinal = LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getMonth().length(fecha.isLeapYear()));
        String condicion = "!=";
        if (getEgreso){
            condicion = "=";
        }
        try {
            Concepto c = session.createQuery("SELECT c FROM Concepto c WHERE "
                    + "c.cantidad = (SELECT MAX(a.cantidad) FROM Concepto a WHERE a.categoria" + condicion + ":categoria AND a.fecha BETWEEN :fecha1 AND :fecha2) AND "
                    + "c.iglesia = :iglesia AND "
                    + "c.categoria" + condicion +":categoria AND "
                    + "c.fecha BETWEEN :fecha1 AND :fecha2", Concepto.class)
                    .setParameter("iglesia", ControlIglesias.activo)
                    .setParameter("categoria", Categoria.EGRESOS)
                    .setParameter("fecha1", fechaInicial)
                    .setParameter("fecha2", fechaFinal)
                    .list().get(0);
            session.close();
            return c;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    static void eliminar(Concepto c) {
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.remove(c);
            tx.commit();
            session.close();
        } catch (Exception e){
            
        }
    }

    static void editar(Concepto c) {
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(c);
            tx.commit();
            session.close();
        } catch (Exception e){
            
        }
    }
}
