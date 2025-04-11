module org.example.pizzashop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.pizzashop to javafx.fxml;
    exports org.example.pizzashop;
    exports org.example.pizzashop.controllers;
    opens org.example.pizzashop.controllers to javafx.fxml;
}