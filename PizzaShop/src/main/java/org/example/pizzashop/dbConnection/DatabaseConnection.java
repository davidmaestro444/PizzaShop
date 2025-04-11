package org.example.pizzashop.dbConnection;

import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private Connection connection;
    public DatabaseConnection(String url, String username, String password){
        Stage primaryStage = new Stage();

        try{
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e){
            connection = null;

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Adatbázis csatlakozási hiba");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.exit(0);
        }
        this.setConnection(connection);
    }

    public Connection getDbConnection() {
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
    public void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch (Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Adatbázis lezárási hiba");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
