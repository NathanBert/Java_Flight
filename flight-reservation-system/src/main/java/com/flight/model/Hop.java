package com.flight.model;

import java.sql.Timestamp;

public class Hop {
    private int airplaneId;
    private int hopId;
    private Timestamp departTime;
    private Timestamp arrivalTime;
    private int depart;  // Référence Airport pour le départ
    private int arrive;  // Référence Airport pour l'arrivée
    private double Price;

    public int getAirplaneId() {
        return airplaneId;
    }
    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }
    public int getHopId() {
        return hopId;
    }
    public void setHopId(int hopId) {
        this.hopId = hopId;
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
    public int getDepart() {
        return depart;
    }
    public void setDepart(int depart) {
        this.depart = depart;
    }
    public int getArrive() {
        return arrive;
    }
    public void setArrive(int arrive) {
        this.arrive = arrive;
    }
    public double getPrice() {
        return Price;
    }
    public void setPrice(double price) {
        Price = price;
    }
}

