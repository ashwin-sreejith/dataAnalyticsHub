module com.example.dataanalyticshub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;


    opens com.ashwin.dataanalyticshub to javafx.fxml;
    exports com.ashwin.dataanalyticshub;
}