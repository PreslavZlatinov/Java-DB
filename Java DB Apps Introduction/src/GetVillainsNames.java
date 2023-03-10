import java.sql.*;
import java.util.Properties;

public class GetVillainsNames {

    private static final String GET_VILLAINS_NAMES = "SELECT v.`name`, COUNT(DISTINCT mv.minion_id) as 'count' FROM `villains` as v" +
            " JOIN `minions_villains` as mv on v.id = mv.`villain_id`" +
            " GROUP BY v.`name`" +
            " HAVING count > 15" +
            " ORDER BY count DESC;";

    public static void main(String[] args) throws SQLException {

        Connection connection = Utils.getSQLConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(GET_VILLAINS_NAMES);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d",resultSet.getString("name"),resultSet.getInt("count"));
        }
    }
}
