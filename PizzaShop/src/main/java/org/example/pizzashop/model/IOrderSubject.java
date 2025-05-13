package org.example.pizzashop.model;

public interface IOrderSubject {
    void registerObserver(IOrderObserver observer);
    void removeObserver(IOrderObserver observer);
    void notifyObservers();
}
