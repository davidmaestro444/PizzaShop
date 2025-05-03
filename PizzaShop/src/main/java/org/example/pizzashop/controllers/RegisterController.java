package org.example.pizzashop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.example.pizzashop.dbConnection.UserDAO;
import org.example.pizzashop.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    private final DatabaseConnection dbConnection;

    {
        try {
            dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3306/pizzaShop", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final Connection connection = dbConnection.getDbConnection();
    private final UserDAO userDAO = new UserDAO(connection);

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Minden mezőt ki kell tölteni!", Alert.AlertType.WARNING);
            return;
        }

        User newUser = new User(username, password, email);
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            showAlert("Sikeres regisztráció!", Alert.AlertType.INFORMATION);
            goToLogin(event);
        } else {
            showAlert("Nem sikerült regisztrálni.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/pizzashop/fxml/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bejelentkezés");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Nem sikerült betölteni a bejelentkezési oldalt.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Regisztráció");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
