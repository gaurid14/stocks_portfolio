package com.example.stocksportfolio.splashscreen;

import com.example.stocksportfolio.Login;
import com.example.stocksportfolio.Portfolio;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Screen2 extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Screen2.class.getResource("screen2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setFullScreen(true);
//        stage.setIconified(true);
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
    }

    public void proceedToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Screen2.class.getResource("/com/example/stocksportfolio/login.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Close the current stage
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
//        newStage.setMaximized(true);
        newStage.setTitle("DBIT Stock");
        newStage.show();
    }
    public void proceedToRegister(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Screen2.class.getResource("/com/example/stocksportfolio/registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
    }
}
