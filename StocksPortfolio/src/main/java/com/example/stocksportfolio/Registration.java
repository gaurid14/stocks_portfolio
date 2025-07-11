package com.example.stocksportfolio;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Application {


    @FXML
    TextField firstName = new TextField();
    @FXML
    TextField middleName = new TextField();
    @FXML
    TextField lastName = new TextField();
    @FXML
    TextField emailId = new TextField();
    @FXML
    TextField username = new TextField();
    static String userName;
    @FXML
    PasswordField password = new PasswordField();
    @FXML
    PasswordField confirmPassword = new PasswordField();
    @FXML
    TextField phoneNumber = new TextField();
    String emailRegEx = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    String passwordRegEx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    static String hashValue;

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setFullScreen(true);
//        stage.setIconified(true);
        stage.setTitle("DBIT Stock");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        phoneNumber.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]") || phoneNumber.getText().length() >= 10) {
                event.consume();
            }
        });
    }

    public void signUp(ActionEvent event) throws IOException, SQLException {
        Pattern passwordPattern = Pattern.compile(passwordRegEx);
        Pattern emailPattern = Pattern.compile(emailRegEx);
        Matcher passwordMatcher = passwordPattern.matcher(password.getText());
        Matcher emailMatcher = emailPattern.matcher(emailId.getText());

        if (firstName.getText().isEmpty() || middleName.getText().isEmpty() || lastName.getText().isEmpty() || emailId.getText().isEmpty() || username.getText().isEmpty() || phoneNumber.getText().isEmpty() || password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
//            System.out.println("1");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("All fields are mandatory");
            errorAlert.showAndWait();
//            message.setText("Your password is incorrect!");
//            message.setTextFill(Color.rgb(210, 39, 30));
        } else if (!passwordMatcher.matches()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Password should contain atleast one digit(0-9).one lowercase and one uppercase letter and special symbol");
            errorAlert.showAndWait();
            password.setText("");
            confirmPassword.setText("");
        } else if (!emailMatcher.matches()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid email");
            errorAlert.showAndWait();
            emailId.setText("");
        } else if (!Objects.equals(password.getText(), confirmPassword.getText())) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Password doesn't match");
            errorAlert.showAndWait();
//            confirmPassword.setText("");
        } else {
//            System.out.println(hashValue);
            userName = username.getText();

            hashValue = encryptPassword(password.getText());

            try {
                // Attempt to insert the user
                DBHandler.insertUser(firstName.getText(), middleName.getText(), lastName.getText(), emailId.getText(), userName, phoneNumber.getText(), hashValue);
                // Successful insertion, proceed
                FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close(); // Close the current stage
                Stage newStage = new Stage();
                Scene scene = new Scene(root);
                newStage.setScene(scene);
//                newStage.setMaximized(true);
                newStage.setTitle("DBIT Stock");
                newStage.show();

                DBHandler.queryUser();
//                DBHandler.createPortfolio(firstName.getText());
            } catch (SQLException e) {
                if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                    // Handle the unique constraint violation (username already exists) error
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Username already exists. Please choose a different username.");
                    errorAlert.showAndWait();
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String encryptPassword(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] md = messageDigest.digest(input.getBytes());
            BigInteger no = new BigInteger(1, md);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
