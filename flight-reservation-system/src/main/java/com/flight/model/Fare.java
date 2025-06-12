package com.flight.model;


public class Fare {
    private int HopId;  // Référence vers Flight_Trip
    private double amount;
    private String currency;
    private double discount;
    private double tax;
    private double finalAmount;

    // Getters and setters
    public int getHopId() {
        return HopId;
    }
    public void setHopId(int HopId) {
        this.HopId = HopId;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public double getFinalAmount() {
        return finalAmount;
    }
    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }
}

