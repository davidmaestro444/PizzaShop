package org.example.pizzashop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import org.example.pizzashop.dbConnection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaSelectorController {
    public VBox contentBox;
    public Label cartItemCount;
    @FXML
    private GridPane pizzaGrid;

    @FXML
    private ScrollPane scrollPane;

    private static final int CARDS_PER_ROW = 3;

    @FXML
    public void initialize() {
        loadPizzasFromDatabase();
        updateCartCount(0);
    }

    private void loadPizzasFromDatabase() {
        List<Pizza> pizzas = fetchPizzasFromDB();
        displayPizzaCards(pizzas);
    }

    private List<Pizza> fetchPizzasFromDB() {
        List<Pizza> pizzas = new ArrayList<>();
        String query = "SELECT name, description, price FROM pizzas";
        DatabaseConnection db;
        try {
            db = new DatabaseConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = db.getDbConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Pizza pizza = new Pizza(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                );
                pizzas.add(pizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzas;
    }

    private void displayPizzaCards(List<Pizza> pizzas) {
        pizzaGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Pizza pizza : pizzas) {
            VBox card = createPizzaCard(pizza);
            pizzaGrid.add(card, col, row);

            col++;
            if (col >= CARDS_PER_ROW) {
                col = 0;
                row++;
            }
        }
        contentBox.requestLayout();
    }

    private VBox createPizzaCard(Pizza pizza) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(200);

        Label nameLabel = new Label(pizza.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;");

        Label descLabel = new Label(pizza.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(180);
        descLabel.setStyle("-fx-font-size: 10pt;");

        Label priceLabel = new Label(pizza.getPrice() + " Ft");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt; -fx-text-fill: #e74c3c;");

        Button orderButton = new Button("Kosárba");
        orderButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        orderButton.setOnAction(e -> addToCart(pizza));

        card.getChildren().addAll(nameLabel, descLabel, priceLabel, orderButton);
        return card;
    }

    private void addToCart(Pizza pizza) {
        System.out.println("Kosárba téve: " + pizza.getName());
    }

    @FXML
    public void showCart(ActionEvent actionEvent) {
    }

    @FXML
    public void showOrders(ActionEvent actionEvent) {
    }

    public void updateCartCount(int count) {
        cartItemCount.setText(count + " tétel");
    }


    private static class Pizza {
        private final String name;
        private final String description;
        private final int price;

        public Pizza(String name, String description, int price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getPrice() { return price; }
    }
}
