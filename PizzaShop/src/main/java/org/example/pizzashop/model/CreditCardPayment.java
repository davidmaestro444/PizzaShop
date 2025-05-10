package org.example.pizzashop.model;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Fizetés bankkártyával: " + amount + " forint.");
    }
}
