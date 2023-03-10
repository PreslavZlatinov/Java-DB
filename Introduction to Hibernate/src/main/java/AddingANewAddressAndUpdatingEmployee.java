import entities.Address;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class AddingANewAddressAndUpdatingEmployee {

    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        final String lastName = new Scanner(System.in).nextLine();

        entityManager.getTransaction().begin();

        final Address newAddress = new Address();
        newAddress.setText(Constants.ADDRESS_FOR_ADDING);

        entityManager.persist(newAddress);

        int count = entityManager.createQuery(Constants.Queries.UPDATE_EMPLOYEE_ADDRESS_BY_LAST_NAME)
                .setParameter(Constants.PlaceHolders.ADDRESS_PLACEHOLDER, newAddress)
                .setParameter(Constants.PlaceHolders.LAST_NAME, lastName)
                .executeUpdate();

        if (count > 0) {
            entityManager.getTransaction().commit();
        } else {
            entityManager.getTransaction().rollback();
        }

        entityManager.close();

        System.out.println(count);
    }
}
