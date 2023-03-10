import entities.Employee;

import javax.persistence.EntityManager;

public class EmployeesFromDepartment {
    private static final String PRINT_EMP_FORMAT = "%s %s from %s - $%.2f%n";

    public static void main(String[] args) {
        final EntityManager entityManager = Utils.createEntityManager();

        entityManager.getTransaction().begin();

        entityManager.createQuery(Constants.Queries.GET_ALL_EMPLOYEES_BY_DEPARTMENT_ORDER_BY_SALARY, Employee.class)
                .setParameter(Constants.PlaceHolders.DEPARTMENT_NAME, Constants.Departments.RESEARCH_AND_DEV)
                .getResultList()
                .forEach(e -> System.out.printf(PRINT_EMP_FORMAT,
                        e.getFirstName(),
                        e.getLastName(),
                        e.getDepartment().getName(),
                        e.getSalary()));

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
