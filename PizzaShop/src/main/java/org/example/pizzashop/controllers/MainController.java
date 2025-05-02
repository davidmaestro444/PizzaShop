package org.example.pizzashop.controllers;

import org.example.pizzashop.dbConnection.DatabaseConnection;
import org.example.pizzashop.utils.ErrorHandler;

public class MainController {
    private DatabaseConnection dbConnection;

    public void initializeDatabase() {
        try {
            dbConnection = new DatabaseConnection(
                    "jdbc:mysql://localhost:3306/pizzashop",
                    "root",
                    ""
            );
        } catch (Exception e) {
            ErrorHandler.showErrorDialog(
                    "Adatbázis hiba",
                    "Nem sikerült csatlakozni az adatbázishoz: " + e.getMessage()
            );
        }
    }

    public void closeApplication() {
        try {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        } catch (Exception e) {
            ErrorHandler.showErrorDialog(
                    "Adatbázis hiba",
                    "Nem sikerült lezárni a kapcsolatot: " + e.getMessage()
            );
        }
    }
}