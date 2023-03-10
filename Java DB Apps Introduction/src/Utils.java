import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum Utils {
    ;
    static Connection getSQLConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "softunisql123");

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);
    }
}
