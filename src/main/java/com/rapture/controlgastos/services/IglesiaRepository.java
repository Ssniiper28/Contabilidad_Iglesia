package com.rapture.controlgastos.services;

import com.rapture.controlgastos.models.Iglesia;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author edgar
 */
public class IglesiaRepository {
    private static Session session;
    private static SessionFactory factory = SessionFactoryMaker.getFactory();
    
    public static void guardar(Iglesia i){
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            session.persist(i);
            tx.commit();
            session.close();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public static void eliminar(Iglesia iglesia) {
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            //List<Concepto> conceptos = session.createQuery("SELECT c FROM Concepto c WHERE c.iglesia=:iglesia",Concepto.class).setParameter("iglesia", iglesia).list();
            //session.remove(conceptos);
            session.remove(iglesia);
            tx.commit();
            session.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void editar(Iglesia iglesia) {
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.merge(iglesia);
            tx.commit();
            session.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public Iglesia getIglesiaById(long id){
        session = factory.openSession();
        try {
            Iglesia iglesia = session.get(Iglesia.class, id);
            session.close();
            return iglesia;
        } catch (Exception e){
            return null;
        }
    }
    
    public static Iglesia getIglesiaByActivo(){
        session = factory.openSession();
        try {
            Iglesia iglesia = session.createQuery("SELECT i FROM Iglesia i WHERE i.activo=true", Iglesia.class).getSingleResult();
            session.close();
            return iglesia;
        } catch (Exception e){
            return null;
        }
    }
    
    public static void setIglesiaActivo(Iglesia i){
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            if (ControlIglesias.activo != null){
                Iglesia anteriorActivo = session.merge(ControlIglesias.activo);
                anteriorActivo.setActivo(false);
            }
            Iglesia nuevoActivo = session.merge(i);
            nuevoActivo.setActivo(true);
            tx.commit();
            session.close();
        } catch (Exception e){
            
        }
    }
    
    public static List<Iglesia> getIglesias(){
        session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            return session.createQuery("From Iglesia", Iglesia.class).list();
        }catch (Exception e){
            return List.of();
        }
    }
}
