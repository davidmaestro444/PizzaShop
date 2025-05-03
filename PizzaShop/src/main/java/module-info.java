module org.example.pizzashop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.example.pizzashop to javafx.fxml;
    opens org.example.pizzashop.controllers to javafx.fxml;
    opens org.example.pizzashop.utils to javafx.fxml;
    opens org.example.pizzashop.images to javafx.fxml, javafx.graphics;

    exports org.example.pizzashop.dbConnection;
    exports org.example.pizzashop.utils;
    exports org.example.pizzashop;
    exports org.example.pizzashop.controllers;
}