import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.example.pizzashop.controllers.CartController;
import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.example.pizzashop.model.*;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void testGetAllAvailableToppings() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT * FROM toppings");
        }

        PizzaRepository dao = new PizzaRepository();
        List<Topping> result = dao.getAllAvailableToppings();

        assertEquals(15, result.size());
    }

    @Test
    void testGetTotalPrice() {
        BasicPizza pizza = new BasicPizza(1,"Margarita","", 1000);
        CartItem cartItem = new CartItem(pizza);

        cartItem.addToppings(List.of(
                new Topping(1,"Sajt", 200, "", false, false),
                new Topping(1,"Szalámi", 300, "", true, false)
        ));

        int actual = cartItem.getTotalPrice();
        assertEquals(1500, actual);
    }
}
