package org.example.pizzashop.model;

public class ToppingDecorator extends BasicPizza{
    protected BasicPizza pizza;
    protected Topping topping;

    public ToppingDecorator(BasicPizza pizza, Topping topping){
        super(pizza.getId(), pizza.getName(), pizza.getDescription(), pizza.getPrice());
        this.pizza = pizza;
        this.topping = topping;
    }

    @Override
    public String getName(){
        return pizza.getName();
    }

    @Override
    public String getDescription(){
        return pizza.getDescription() + ", " + topping.getName();
    }

    @Override
    public int getPrice(){
        return pizza.getPrice() + topping.getPrice();
    }

    public String getToppingName() {
        return topping.getName();
    }

    public boolean isSpicy() {
        return topping.getIsSpicy();
    }

    public boolean isVegetarian() {
        return topping.getIsVegetarian();
    }
}
