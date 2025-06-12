package com.flight.model;

public class Airport 
{
    private int airportId;
    private String airportName;
    private String zip;
    private double latitude;
    private double longitude;
    private String city;
    private String country;

    public int getAirportId()
    {
        return airportId;
    }

    public String getZip()
    {
        return zip;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public String getAirportName()
    {
        return airportName;
    }

    public String getCity()
    {
        return city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setAirportId(int newAirportId)
    {
        airportId = newAirportId; 
    }

    public void setZip(String newZip)
    {
        zip = newZip; 
    }

    public void setLatitude(double newLatitude)
    {
        latitude = newLatitude; 
    }

    public void setLongitude(double newLongitude)
    {
        longitude = newLongitude; 
    }

    public void setAirportName(String newAirportName)
    {
        airportName = newAirportName; 
    }

    public void setCity(String newCity)
    {
        city = newCity; 
    }

    public void setCountry(String newCountry)
    {
        country = newCountry; 
    }

    public Airport() 
    {
    }

    public Airport(int airportId, 
                   String airportName, 
                   String zip, 
                   double latitude, 
                   double longitude, 
                   String city, 
                   String country) 
    {
        this.airportId = airportId;
        this.airportName = airportName;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

}



