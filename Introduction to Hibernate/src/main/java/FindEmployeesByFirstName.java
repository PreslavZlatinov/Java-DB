import entities.Employee;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class FindEmployeesByFirstName {

    private static final String PRINT_FORMAT = "%s %s - %s - ($%.2f)%n";

    public static void main(String[] args) {

        final EntityManager entityManager = Utils.createEntityManager();

        final String firstName = new Scanner(System.in).nextLine();

        entityManager
                .createQuery(Constants.Queries.GET_EMPLOYEES_WITH_FIRST_NAME_LIKE, Employee.class)
                .setParameter(Constants.PlaceHolders.FIRST_NAME, firstName + "%")
                .getResultList()
                .forEach(employee -> System.out.printf(PRINT_FORMAT,
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getJobTitle(),
                        employee.getSalary()));

        entityManager.close();
    }
}