import com.rapture.controlgastos.services.SessionFactoryMaker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 *
 * @author edgar
 */

public class DatabaseConnectionTest {
    
    public static void main(String[] args){
        
        SessionFactory factory = SessionFactoryMaker.getFactory();
        
        Session session = factory.openSession();
        if (session.isConnected()){
            System.out.println("Conexion Exitosa");
        }
    }
}
