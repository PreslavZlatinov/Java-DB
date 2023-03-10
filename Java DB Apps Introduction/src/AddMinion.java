import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddMinion {

    private static final String INSERT_TOWN = "INSERT INTO towns(name)" +
            " VALUES(?);";

    private static final String INSERT_VILLAIN = "INSERT INTO `villains`(name,`evilness_factor`)" +
            " VALUES(?,'evil');";

    private static final String INSERT_MINION = "INSERT INTO minions(name,age,town_id)" +
            " VALUES(?,?,?);";

    private static final String INSERT_MINION_VILLAIN = "INSERT INTO `minions_villains`(`minion_id`,`villain_id`)" +
            " VALUES(?,?);";

    private static final String GET_TOWN_NAME = "SELECT id, name FROM towns" +
            " WHERE name = ?;";

    private static final String GET_VILLAIN_NAME = "SELECT id, name FROM villains WHERE name = ?;";

    private static final String GET_MINION_NAME = "SELECT id, name FROM minions WHERE name = ?;";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Connection connection = Utils.getSQLConnection();

        String minionInfo = scanner.nextLine();
        String villainInfo = scanner.nextLine();

        String[] minionTokens = minionInfo.split("\\s+");
        String[] villainTokens = villainInfo.split("\\s+");

        String minionName = minionTokens[1];
        int minionAge = Integer.parseInt(minionTokens[2]);
        String townName = minionTokens[3];
        String villainName = villainTokens[1];

        PreparedStatement townStatement = connection.prepareStatement(GET_TOWN_NAME);

        townStatement.setString(1,townName);

        ResultSet resultSetTown = townStatement.executeQuery();

        if(!resultSetTown.next()) {
            PreparedStatement townInsertStatement = connection.prepareStatement(INSERT_TOWN);

            townInsertStatement.setString(1,townName);

            townInsertStatement.executeUpdate();

            System.out.printf("Town %s was added to the database.%n",townName);
        }

        ResultSet rSetTown = townStatement.executeQuery();
        rSetTown.next();
        int townID = rSetTown.getInt(1);

        PreparedStatement villainStatement = connection.prepareStatement(GET_VILLAIN_NAME);

        villainStatement.setString(1,villainName);

        ResultSet resultSetVillain = villainStatement.executeQuery();

        if(!resultSetVillain.next()) {
            PreparedStatement villainInsertStatement = connection.prepareStatement(INSERT_VILLAIN);

            villainInsertStatement.setString(1,villainName);

            villainInsertStatement.executeUpdate();

            System.out.printf("Villain %s was added to the database.%n",villainName);
        }

        ResultSet rSetVillain = villainStatement.executeQuery();
        rSetVillain.next();
        int villainID = rSetVillain.getInt(1);

        PreparedStatement minionStatement = connection.prepareStatement(GET_MINION_NAME);

        minionStatement.setString(1,minionName);

        ResultSet resultSetMinion = minionStatement.executeQuery();

        if(!resultSetMinion.next()) {
            PreparedStatement minionInsertStatement = connection.prepareStatement(INSERT_MINION);

            minionInsertStatement.setString(1,minionName);
            minionInsertStatement.setInt(2,minionAge);
            minionInsertStatement.setInt(3,townID);

            minionInsertStatement.executeUpdate();
        }

        ResultSet rSetMinion = minionStatement.executeQuery();
        rSetMinion.next();
        int minionID = rSetMinion.getInt(1);

        PreparedStatement minionVillainStatement = connection.prepareStatement(INSERT_MINION_VILLAIN);

        minionVillainStatement.setInt(1, minionID);
        minionVillainStatement.setInt(2, villainID);

        minionVillainStatement.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.",minionName,villainName);
    }
}
