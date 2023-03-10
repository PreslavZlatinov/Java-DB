import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class RemoveTowns {

    private final static String PRINT_FORMAT = "%d address%s in %s deleted%n";

    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();
        final String townName = new Scanner(System.in).nextLine().trim();

        final Town town = entityManager.createQuery(Constants.Queries.GET_TOWN_BY_TOWN_NAME, Town.class)
                .setParameter(Constants.PlaceHolders.TOWN_NAME, townName)
                .getSingleResult();

        final List<Address> addresses = entityManager
                .createQuery(Constants.Queries.GET_ALL_ADDRESSES_BY_TOWN_NAME, Address.class)
                .setParameter(Constants.PlaceHolders.TOWN_NAME, townName)
                .getResultList();

        entityManager.getTransaction().begin();

        addresses.forEach(address -> {
            for (Employee employee : address.getEmployees()) {
                employee.setAddress(null);
            }
            address.setTown(null);
            entityManager.remove(address);
        });

        entityManager.remove(town);

        entityManager.getTransaction().commit();

        System.out.printf(PRINT_FORMAT,
                addresses.size(),
                addresses.size() != 1 ? "es" : "",
                town.getName());

        entityManager.close();
    }
}
