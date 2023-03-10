import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IncreaseMinionsAge {

    private static final String INCREASE_MINION_AGE = "UPDATE minions SET `name` = lower(`name`), age = age + 1 WHERE id = ?;";

    private static final String GET_MINIONS_NAMES = "SELECT `name`, age FROM minions;";

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = Utils.getSQLConnection();

        String[] idInput = scanner.nextLine().split("\\s+");

        for (int i = 0; i < idInput.length; i++) {
            PreparedStatement preparedStatement = connection.prepareStatement(INCREASE_MINION_AGE);

            preparedStatement.setInt(1,Integer.parseInt(idInput[i]));

            preparedStatement.executeUpdate();
        }

        PreparedStatement preparedStatement = connection.prepareStatement(GET_MINIONS_NAMES);

        ResultSet resultSet = preparedStatement.executeQuery();


        while (resultSet.next()) {
            System.out.printf("%s %d%n",resultSet.getString(1),resultSet.getInt(2));
        }

    }
}
