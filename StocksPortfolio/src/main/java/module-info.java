module com.example.stocksportfolio {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.prefs;

    opens com.example.stocksportfolio to javafx.fxml;
    opens com.example.stocksportfolio.splashscreen to javafx.fxml;
    exports com.example.stocksportfolio;
    exports com.example.stocksportfolio.splashscreen;
}