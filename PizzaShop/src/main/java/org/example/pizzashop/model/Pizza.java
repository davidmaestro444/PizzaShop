package org.example.pizzashop.model;

import java.util.List;

public abstract class Pizza {
    protected int id;
    protected String name;
    protected String description;
    protected int price;

    public abstract int getId();
    public abstract String getName();
    public abstract String getDescription();
    public abstract int getPrice();
}
