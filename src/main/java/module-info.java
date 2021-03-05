module mff.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.common;
    requires sqlite.jdbc;

    opens mff.java to javafx.fxml;
    exports mff.java;
}