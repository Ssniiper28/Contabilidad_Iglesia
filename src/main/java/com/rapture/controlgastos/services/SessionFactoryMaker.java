
package com.rapture.controlgastos.services;

import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.models.Iglesia;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author edgar
 */
public class SessionFactoryMaker {
    private static SessionFactory factory;
    
    private static void configureFactory(){
        try{
            Configuration configuration = new Configuration();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            configuration.addAnnotatedClass(Concepto.class)
                    .addAnnotatedClass(Iglesia.class);
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable e){
            System.out.println("Failed to create sessionFactory" + e);
            throw new RuntimeException(e);
        }   
    }
    
    public static SessionFactory getFactory(){
        if (factory == null){
            configureFactory();
        } 
        
        return factory;
    }
}
