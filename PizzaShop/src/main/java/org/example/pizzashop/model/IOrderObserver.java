package org.example.pizzashop.model;

public interface IOrderObserver {
    void updateOrderStatus(Order order);
}
