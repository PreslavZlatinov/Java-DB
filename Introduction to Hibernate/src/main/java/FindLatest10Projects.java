import entities.Project;

import javax.persistence.EntityManager;
import java.util.Comparator;

public class FindLatest10Projects {

    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        entityManager
                .createQuery(Constants.Queries.GET_PROJECTS_ORDER_BY_START_DATA_DESC, Project.class)
                .setMaxResults(10)
                .getResultList()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(System.out::println);

        entityManager.close();
    }
}