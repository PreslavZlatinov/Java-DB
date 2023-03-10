import javax.persistence.EntityManager;

public class EmployeesWithSalaryOver50000 {
    public static void main(String[] args) {
        final EntityManager entityManager = Utils.createEntityManager();

        entityManager.createQuery(Constants.Queries.GET_EMPLOYEES_FIRST_NAMES_WITH_SALARY_MORE_THAN_50000, String.class)
                .getResultList()
                .forEach(System.out::println);

        entityManager.getTransaction().commit();

        entityManager.close();
    }
}