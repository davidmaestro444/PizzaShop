package org.example.pizzashop.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.pizzashop.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartController {
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> pizzaNameColumn;
    @FXML private TableColumn<CartItem, Integer> priceColumn;
    @FXML private ComboBox<Topping> toppingsComboBox;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private PizzaRepository pizzaRepository;
    private PaymentStrategy paymentStrategy;

    public void initialize() {
        pizzaRepository = new PizzaRepository();
        try {
            pizzaNameColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaName"));
            priceColumn.setCellValueFactory(cellData -> {
                CartItem item = cellData.getValue();
                int totalPrice = item.getBasePrice();
                for (Topping topping : item.getToppings()) {
                    totalPrice += topping.getPrice();
                }
                return new SimpleIntegerProperty(totalPrice).asObject();
            });

            priceColumn.setCellFactory(column -> new TableCell<CartItem, Integer>() {
                @Override
                protected void updateItem(Integer price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(price + " Ft");
                    }
                }
            });

            loadAvailableToppings();
            toppingsComboBox.getItems().addAll();
            cartTable.setItems(cartItems);
            updateTotalLabel();
        } finally {
            pizzaRepository.close();
        }
    }

    private void loadAvailableToppings() {
        List<Topping> toppings = pizzaRepository.getAllAvailableToppings();
        ObservableList<Topping> observableToppings = FXCollections.observableArrayList(toppings);
        toppingsComboBox.setItems(observableToppings);

        toppingsComboBox.setCellFactory(lv -> new ListCell<Topping>() {
            @Override
            protected void updateItem(Topping item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (+" + item.getPrice() + " Ft)");
                }
            }
        });

        toppingsComboBox.setButtonCell(new ListCell<Topping>() {
            @Override
            protected void updateItem(Topping item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (+" + item.getPrice() + " Ft)");
                }
            }
        });
    }

    public void setCartItems(List<CartItem> items) {
        cartItems.setAll(items);

        if (cartTable != null) {
            cartTable.setItems(cartItems);
        }
        updateTotalLabel();
    }


    @FXML
    private void addTopping(ActionEvent event) {
        Topping selectedTopping = toppingsComboBox.getValue();
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedTopping != null) {
            selectedItem.addToppings(selectedTopping);
            cartTable.refresh();
            updateTotalLabel();
        }
    }

    private void updateTotalLabel(){
        int total = calculateTotal();
        totalLabel.setText(total + " Ft");
    }

    private int calculateTotal(){
        int total = 0;

        for(CartItem item : cartItems){
            total+=item.getBasePrice();

            for(Topping topping : item.getToppings()){
                total += topping.getPrice();
            }
        }
        return total;
    }

    @FXML
    private void proceedToCheckout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pizzashop/fxml/checkout-view.fxml"));
            Parent root = loader.load();

            CheckoutController controller = loader.getController();
            controller.setOrder(cartItems);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Fizetés");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pizzashop/fxml/orders-view.fxml")); //orders viewt megvalósítani
            Parent parent = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPizzas(List<CartItem> pizzas) {
        cartItems.clear();
        cartItems.addAll(pizzas);
        updateTotalLabel();
    }

   public void goBack(ActionEvent actionEvent) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pizzashop/fxml/pizza-selector-view.fxml"));
           Parent parent = loader.load();

           PizzaSelectorController pizzaSelectorController = loader.getController();
           pizzaSelectorController.setOrder(new ArrayList<>(cartItems), toppingsComboBox.getItems());

           Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
           stage.setScene(new Scene(parent));
           stage.show();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public void deletePizza(ActionEvent actionEvent) {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            cartTable.refresh();
            updateTotalLabel();
        }
    }

    public void deleteAllTopping(ActionEvent actionEvent) {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            selectedItem.getToppings().clear();
            cartTable.refresh();
            updateTotalLabel();
        }
    }

    public void deleteCart(ActionEvent actionEvent) {
        cartItems.clear();
        updateTotalLabel();
    }
}