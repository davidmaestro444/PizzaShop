package org.example.pizzashop.model;

public class CashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Fizetés készpénzzel: " + amount + " forint.");
    }
}

