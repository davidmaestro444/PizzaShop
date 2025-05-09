package org.example.pizzashop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.pizzashop.model.CartItem;
import org.example.pizzashop.model.PaymentStrategy;
import org.example.pizzashop.model.CreditCardPayment;
import org.example.pizzashop.model.CashPayment;

import java.util.List;

public class CheckoutController {

    @FXML private TextField cardNumberField;
    @FXML private TextField expirationDateField;
    @FXML private TextField cvvField;

    private List<CartItem> order;
    private PaymentStrategy paymentStrategy;

    @FXML
    public void initialize() {

    }

    @FXML
    private void confirmPayment(ActionEvent event) {
        if (paymentStrategy == null) {
            showAlert("Hiba", "Nincs kiválasztott fizetési mód!");
            return;
        }

        if (paymentStrategy instanceof CreditCardPayment) {
            String cardNumber = cardNumberField.getText();
            String expirationDate = expirationDateField.getText();
            String cvv = cvvField.getText();

            if (isValidCardDetails(cardNumber, expirationDate, cvv)) {
                paymentStrategy.pay(calculateTotal());
                showAlert("Sikeres fizetés", "A fizetés sikeresen megtörtént!");
                navigateToOrders(event);
            } else {
                showAlert("Hiba", "Érvénytelen kártyaadatok!");
            }
        } else if (paymentStrategy instanceof CashPayment) {
            paymentStrategy.pay(calculateTotal());
            showAlert("Sikeres fizetés", "A fizetés készpénzben sikeresen megtörtént!");
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

    private boolean isValidCardDetails(String cardNumber, String expirationDate, String cvv) {
        return cardNumber != null && expirationDate != null && cvv != null &&
                cardNumber.length() == 16 && expirationDate.length() == 5 && cvv.length() == 3;
    }

    private int calculateTotal() {
        int total = 0;
        for (CartItem item : order) {
            total += item.getBasePrice();

            for (var topping : item.getToppings()) {
                total += topping.getPrice();
            }
        }
        return total;
    }

    private void navigateToOrders(ActionEvent event) {

    }

    public void setOrder(List<CartItem> order) {
        this.order = order;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}
