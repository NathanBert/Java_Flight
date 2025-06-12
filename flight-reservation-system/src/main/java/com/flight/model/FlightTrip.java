package com.flight.model;

import com.flight.dao.AirportDAO;
import com.flight.dao.FlightTripDAO;
import com.flight.dao.HopDAO;
import com.flight.dao.SeatDAO;
import com.flight.model.DBConnector;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class FlightTrip {
    private int flightTripId;
    private double distance;
    //private List<Integer> noTravellers = new ArrayList<>();
    private Timestamp departTime;
    private Timestamp arrivalTime;
    private int departAirport;   // Reference to Airport
    private int arrivalAirport;  // Reference to Airport
    public static int numFlights = 0;
    private double price;

    private List<Hop> hops = new ArrayList<Hop>(); // List of hops for this flight trip


     

    public FlightTrip() 
    {
    }

    public FlightTrip(int flightTripId, 
                      double distance, 
                      //List<Integer> noTravellers, 
                      Timestamp departTime, 
                      Timestamp arrivalTime, 
                      int departAirport, 
                      int arrivalAirport,
                      List<Hop> hops,
                      double price) 
    {
        this.flightTripId = flightTripId;
        this.distance = distance;
        //this.noTravellers = noTravellers;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
        this.departAirport = departAirport;
        this.arrivalAirport = arrivalAirport;
        this.hops = hops;
        this.price = price;
    }

    public FlightTrip(int flightTripId, 
                     List<Integer> noTravellers, 
                     Timestamp departTime, 
                     Timestamp arrivalTime, 
                     int departAirport, 
                     int arrivalAirport,
                     List<Hop> hops,
                     double price) 
    {
        this.flightTripId = flightTripId;
        //this.noTravellers = noTravellers;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
        this.departAirport = departAirport;
        this.arrivalAirport = arrivalAirport;
        this.hops = hops;
        this.price = price;

        try {
            Connection conn = DBConnector.getConnection();
            AirportDAO airportDAO = new AirportDAO(conn); 
            Airport AAirport = airportDAO.findById(arrivalAirport);
            Airport DAirport = airportDAO.findById(departAirport);
            
            // Calculate distance between airports using Haversine formula
            final int EARTH_RADIUS = 6371; // Earth's radius in kilometers
            
            double latDistance = Math.toRadians(AAirport.getLatitude() - DAirport.getLatitude());
            double lonDistance = Math.toRadians(AAirport.getLongitude() - DAirport.getLongitude());
            
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(DAirport.getLatitude())) * Math.cos(Math.toRadians(AAirport.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            this.distance = EARTH_RADIUS * c; // Distance in kilometers
        } catch (Exception e)
        {
            // Handle the exception appropriately, perhaps log it
            e.printStackTrace();
            // Set a default distance or rethrow as a runtime exception if needed
            this.distance = 0;
        }
    }
    
    /*
    public static void findNoTravellers(List<FlightTrip> flightTrips) throws Exception 
    
    
    {
        Connection conn = DBConnector.getConnection();
        SeatDAO seatDAO = new SeatDAO(conn);
        for (FlightTrip flightTrip : flightTrips) 
        {
            for (int i = 0; i < flightTrip.getHops().size(); i++) 
            {
                int airPlaneId = flightTrip.getHops().get(i).getAirplaneId();
                int noTravellers = seatDAO.getNumberOfAvailableSeats(airPlaneId);
                flightTrip.getNoTravellers().add(noTravellers);
            }
        }
    }   
    */


    public int getFlightTripId() 
    {
        return flightTripId;
    }
    public void setFlightTripId(int flightTripId) 
    {
        this.flightTripId = flightTripId;
    }
    public double getDistance() 
    {
        return distance;
    }
    public void setDistance(double distance) 
    {
        this.distance = distance;
    }
    /*public List<Integer> getNoTravellers() 
    {
        return noTravellers;
    }
    public void setNoTravellers(List<Integer> noTravellers) 
    {
        this.noTravellers = noTravellers;
    }*/
    public Timestamp getDepartTime() 
    {
        return departTime;
    }
    public void setDepartTime(Timestamp departTime) 
    {
        this.departTime = departTime;
    }
    public Timestamp getArrivalTime() 
    {
        return arrivalTime;
    }
    public void setArrivalTime(Timestamp arrivalTime) 
    {
        this.arrivalTime = arrivalTime;
    }
    public int getDepartAirport() 
    {
        return departAirport;
    }
    public void setDepartAirport(int departAirport) 
    {
        this.departAirport = departAirport;
    }
    public int getArrivalAirport() 
    {
        return arrivalAirport;
    }
    public void setArrivalAirport(int arrivalAirport) 
    {
        this.arrivalAirport = arrivalAirport;
    }
    public List<Hop> getHops() 
    {
        return hops;
    }
    public void setHops(List<Hop> hops) 
    {
        this.hops = hops;
    }

    public static int getNumFlights() 
    {
        return numFlights;
    }
    public static void setNumFlights(int numFlights) 
    {
        FlightTrip.numFlights = numFlights;
    }
    public static void incrementNumFlights() 
    {
        FlightTrip.numFlights++;
    }
    public static void decrementNumFlights() 
    {
        FlightTrip.numFlights--;
    }
    public static void setNumFlight(Connection conn)
    {
        
        try {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            List<FlightTrip> list = flightTripDAO.findAll();
            numFlights = list.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
    }

    public double getPrice() 
    {
        return price;
    }

    public void setPrice(double price) 
    {
        this.price = price;
    }

    

}
 