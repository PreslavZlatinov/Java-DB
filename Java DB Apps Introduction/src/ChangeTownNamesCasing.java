import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChangeTownNamesCasing {

    private static final String CHANGE_TOWN_NAME = "UPDATE towns SET `name` = upper(`name`) WHERE country = ?;";

    private static final String GET_ALL_TOWNS = "SELECT `name` FROM towns WHERE country = ?;";

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = Utils.getSQLConnection();

        String inputTown = scanner.nextLine();

        PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_TOWN_NAME);

        preparedStatement.setString(1,inputTown);

        int rowsChanged = preparedStatement.executeUpdate();

        if(rowsChanged == 0) {
            System.out.println("No town names were affected.");
        } else {
            System.out.printf("%d town names were affected.%n",rowsChanged);

            PreparedStatement preparedStatementTowns = connection.prepareStatement(GET_ALL_TOWNS);

            preparedStatementTowns.setString(1,inputTown);

            ResultSet resultSet = preparedStatementTowns.executeQuery();

            List<String> townList = new ArrayList<>();
            while (resultSet.next()) {
                townList.add(resultSet.getString("name"));
            }

            System.out.printf("[%s]",String.join(", ",townList));
        }

    }
}
