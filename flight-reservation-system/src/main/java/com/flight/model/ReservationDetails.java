package com.flight.model;

import java.sql.Timestamp;

public class ReservationDetails {
    private int userId;
    private int flightTripId;
    private String departAirportName;
    private String arrivalAirportName;
    private Timestamp departTime;
    private Timestamp arrivalTime;
    private String seatLocation;
    private String seatClass;
    private String airplaneModel; 
    private double fareAmount;
    private String currency;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlightTripId() {
        return flightTripId;
    }

    public void setFlightTripId(int flightTripId) {
        this.flightTripId = flightTripId;
    }

    public String getDepartAirportName() {
        return departAirportName;
    }

    public void setDepartAirportName(String departAirportName) {
        this.departAirportName = departAirportName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public Timestamp getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Timestamp departTime) {
        this.departTime = departTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatLocation() {
        return seatLocation;
    }

    public void setSeatLocation(String seatLocation) {
        this.seatLocation = seatLocation;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }


    public double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "🛫 Vol #" + flightTripId + " | " + departAirportName + " ➔ " + arrivalAirportName + "\n" +
               "🕓 Départ : " + departTime + " | Arrivée : " + arrivalTime + "\n" +
               "💺 Siège : " + seatLocation + " (" + seatClass + ") | Avion : " + airplaneModel + "\n" +
               "💰 Tarif : " + fareAmount + " " + currency;
    }
}
