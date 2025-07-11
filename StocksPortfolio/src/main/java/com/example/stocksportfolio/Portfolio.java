package com.example.stocksportfolio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Portfolio {
    @FXML
    public static Label greet = new Label();
    @FXML
    Button portfolio_details = new Button();
    @FXML
    TextField nameOfStock = new TextField();
    @FXML
    TextField purpose = new TextField();
    @FXML
    TextField buyingPrice = new TextField();
    @FXML
    TextField quantity = new TextField();
    @FXML
    DatePicker buyingDate = new DatePicker();
    @FXML
    TextField appUsed = new TextField();
    @FXML
    TextField notes = new TextField();
    @FXML
    private TableView<PortfolioDetail> perscTable; // Assuming PortfolioDetail is a class representing data

    @FXML
    private TableColumn<PortfolioDetail, String> getnameOfStock;
    @FXML
    private TableColumn<PortfolioDetail, String> getpurpose;
    @FXML
    private TableColumn<PortfolioDetail, String> getbuyingPrice;
    @FXML
    private TableColumn<PortfolioDetail, String> getquantity;
    @FXML
    private TableColumn<PortfolioDetail, Date> getbuyingDate;
    @FXML
    private TableColumn<PortfolioDetail, String> applicationTrading;
    @FXML
    private TableColumn<PortfolioDetail, String> getnotes;

    private ObservableList<PortfolioDetail> portfolioData = FXCollections.observableArrayList();
    FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
    static int rowNumber;

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Portfolio.class.getResource("portfolio.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMaximized(true);
        stage.setTitle("DBIT Stock");
        stage.setScene(scene);
        stage.show();
//        Login controller = fxmlLoader.getController();
    }

//    public void initialize() {
//    }

//    public void setUserName(String userName) {
//        greet.setText("Hello " + userName + ",");
//    }

    public void insertUserData() throws SQLException {
        if (nameOfStock.getText().isEmpty() || purpose.getText().isEmpty() || quantity.getText().isEmpty() || appUsed.getText().isEmpty() || notes.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Please enter details");
            errorAlert.showAndWait();
        } else {
//            Login controller = fxmlLoader.getController();
            System.out.println("Login.portfolioUsername " + Login.portfolioUsername);
            DBHandler.insertUserData(Login.portfolioUsername, nameOfStock.getText(), purpose.getText(), buyingPrice.getText(), quantity.getText(), Date.valueOf(buyingDate.getValue()), appUsed.getText(), notes.getText());
            insertRow();
        }
    }

    public void logout(ActionEvent event) throws IOException {
//        UserSessionManager.clearUserPreferences();
        FXMLLoader fxmlLoader = new FXMLLoader(Portfolio.class.getResource("login.fxml"));
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

    public void insertRow() {
        rowNumber = perscTable.getItems().size() + 1;
        PortfolioDetail detail = new PortfolioDetail(
                rowNumber,
                nameOfStock.getText(),
                purpose.getText(),
                buyingPrice.getText(),
                quantity.getText(),
                Date.valueOf(buyingDate.getValue()),
                appUsed.getText(),
                notes.getText()
        );

        // Create a new ObservableList with the new item
        ObservableList<PortfolioDetail> items = FXCollections.observableArrayList(detail);

//        int rowNumber = perscTable.getItems().size() + 1;
//        detail.setRowNumber(rowNumber);

        // Add the new item to the existing items in the TableView
        perscTable.getItems().addAll(items);

        // Clear the input fields
        nameOfStock.clear();
        purpose.clear();
        buyingPrice.clear();
        quantity.clear();
        buyingDate.getEditor().clear();
        appUsed.clear();
        notes.clear();
    }

//    public void deleteRow() throws SQLException {
//        PortfolioDetail selectedItem = perscTable.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            DBHandler.deleteRecord(rowNumber,Login.portfolioUsername);
//            perscTable.getItems().remove(selectedItem);
//        } else {
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setHeaderText("Please select a row to delete.");
//            errorAlert.showAndWait();
//        }
//    }

    public void deleteRow() throws SQLException {
        PortfolioDetail selectedItem = perscTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String nameOfStock = selectedItem.getNameOfStock();
            String purpose = selectedItem.getPurpose();
            Date buyingDate = selectedItem.getBuyingDate();
            System.out.println(nameOfStock + purpose + buyingDate);
            DBHandler.deleteRecord(nameOfStock, purpose, buyingDate, Login.portfolioUsername);
            perscTable.getItems().remove(selectedItem);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Please select a row to delete.");
            errorAlert.showAndWait();
        }
    }

    public void displayExistingData() throws SQLException {
        System.out.println("Login.portfolioUsername " + Login.portfolioUsername);
        String username = Login.portfolioUsername;
        System.out.println(username);

        String[][] myList = DBHandler.displayData(username);

        if (myList != null) {
            for (String[] record : myList) {
                rowNumber = perscTable.getItems().size() + 1;

                // Extract values from the record
                String nameOfStock = record[0];
                String purpose = record[1];
                String buyingPrice = record[2];
                String quantity = record[3];
                String buyingDateStr = record[4];
                String appUsed = record[5];
                String notes = record[6];

                if (nameOfStock != null && purpose != null && buyingPrice != null && quantity != null && buyingDateStr != null && appUsed != null && notes != null) {
                    try {
                        double buyingPriceValue = Double.parseDouble(buyingPrice);
                        int quantityValue = Integer.parseInt(quantity);
                        Date buyingDate = Date.valueOf(buyingDateStr);

                        // Add valid data to the table
                        rowNumber = perscTable.getItems().size() + 1;
                        perscTable.getItems().add(new PortfolioDetail(rowNumber, nameOfStock, purpose, String.valueOf(buyingPriceValue), String.valueOf(quantityValue), buyingDate, appUsed, notes));
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }
}
