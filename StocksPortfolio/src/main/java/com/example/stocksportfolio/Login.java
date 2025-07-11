package com.example.stocksportfolio;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.util.prefs.Preferences;
import java.io.IOException;

import static com.example.stocksportfolio.Portfolio.greet;
//import static com.example.stocksportfolio.UserSessionManager.loadUserFromPreferences;

public class Login extends Application {

    @FXML
    TextField txtUsername = new TextField();
    ;
    @FXML
    PasswordField txtPassword = new PasswordField();
    String username, password;
    static String portfolioUsername;
    static String userPassword;
    final Label message = new Label("");
    DBHandler dbHandler = new DBHandler();

//    public Login(String savedUsername, String savedPassword) {
//        this.username = username;
//        this.password = password;
//    }

    @Override
    public void start(Stage stage) throws IOException {
//        loadUserFromPreferences();
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setMaximized(true);
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
    }

    public void validateLogin(ActionEvent event) throws IOException, SQLException {
        username = txtUsername.getText();
        password = txtPassword.getText();

        FXMLLoader fxmlLoader = null;
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Username or password cannot be empty");
            errorAlert.showAndWait();
//            message.setText("Your password is incorrect!");
//            message.setTextFill(Color.rgb(210, 39, 30));
        } else {
            String hashValue = Registration.encryptPassword(txtPassword.getText());
            userPassword = hashValue;
            dbHandler.checkUserExists(username, hashValue);

            if (dbHandler.count != 1) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Incorrect username or password");
                errorAlert.showAndWait();
                txtUsername.setText("");
                txtPassword.setText("");
            } else {
//                UserSessionManager.saveUserToPreferences(username, hashValue);
//                saveUserPreferences(username,hashValue);

                portfolioUsername = username;
                System.out.println("portfolioUsername " + portfolioUsername);

                System.out.println("1 " + username);

//                greet.setText("Hello " + username + ",");
                System.out.println("Login.portfolioUsername " + username);
                proceedToPortfolio(event);

                Platform.runLater(() -> {
                    greet.setText("Hello " + username + ",");
                    System.out.println("Setting label text: " + greet.getText());
                    System.out.println(greet.isVisible());
                    greet.setVisible(true);
                });
                System.out.println("2 " + username);
            }
        }
    }


    public void proceedToRegister(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("registration.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Close the current stage
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setTitle("DBIT Stock");
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.centerOnScreen();
        newStage.setScene(scene);
        newStage.show();
    }

    public void proceedToPortfolio(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("portfolio.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
        Portfolio portfolioController = fxmlLoader.getController();
        portfolioController.displayExistingData();
    }
}