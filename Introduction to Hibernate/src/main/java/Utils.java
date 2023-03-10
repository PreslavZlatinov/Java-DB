import javax.persistence.EntityManager;
import javax.persistence.Persistence;

class Utils {

    static EntityManager createEntityManager() {
        return Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME)
                .createEntityManager();
    }
}