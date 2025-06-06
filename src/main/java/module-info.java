module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;

    opens com to javafx.fxml;
    opens com.view to javafx.fxml;

    exports com;
    exports com.view;
}
