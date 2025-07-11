package com.example.stocksportfolio;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHandler {
    private static final String dbUrl = "jdbc:postgresql://localhost/StocksPortfolio";
    private static final String user = "postgres";
    private static final String password = "pgadmin4";
    private static final String selectQuery = "select * from userInfo";
    int count;
    private static final String insertUserQuery = "insert into userInfo" + "(firstname,middlename,lastname,email,username,phone_number,password_hash_code) values " + " (?,?,?,?,?,?,?);";
    //    private static final String insertUserDataQuery = "insert into portfolio_stock" + "(name_of_stock,purpose,buying_price,quantity,buying_date,app_used,notes) values " + " (?,?,?,?,?,?,?);";
//    private static final String insertUserDataQuery = "update portfolio_stock set name_of_stock=?,purpose=?,buying_price=?,quantity=?,buying_date=?,app_used=?,notes=? where fk_portfolio_id=(select user_portfolio_id from userInfo);";
//    private static final String insertUserDataQuery = "insert into portfolio_stock" + "(fk_portfolio_id, name_of_stock, purpose, buying_price, quantity, buying_date, app_used, notes)" +"select user_portfolio_id, ?, ?, ?, ?, ?, ?, ? from userInfo where username = ?;";
//    private static final String insertUserDataQuery = "insert into portfolio_stock" + "(fk_portfolio_id,name_of_stock,purpose,buying_price,quantity,buying_date,app_used,notes) values " + " (select user_portfolio_id from userInfo where username=?),?,?,?,?,?,?,?);";
//    private static final String insertUserDataQuery = "select user_portfolio_id into userPortfolio_id from userInfo  where username = ?insert into portfolio_stock" + "(fk_portfolio_id,name_of_stock,purpose,buying_price,quantity,buying_date,app_used,notes) values " + " ((select user_portfolio_id from userInfo where username=?),?,?,?,?,?,?,?);";
    private static final String insertUserDataQuery = "insert into portfolio_stock" + "(fk_portfolio_id,name_of_stock,purpose,buying_price,quantity,buying_date,app_used,notes) values " + " ((select user_portfolio_id from userInfo where username=?),?,?,?,?,?,?,?);";
    private static final String displayData = "select name_of_stock,purpose,buying_price,quantity,buying_date,app_used,notes from portfolio_stock where fk_portfolio_id=(select user_portfolio_id from userInfo where username=?)";

    static String name_of_stock;
    static String purpose;
    static String app_used;
    static String notes;
    static int buying_price, quantity;
    static Date buying_date;
    public static int c = 0;

    public static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    public static void insertUser(String firstName, String middleName, String lastName, String emailId, String userName, String phoneNumber, String hashValue) throws SQLException {
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, middleName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, emailId);
            preparedStatement.setString(5, userName);
            preparedStatement.setString(6, phoneNumber);
            preparedStatement.setString(7, hashValue);
            preparedStatement.executeUpdate();
        }
    }

    public static void queryUser() throws SQLException {
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("firstname");
//                portfolioController.setUserName(firstName);
                String middleName = resultSet.getString("middlename");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String userName = resultSet.getString("username");
                String phoneNumber = resultSet.getString("phone_number");
                String password_hash_value = resultSet.getString("password_hash_code");
                System.out.println("First Name: " + firstName);
                System.out.println("Middle Name: " + middleName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Username: " + userName);
                System.out.println("Phone number: " + phoneNumber);
                System.out.println("Password hash value: " + password_hash_value);
                System.out.println();
            }
        }
    }

    public void checkUserExists(String username, String hashValue) {
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement("select username from userInfo where username='" + username + "'" + " and password_hash_code='" + hashValue + "';")) {
            ResultSet resultSet = preparedStatement.executeQuery();
//            System.out.println("1");
            while (resultSet.next()) {
//                String loginUsername = resultSet.getString("username");
//                System.out.println(loginUsername);
                count++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertUserData(String username, String nameOfStock, String purpose, String buyingPrice, String quantity, Date buyingDate, String appUsed, String notes) throws SQLException {
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserDataQuery)) {
            System.out.println(username);
            preparedStatement.setString(1, username);
            System.out.println(username);
            preparedStatement.setString(2, nameOfStock);
            preparedStatement.setString(3, purpose);
            preparedStatement.setString(4, buyingPrice);
            preparedStatement.setString(5, quantity);
            preparedStatement.setDate(6, buyingDate);
            preparedStatement.setString(7, appUsed);
            preparedStatement.setString(8, notes);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteRecord(String nameOfStock, String purpose, Date buyingDate, String username) throws SQLException {
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from portfolio_stock " +
                             "where id in (select p.id from portfolio_stock p " +
                             "join userInfo u on p.fk_portfolio_id = u.user_portfolio_id " +
                             "where p.name_of_stock = ? " +
                             "and p.purpose = ? " +
                             "and p.buying_date = cast(? as date) " +
                             "and u.username = ?)")) {
            preparedStatement.setString(1, nameOfStock);
            preparedStatement.setString(2, purpose);
            preparedStatement.setDate(3, new java.sql.Date(buyingDate.getTime()));
            preparedStatement.setString(4, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[][] displayData(String username){
        String[][] myList = new String[1000][7];
        try (Connection connection = connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(displayData)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                name_of_stock=resultSet.getString("name_of_stock");
                purpose=resultSet.getString("purpose");
                buying_price=resultSet.getInt("buying_price");
                quantity=resultSet.getInt("quantity");
                System.out.println("buying date: ");
                buying_date=resultSet.getDate("buying_date");
                System.out.println(buying_date);
                app_used=resultSet.getString("app_used");
                notes=resultSet.getString("notes");
                myList[c][0]=name_of_stock;
                myList[c][1]=purpose;
                myList[c][2]=String.valueOf(buying_price);
                myList[c][3]=String.valueOf(quantity);
                myList[c][4]=String.valueOf(buying_date);
                myList[c][5]=app_used;
                myList[c][6]=notes;
                System.out.println("Inserting records");
                c++;
//                result.add(new PortfolioDetail(Portfolio.rowNumber,name_of_stock,purpose,buying_price,quantity, buying_date,app_used,notes));
//                portfolioController.displayExistingData();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return myList;
    }
}
