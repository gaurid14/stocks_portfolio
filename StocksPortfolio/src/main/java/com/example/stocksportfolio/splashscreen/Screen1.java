package com.example.stocksportfolio.splashscreen;

import com.example.stocksportfolio.DBHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Screen1 extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Screen1.class.getResource("screen1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setFullScreen(true);
//        stage.setIconified(true);
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
    }

    public void proceedToScreen2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Screen1.class.getResource("screen2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("DBIT Stock");
        stage.centerOnScreen();
//        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
        DBHandler.connectDB();
    }
}
