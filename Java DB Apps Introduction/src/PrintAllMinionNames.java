import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintAllMinionNames {

    private static final String GET_MINIONS_NAMES = "SELECT `name` FROM minions;";

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Connection connection = Utils.getSQLConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(GET_MINIONS_NAMES);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> minionList = new ArrayList<>();

        while (resultSet.next()) {
            minionList.add(resultSet.getString("name"));
        }

        for (int i = 0; i < minionList.size()/2; i++) {
            System.out.println(minionList.get(i));
            System.out.println(minionList.get(minionList.size()-1-i));
        }
    }
}
