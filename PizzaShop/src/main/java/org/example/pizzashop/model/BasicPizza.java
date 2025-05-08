package org.example.pizzashop.model;

public class BasicPizza extends Pizza {
    public BasicPizza(int id, String name, String description, int price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public int getPrice(){
        return price;
    }
}
