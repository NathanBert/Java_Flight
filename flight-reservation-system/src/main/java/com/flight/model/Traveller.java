package com.flight.model;

import com.flight.dao.UserDAO;
import com.flight.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


import com.flight.dao.TravellerDAO;


public class Traveller {
    private int userId;
    private int seatId;
    private int FlightTripId;
    private int step;

    private static int travellerIdCounter;
    private int traveller_Id;
    // Default constructor
    public Traveller() {}
    

    public static void setUpTravellerIdCounter(User user, Connection conn) {
        try
        {
            TravellerDAO travellerDAO = new TravellerDAO(conn);
            List<Traveller> traveller = travellerDAO.findByUserId(user.getUserId());
            if (!traveller.isEmpty()) {
                travellerIdCounter = Collections.max(traveller, Comparator.comparingInt(Traveller::getTraveller_Id)).getTraveller_Id();
            } else {
                travellerIdCounter = 0; 
}        }
        catch (Exception e)
        {
            System.out.println("Error setting up traveller ID counter: " + e.getMessage());
        }

    }

    public Traveller(User user, int step )
    {
        this.step = step;
        userId = user.getUserId();
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getSeatId() {
        return seatId;
    }
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    public int getFlightTripId() {
        return FlightTripId;
    }
    public void setFlightTripId(int flightTripId) {
        FlightTripId = flightTripId;
    }

    public int getStep() {
        return step;
    }
    public void setStep(int step) {
        this.step = step;
    }


    public static int getTravellerIdCounter() {
        return travellerIdCounter;
    }
    public static void setTravellerIdCounter(int travellerIdCounter) {
        Traveller.travellerIdCounter = travellerIdCounter;
    }
    public static void incrementTravellerIdCounter() {
        Traveller.travellerIdCounter++;
    }
    public static void decrementTravellerIdCounter() {
        Traveller.travellerIdCounter--;
    }

    public int getTraveller_Id() {
        return traveller_Id;
    }
    public void setTraveller_Id(int traveller_Id) {
        this.traveller_Id = traveller_Id;
    }

}