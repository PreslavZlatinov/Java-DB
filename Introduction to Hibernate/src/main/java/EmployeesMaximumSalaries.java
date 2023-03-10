import entities.Employee;

import javax.persistence.EntityManager;
import java.util.Comparator;

public class EmployeesMaximumSalaries {

    private final static String PRINT_FORMAT = "%s - %.2f%n";

    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        entityManager
                .createQuery(Constants.Queries.GET_EMPLOYEE_MAX_SALARY_BY_DEPARTMENT_WHERE_SALARY_BETWEEN_30000_AND_70000_ORDER_BY_SALARY,
                        Employee.class)
                .getResultList()
                .stream()
                .sorted(Comparator.comparing(e -> e.getDepartment().getId()))
                .forEach(employee -> System.out.printf(PRINT_FORMAT,
                        employee.getDepartment().getName(),
                        employee.getSalary()));

        entityManager.close();
    }
}