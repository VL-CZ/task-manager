module mff.java {
    requires javafx.controls;
    requires javafx.fxml;

    opens mff.java to javafx.fxml;
    exports mff.java;
}