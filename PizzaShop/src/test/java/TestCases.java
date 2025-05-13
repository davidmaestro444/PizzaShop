import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.example.pizzashop.controllers.CartController;
import org.example.pizzashop.controllers.CheckoutController;
import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.example.pizzashop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    private OrderStatusManager manager;
    private TestObserver observer;

    @BeforeEach
    void setUp() {
        manager = new OrderStatusManager();
        observer = new TestObserver();
        manager.registerObserver(observer);
        checkoutController = new CheckoutController();
        testOrder = FXCollections.observableArrayList();
    }

    @Test
    void testOrderStatusUpdates() throws InterruptedException {
        Order order = new Order(1, "Készítés alatt");
        manager.addOrder(order);

        order.setStatus("Kiszállítás alatt");
        manager.notifyObservers();

        observer.awaitNotification(1, TimeUnit.SECONDS);

        assertEquals("Kiszállítás alatt", order.getStatus());
        assertTrue(observer.wasNotified());
        assertEquals(1, observer.getNotifiedOrder().getId());
    }

    private static class TestObserver implements IOrderObserver {
        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean notified = false;
        private Order notifiedOrder;

        @Override
        public void updateOrderStatus(Order order) {
            notified = true;
            notifiedOrder = order;
            latch.countDown();
        }

        public boolean wasNotified() {
            return notified;
        }

        public Order getNotifiedOrder() {
            return notifiedOrder;
        }

        public void awaitNotification(long timeout, TimeUnit unit) throws InterruptedException {
            latch.await(timeout, unit);
        }
    }

    private CheckoutController checkoutController;
    private List<CartItem> testOrder;

    @Test
    void testCalculateTotalWithDecoratedPizza() {
        BasicPizza margherita = new BasicPizza(1, "Margherita", "Paradicsom szósz, mozzarella", 1990);

        Topping parmesan = new Topping(13, "Parmezán", 300, "cheese", false, true);
        Topping mushroom = new Topping(6, "Gomba", 200, "vegetable", false, true);

        CartItem item = new CartItem(margherita);
        item.addToppings(List.of(parmesan, mushroom));

        testOrder.add(item);
        checkoutController.setOrder(testOrder);

        assertEquals(2490, checkoutController.calculateTotal());
    }

    @Test
    void testCalculateTotalWithMultipleItems() {
        BasicPizza pepperoni = new BasicPizza(2, "Pepperoni", "Paradicsom szósz, mozzarella, pepperoni", 2290);
        Topping extraPepperoni = new Topping(1, "Extra pepperoni", 300, "meat", false, false);

        CartItem item1 = new CartItem(pepperoni);
        item1.addToppings(extraPepperoni);

        BasicPizza hawaii = new BasicPizza(3, "Hawaii", "Paradicsom szósz, mozzarella, sonka, ananász", 2390);
        CartItem item2 = new CartItem(hawaii);

        testOrder.add(item1);
        testOrder.add(item2);
        checkoutController.setOrder(testOrder);
        
        assertEquals(4980, checkoutController.calculateTotal());
    }

    @Test
    void testEmptyOrderTotal() {
        checkoutController.setOrder(testOrder);
        assertEquals(0, checkoutController.calculateTotal());
    }

    @Test
    void testPaymentStrategySelection() {
        checkoutController.setPaymentStrategy(new CreditCardPayment());
        assertTrue(checkoutController.getPaymentStrategy() instanceof CreditCardPayment);

        checkoutController.setPaymentStrategy(new CashPayment());
        assertTrue(checkoutController.getPaymentStrategy() instanceof CashPayment);
    }


}
