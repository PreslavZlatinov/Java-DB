import entities.Town;

import javax.persistence.EntityManager;
import java.util.List;

public class ChangeCasing {
    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        entityManager.getTransaction().begin();

        entityManager.createQuery(Constants.Queries.UPDATE_ALL_TOWNS_WITH_LENGTH_NAME_MORE_THAN_5).executeUpdate();

        final List<Town> resultList = entityManager.createQuery(Constants.Queries.GET_ALL_TOWNS, Town.class)
                .getResultList();

        for (Town town : resultList) {
            final String townName = town.getName();

            if (townName.length() <= 5) {
                town.setName(townName.toUpperCase());

                entityManager.persist(town);
            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}