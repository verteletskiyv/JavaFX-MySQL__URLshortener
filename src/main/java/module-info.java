module com.study.urlshortener_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;


    opens com.study.urlshortener_fx to javafx.fxml;
    exports com.study.urlshortener_fx;
}