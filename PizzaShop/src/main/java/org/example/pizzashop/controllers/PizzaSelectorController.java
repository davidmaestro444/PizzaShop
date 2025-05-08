package org.example.pizzashop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import org.example.pizzashop.model.*;

public class PizzaSelectorController {
    public VBox contentBox;
    public Label cartItemCount;
    public List<CartItem> order;
    private List<Topping> availableToppings;

    @FXML
    private GridPane pizzaGrid;

    @FXML
    private ScrollPane scrollPane;

    private static final int CARDS_PER_ROW = 3;

    @FXML
    public void initialize() {
        loadPizzasFromDatabase();
        updateCartCount(0);
        order = new ArrayList<>();
    }

    private void loadPizzasFromDatabase() {
        PizzaRepository repository = new PizzaRepository();
        List<Pizza> pizzas = new ArrayList<>(repository.getAllAvailablePizzas());
        displayPizzaCards(pizzas);
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
        order.add(new CartItem(pizza));
        updateCartCount(order.size());
    }

    @FXML
    public void showCart(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pizzashop/fxml/cart-view.fxml"));
            Parent root = loader.load();

            CartController cartController = loader.getController();

            cartController.setPizzas(order);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("PizzaShop - Kosár");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nem sikerült betölteni a kosár tartalmát");
            alert.show();
        }
    }

    @FXML
    public void showOrders(ActionEvent actionEvent) {
    }

    public void updateCartCount(int count) {
        cartItemCount.setText(count + " tétel");
    }

    public void setOrder(List<CartItem> cart, List<Topping> toppings) {
        this.order = new ArrayList<>(cart);
        this.availableToppings = toppings;
        updateCartCount(order.size());
    }
}
