module com.example.dataanalyticshub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ashwin.dataanalyticshub to javafx.fxml;
    exports com.ashwin.dataanalyticshub;
}