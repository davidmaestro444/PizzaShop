import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
    @Test
    void databaseTest() {
        DatabaseConnection dbConnection = null;
        try {
            dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3306/pizzashop", "root","");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean actual = (dbConnection.getDbConnection() != null);
        assertTrue(actual);

        try {
            dbConnection.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
