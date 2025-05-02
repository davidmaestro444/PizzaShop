package org.example.pizzashop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.pizzashop.dbConnection.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private DatabaseConnection connection;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pizzashop/main-view.fxml"));
        Parent root = loader.load();
        String url = "jdbc:mysql://localhost:3306/pizzaShop";
        String username = "root";
        String password = "";

        try{
            connection = new DatabaseConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Scene scene = new Scene(root);
        primaryStage.setTitle("PizzaShop alkalmazás");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                connection.closeConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) {
        launch();
    }
}