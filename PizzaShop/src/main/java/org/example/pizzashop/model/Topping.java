package org.example.pizzashop.model;

public class Topping {
    private int id;
    private String name;
    private int price;
    private String category;
    private boolean isSpicy;
    private boolean isVegetarian;

    public Topping(int id, String name, int price, String category, boolean isSpicy, boolean isVegetarian){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.isSpicy = isSpicy;
        this.isVegetarian = isVegetarian;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean getIsSpicy() { return isSpicy; }
    public boolean getIsVegetarian() { return isVegetarian; }
}
