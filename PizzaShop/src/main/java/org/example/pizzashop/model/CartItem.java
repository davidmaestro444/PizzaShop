package org.example.pizzashop.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.pizzashop.model.BasicPizza;
import org.example.pizzashop.model.Topping;

import java.util.List;
import java.util.stream.Collectors;

public class CartItem {
    private final Pizza pizza;
    private final ObservableList<Topping> toppings = FXCollections.observableArrayList();

    public CartItem(Pizza pizza) {
        this.pizza = pizza;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void addToppings(List<Topping> topping) {
        toppings.addAll(topping);
    }

    public void addToppings(Topping topping) {
        toppings.addAll(topping);
    }

    public String getPizzaName() { return pizza.getName(); }
    public int getBasePrice() { return pizza.getPrice(); }
    public ObservableList<Topping> getToppings() { return toppings; }

    public int getTotalPrice() {
        return pizza.getPrice() + toppings.stream().mapToInt(Topping::getPrice).sum();
    }

    public String getToppingsList() {
        return toppings.stream()
                .map(Topping::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Nincs feltét");
    }

    public String getToppingsFormatted() {
        return toppings.stream()
                .map(Topping::getName)
                .collect(Collectors.joining(", "));
    }
}