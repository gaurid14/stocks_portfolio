package com.example.stocksportfolio;

import java.sql.Date;

public class PortfolioDetail {
    private int rowNumber;
    private String nameOfStock;
    private String purpose;
    private String buyingPrice;
    private String quantity;
    private Date buyingDate;
    private String appUsed;
    private String notes;

    public PortfolioDetail(int rowNumber,String nameOfStock, String purpose, String buyingPrice, String quantity, Date buyingDate, String appUsed, String notes) {
        this.rowNumber = rowNumber;
        this.nameOfStock = nameOfStock;
        this.purpose = purpose;
        this.buyingPrice = buyingPrice;
        this.quantity = quantity;
        this.buyingDate = buyingDate;
        this.appUsed = appUsed;
        this.notes = notes;
    }

    // Getters and setters for your properties

    public String getNameOfStock() {
        return nameOfStock;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public String getAppUsed() {
        return appUsed;
    }

    public String getNotes() {
        return notes;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
