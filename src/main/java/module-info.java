module mff.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens mff.java to javafx.fxml;
    exports mff.java;
}