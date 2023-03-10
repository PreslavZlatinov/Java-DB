import javax.persistence.EntityManager;
import java.util.Scanner;

public class ContainsEmployee {
    private static final String NO = "No matches";
    private static final String YES = "There are matches";

    public static void main(String[] args) {
        final EntityManager entityManager = Utils.createEntityManager();

        final String[] name = new Scanner(System.in).nextLine().split(" ");

        final String firstName = name[0];
        final String lastName = name[1];

        final Long countOfMatches = entityManager.createQuery(Constants.Queries.GET_COUNT_OF_EMPLOYEES_BY_FULL_NAME, Long.class)
                .setParameter(Constants.PlaceHolders.FIRST_NAME, firstName)
                .setParameter(Constants.PlaceHolders.LAST_NAME, lastName)
                .getSingleResult();

        if (countOfMatches == 0) {
            System.out.println(NO);
        } else {
            System.out.println(YES);
        }
    }
}