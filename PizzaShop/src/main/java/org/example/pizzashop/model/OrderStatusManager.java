package org.example.pizzashop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderStatusManager implements IOrderSubject{
    private List<IOrderObserver> observers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    @Override
    public void registerObserver(IOrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IOrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Order order : orders) {
            for (IOrderObserver observer : observers) {
                observer.updateOrderStatus(order);
            }
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void startStatusUpdate() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Order order : orders) {
                    switch (order.getStatus()) {
                        case "Készítés alatt":
                            order.setStatus("Kiszállítás alatt");
                            break;
                        case "Kiszállítás alatt":
                            order.setStatus("Teljesítve");
                            break;
                        case "Teljesítve":
                            continue;
                    }
                    updateOrderInDatabase(order);
                }
                notifyObservers();
            }
        }, 0, 10000);
    }

    private void updateOrderInDatabase(Order order) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "")) {
            String query = "UPDATE orders SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, order.getStatus());
            stmt.setInt(2, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
