package com.flight.model;

public class Airplane {
    private int airplaneId;
    private int seatingCapacity;
    private int airlineCompanyId; 

    public Airplane() {
    }

    public Airplane(int airplaneId, int seatingCapacity, int airlineCompanyId) {
        this.airplaneId = airplaneId;
        this.seatingCapacity = seatingCapacity;
        this.airlineCompanyId = airlineCompanyId;
    }

    // Getters and setters
    public int getAirplaneId() {
        return airplaneId;
    }
    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }
    public int getSeatingCapacity() {
        return seatingCapacity;
    }
    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
    public int getAirlineCompanyId() {
        return airlineCompanyId;
    }
    public void setAirlineCompanyId(int airlineCompanyId) {
        this.airlineCompanyId = airlineCompanyId;
    }
} 
