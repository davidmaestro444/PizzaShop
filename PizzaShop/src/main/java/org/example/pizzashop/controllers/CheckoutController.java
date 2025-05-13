package org.example.pizzashop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.pizzashop.model.CartItem;
import org.example.pizzashop.model.PaymentStrategy;
import org.example.pizzashop.model.CreditCardPayment;
import org.example.pizzashop.model.CashPayment;

import java.util.List;

public class CheckoutController {

    @FXML private ComboBox<String> paymentMethodComboBox;

    private List<CartItem> order;
    private PaymentStrategy paymentStrategy;

    public PaymentStrategy getPaymentStrategy() {
        return this.paymentStrategy;
    }

    @FXML
    public void initialize() {
        paymentMethodComboBox.getItems().addAll("Bankkártya", "Készpénz");

        paymentMethodComboBox.setOnAction(event -> {
            String selected = paymentMethodComboBox.getValue();
            if ("Bankkártya".equals(selected)) {
                paymentStrategy = new CreditCardPayment();
            } else if ("Készpénz".equals(selected)) {
                paymentStrategy = new CashPayment();
            }
        });
    }

    @FXML
    private void confirmPayment(ActionEvent event) {
        if (paymentStrategy == null) {
            showAlert("Hiba", "Nincs kiválasztott fizetési mód!");
            return;
        }

        int total = calculateTotal();

        if (paymentStrategy instanceof CreditCardPayment) {
            paymentStrategy.pay(total);
            showAlert("Sikeres tranzakció!", "Sikeres tranzakció! Jó étvágyat kívánunk!");
            navigateToOrders(event);
        } else if (paymentStrategy instanceof CashPayment) {
            paymentStrategy.pay(total);
            showAlert("Fizetés készpénzzel", "Végösszeg: " + total + " Ft.\nKöszönjük a vásárlást!");
            navigateToOrders(event);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public int calculateTotal() {
        int total = 0;
        if (order != null) {
            for (CartItem item : order) {
                total += item.getBasePrice();
                for (var topping : item.getToppings()) {
                    total += topping.getPrice();
                }
            }
        }
        return total;
    }

    private void navigateToOrders(ActionEvent event) {
        // ide jön a rendeléseim oldal
    }

    public void setOrder(List<CartItem> order) {
        this.order = order;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}

