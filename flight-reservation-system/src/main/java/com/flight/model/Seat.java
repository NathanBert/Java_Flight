package com.flight.model;

public class Seat {
    private int seatId;
    private boolean availability;
    private String location;
    private String seatClass; 
    private int airplaneId;

    public Seat() {
    }

    public Seat(int seatId, boolean availability, String location, String seatClass, int airplaneId) {
        this.seatId = seatId;
        this.availability = availability;
        this.location = location;
        this.seatClass = seatClass;
        this.airplaneId = airplaneId;
    }

    public int getSeatId() {
        return seatId;
    }
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    public boolean isAvailability() {
        return availability;
    }
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
    public Boolean getAvailability() {
        return availability;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getSeatClass() {
        return seatClass;
    }
    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
    public int getAirplaneId() {
        return airplaneId;
    }
    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }
}