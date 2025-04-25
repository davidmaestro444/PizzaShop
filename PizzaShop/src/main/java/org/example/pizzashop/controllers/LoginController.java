package org.example.pizzashop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

import org.example.pizzashop.dbConnection.UserDAO;
import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.example.pizzashop.model.User;

import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final DatabaseConnection dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "");
    private final Connection connection = dbConnection.getDbConnection();
    private final UserDAO userDAO = new UserDAO(connection);

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.loginUser(username, password);

        if (user != null) {
            showAlert("Sikeres bejelentkezés!", Alert.AlertType.INFORMATION);

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/example/pizzashop/main-view.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("PizzaShop - Főoldal");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Nem sikerült betölteni a főoldalt.", Alert.AlertType.ERROR);
            }

        } else {
            showAlert("Hibás felhasználónév vagy jelszó!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Bejelentkezés");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/pizzashop/register-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Regisztráció");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Nem sikerült betölteni a regisztrációs oldalt.", Alert.AlertType.ERROR);
        }
    }
}