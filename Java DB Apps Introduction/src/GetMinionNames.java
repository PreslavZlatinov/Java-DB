import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GetMinionNames {

    private static final String GET_MINION_NAMES = "SELECT m.`name`, m.age, v.`name` FROM minions as m" +
            " JOIN `minions_villains` as mv on m.id = mv.`minion_id`" +
            " JOIN villains as v on mv.`villain_id` = v.id" +
            " WHERE v.id = ?";

    public static void main(String[] args) throws SQLException {

        Connection connection = Utils.getSQLConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(GET_MINION_NAMES);

        Scanner scanner = new Scanner(System.in);

        int villainID = Integer.parseInt(scanner.nextLine());

        preparedStatement.setInt(1,villainID);

        ResultSet resultSet = preparedStatement.executeQuery();

        int i = 0;

        if(!resultSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.",villainID);
        } else {
            i++;
            System.out.printf("Villain: %s%n", resultSet.getString(3));
            System.out.printf("%d. %s %d%n",i ,resultSet.getString(1),resultSet.getInt(2));
        }

        while (resultSet.next()) {
            i++;
            System.out.printf("%d. %s %d%n",i ,resultSet.getString(1),resultSet.getInt(2));
        }
    }
}
