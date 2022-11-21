module org.enstabretagne {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.enstabretagne to javafx.fxml;
    exports org.enstabretagne;
}
