import com.rapture.controlgastos.models.Categoria;
import com.rapture.controlgastos.models.Concepto;
import com.rapture.controlgastos.services.SessionFactoryMaker;
import java.time.LocalDate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author edgar
 */

public class DatabasePersistTest {
    public static void main(String[] args){
        Concepto c = new Concepto(LocalDate.now(),"Davis Manca",500,Categoria.DIEZMOS);
        SessionFactory factory = SessionFactoryMaker.getFactory();
        try {
            Session session = factory.openSession();
            Transaction tx = session.beginTransaction();
            session.persist(c);
            tx.commit();
            System.out.println("Persistencia Exitosa");
            session.close();
            
            session = factory.openSession();
            tx = session.beginTransaction();
            Concepto concepto = session.merge(c);
            concepto.setCantidad(6969d);
            tx.commit();
            session.close();
            
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error en la persistencia");
        }
    }
}
