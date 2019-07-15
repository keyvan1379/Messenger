package JpaConfig;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("ServerSide");
    public static EntityManager getEntitManager(){
        return factory.createEntityManager();
    }
}
