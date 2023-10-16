module com.example.dataanalyticshub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.ashwin.dataanalyticshub to javafx.fxml;
    opens com.ashwin.dataanalyticshub.datamodel to javafx.base;
    exports com.ashwin.dataanalyticshub;
}
