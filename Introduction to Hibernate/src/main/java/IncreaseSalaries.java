import entities.Employee;

import javax.persistence.EntityManager;

public class IncreaseSalaries {

    private static final String PRINT_FORMAT = "%s %s ($%.2f)%n";

    public static void main(String[] args) {
        final EntityManager entityManager = Utils.createEntityManager();

        entityManager.getTransaction().begin();

        int countOfUpdates = entityManager.createQuery(Constants.Queries.UPDATE_EMPLOYEE_SALARIES_BY_12_PERCENTS)
                .executeUpdate();

        if (countOfUpdates != 0) {
            entityManager.getTransaction().commit();

            entityManager.createQuery(Constants.Queries.GET_UPDATED_EMPLOYEES_IN_DEPARTMENTS, Employee.class)
                    .getResultList()
                    .forEach(e -> System.out.printf(PRINT_FORMAT, e.getFirstName(), e.getLastName(), e.getSalary()));
        } else {
            entityManager.getTransaction().rollback();
        }

        entityManager.close();
    }
}
