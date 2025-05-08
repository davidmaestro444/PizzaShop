package org.example.pizzashop.model;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PizzaRepository {
    private Connection connection;

    public PizzaRepository(){
        try{
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "");
        }catch(SQLException e){
            throw new RuntimeException("Nem sikerült csatlakozni az adatbázishoz", e);
        }
    }

    public BasicPizza getPizzaById(int pizzaId){
        String sql = "SELECT * FROM pizzas WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, pizzaId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new BasicPizza(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Pizza> getAllAvailablePizzas(){
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT * FROM pizzas ORDER BY name";

        try(Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                pizzas.add(new BasicPizza(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                ));
            }
        }catch (SQLException e){
            throw new RuntimeException("Hiba a pizzák betöltése során", e);
        }

        return pizzas;
    }

    public List<Topping> getAllAvailableToppings(){
        List<Topping> toppings = new ArrayList<>();
        String sql = "SELECT * FROM toppings ORDER BY category, name";

        try(Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                toppings.add(new Topping(
                        rs.getInt("topping_id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("category"),
                        rs.getBoolean("is_spicy"),
                        rs.getBoolean("is_vegetarian")
                ));
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Hiba a feltétek lekérdezése során", e);
        }

        return toppings;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
