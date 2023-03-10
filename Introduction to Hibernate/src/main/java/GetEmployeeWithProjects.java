import entities.Employee;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class GetEmployeeWithProjects {
    public static void main(String[] args) {
        final EntityManager entityManager = Utils.createEntityManager();

        final int empId = new Scanner(System.in).nextInt();

        System.out.println(entityManager.createQuery(Constants.Queries.GET_EMPLOYEE_BY_ID, Employee.class)
                .setParameter(Constants.PlaceHolders.ID, empId)
                .getSingleResult()
                .toString());

    }
}
