import entities.Address;

import javax.persistence.EntityManager;

public class AddressesWithEmployeeCount {
    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        entityManager.createQuery(Constants.Queries.GET_ALL_ADDRESSES_ORDER_BY_EMPLOYEE_SIZE_DESC, Address.class)
                .setMaxResults(10)
                .getResultList()
                .forEach(System.out::println);

        entityManager.close();
    }
}
