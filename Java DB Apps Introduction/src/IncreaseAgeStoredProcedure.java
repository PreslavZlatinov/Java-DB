import java.sql.*;
import java.util.Scanner;

public class IncreaseAgeStoredProcedure {

    private static final String GET_PROCEDURE = "{CALL usp_get_older (?)}";

    private static final String GET_MINION_NAME_AGE = "SELECT name, age FROM minions WHERE id = ?;";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        int minion_id = Integer.parseInt(scanner.nextLine());

        Connection connection = Utils.getSQLConnection();

        CallableStatement callableStatement = connection.prepareCall(GET_PROCEDURE);

        callableStatement.setInt(1,minion_id);
        callableStatement.execute();

        PreparedStatement preparedStatement = connection.prepareStatement(GET_MINION_NAME_AGE);

        preparedStatement.setInt(1,minion_id);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        System.out.printf("%s %d%n",resultSet.getString(1),resultSet.getInt(2));
    }
}
