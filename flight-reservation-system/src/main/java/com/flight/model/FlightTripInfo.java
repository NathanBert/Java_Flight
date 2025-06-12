package com.flight.model;

import java.util.List;

public class FlightTripInfo {
    private FlightTrip flightTrip;
    private List<Seat> Seats;
    //private Airplane airplane;
    private List<AirlineCompany> airlineCompanies;
    private List<String> ZIPS;

    private List<Airplane> airplanes;
    //private String seatClass;
    //private boolean seatAvailable;
    private int Traveller_Id = 0;

    public FlightTripInfo(FlightTrip flightTrip, List<Seat> Seats, List<Airplane> airplanes,List<AirlineCompany> airlineCompanies, List<String> ZIPS) 
    {
        this.flightTrip = flightTrip;
        this.Seats = Seats;
        this.airplanes = airplanes;
        this.airlineCompanies = airlineCompanies;
        this.ZIPS = ZIPS;
    }
    public FlightTripInfo(FlightTrip flightTrip, List<Seat> Seats, List<Airplane> airplanes,List<AirlineCompany> airlineCompanies, List<String> ZIPS, int Traveller_Id) 
    {
        this.flightTrip = flightTrip;
        this.Seats = Seats;
        this.airplanes = airplanes;
        this.airlineCompanies = airlineCompanies;
        this.ZIPS = ZIPS;
        this.Traveller_Id = Traveller_Id;
    } 
   

    // Getters & setters
    public FlightTrip getFlightTrip() 
    {
        return flightTrip;
    }

    public void setFlightTrip(FlightTrip flightTrip) 
    {
        this.flightTrip = flightTrip;
    }

    public List<Seat> getSeats() 
    {
        return Seats;
    }
    public void setSeats(List<Seat> Seats) 
    {
        this.Seats = Seats;
    }
    public List<Airplane> getAirplanes() 
    {
        return airplanes;
    }
    public void setAirplanes(List<Airplane> airplanes) 
    {
        this.airplanes = airplanes;
    }

    public List<AirlineCompany> getAirlineCompanies() 
    {
        return airlineCompanies;
    }
    public void setAirlineCompanies(List<AirlineCompany> airlineCompanies)
    {
        this.airlineCompanies = airlineCompanies;
    }
    public List<String> getZIPS() 
    {
        return ZIPS;
    }
    public void setZIPS(List<String> ZIPS)
    {
        this.ZIPS = ZIPS;
    }
    public int getTraveller_Id() 
    {
        return Traveller_Id;
    }

    public void setTraveller_Id(int traveller_Id) 
    {
        Traveller_Id = traveller_Id;
    }

}
