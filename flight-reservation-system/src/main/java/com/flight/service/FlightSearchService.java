package com.flight.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.flight.model.Airplane;
import com.flight.model.Airport;
import com.flight.model.DBConnector;
import com.flight.model.Fare;
import com.flight.model.FlightTrip;
import com.flight.dao.AirplaneDAO;
import com.flight.dao.AirportDAO;
import com.flight.dao.FareDAO;
import com.flight.dao.FlightTripDAO;
import com.flight.dao.HopDAO;
import com.flight.dao.SeatDAO;
import com.flight.model.FlightTripInfo;
import com.flight.model.Seat;
import com.flight.model.Hop;
import com.flight.model.Seat;





public class FlightSearchService {
    String arrivalAirportName;
    String departureAirportName;

    Timestamp arrivalDate;
    Timestamp departureDate;

    int numberOfPassengers;
    String flightClass;

    Connection conn;

    public FlightSearchService(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * Methode that retrieve with the informations given in argument the List of flight, 
     * and associated Seats and Airplanes that correspond to the Flight
     * @return A list of FlightTripInfo if Flight correspond, Null otherwise
    */
    public List<FlightTrip> searchFlights(int arrivalAirportId, 
                                          int departureAirportId,
                                          Timestamp arrivalDate, 
                                          Timestamp departureDate, 
                                          int numberOfPassengers, 
                                          String flightClass) 
    {
        List<FlightTrip> flightTrips = new ArrayList<FlightTrip>();  
        List<FlightTrip> enrichedFlights = new ArrayList<>();

        try
        {
            HopDAO hopDAO = new HopDAO(conn);
            SeatDAO seatDAO = new SeatDAO(conn);
            AirplaneDAO airplaneDAO = new AirplaneDAO(conn);
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            FareDAO fareDAO = new FareDAO(conn);



            flightTrips =  FlightTripDAO.buildFlightTrips(
            departureAirportId,
            arrivalAirportId,
            3,
            conn);




            System.out.println("FlightTrip size after build: " + flightTrips.size());

            flightTrips = filterFlights(flightTrips, numberOfPassengers, flightClass, arrivalDate, departureDate);



            for (FlightTrip flightTrip : flightTrips) {
                double price = 0;
                for (Hop hop : flightTrip.getHops()) {
                    price += fareDAO.findById(hop.getHopId()).getFinalAmount();
                }

                flightTrip.setPrice(price);
                System.out.println("FlightTrip price: " + flightTrip.getPrice());

                flightTripDAO.insert(flightTrip);

                // Ajouter une nouvelle instance enrichie si nécessaire
                FlightTrip fullTrip = flightTripDAO.findByInfo(flightTrip);
                enrichedFlights.add(fullTrip);
            }

            // Remplacer l’ancienne liste par la nouvelle
            flightTrips = enrichedFlights;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        if (flightTrips.size()>0)
        {
            return flightTrips; 
        }
        else
        {
            return null;
        }
    }


    public List<FlightTrip> filterFlights(List<FlightTrip> flightTrips, 
                                          int numberOfPassengers, 
                                          String flightClass,
                                          Timestamp arrivalDate, 
                                          Timestamp departureDate) throws Exception
    {
        
        List<FlightTrip> filteredFlights = new ArrayList<>();

        for (FlightTrip flightTrip : flightTrips) 
        {


            LocalDate flightDepartureDate = flightTrip.getHops().get(0).getDepartTime().toLocalDateTime().toLocalDate();
            LocalDate filterDepartureDate = departureDate != null ? departureDate.toLocalDateTime().toLocalDate() : null;

            LocalDate flightArrivalDate = flightTrip.getHops().get(flightTrip.getHops().size() - 1).getArrivalTime().toLocalDateTime().toLocalDate();
            LocalDate filterArrivalDate = arrivalDate != null ? arrivalDate.toLocalDateTime().toLocalDate() : null;


            boolean departureDateCheck = filterDepartureDate == null || flightDepartureDate.equals(filterDepartureDate);
            boolean arrivalDateCheck = filterArrivalDate == null || flightArrivalDate.equals(filterArrivalDate);

            if (departureDateCheck && arrivalDateCheck) {
                filteredFlights.add(flightTrip);
            }
        
     
        }

        return filteredFlights;
    }




    /**
     * Methode that retrieve with the informations given in argument the List of flight, 
     * and associated Seats and Airplanes that correspond to the Flight
     * @return A list of FlightTripInfo if Flight correspond, Null otherwise
    */
    public List<FlightTrip> searchFlightsName(String arrivalAirportName, 
                                          String departureAirportName,
                                          Timestamp arrivalDate, 
                                          Timestamp departureDate, 
                                          int numberOfPassengers, 
                                          String flightClass) 
    {

        
        List<FlightTrip> flightTrip = new ArrayList<FlightTrip>();

        try
        {
            AirportDAO airportDAO = new AirportDAO(conn);
            Airport arrivalAirport = airportDAO.findByName(arrivalAirportName);
            Airport departureAirport = airportDAO.findByName(departureAirportName);
            flightTrip = searchFlights(arrivalAirport.getAirportId(), 
                                           departureAirport.getAirportId(), 
                                           arrivalDate, 
                                           departureDate, 
                                           numberOfPassengers, 
                                           flightClass);

            
          
          
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return flightTrip;
    
    }

/*
    public List<FlightTripInfo> searchFlightByUserId(int userId) 
    {
        List<FlightTripInfo> flightTripInfos = new ArrayList<>();
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            List<FlightTrip> flightTrips = flightTripDAO.findByUserId(userId);

            for (FlightTrip flightTrip : flightTrips) 
            {
                Airplane airplane = new AirplaneDAO(conn).findById(flightTrip.getAirplaneId());
                List<Seat> availableSeats = new SeatDAO(conn).findAvailableSeats(flightTrip.get());
                FlightTripInfo flightTripInfo = new FlightTripInfo(flightTrip, availableSeats, airplane);
                flightTripInfos.add(flightTripInfo);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return flightTripInfos;
    }*/


    public List<String> getAllAirportsName()
    {
        List<String> airportNames = new ArrayList<>();
        try 
        {
            AirportDAO airportDAO = new AirportDAO(conn);
            airportNames = airportDAO.findAllNamesAirports();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return airportNames;
    }
    

    public int getAirportIdByName(String airportName) 
    {
        int airportId = -1;
        try 
        {
            AirportDAO airportDAO = new AirportDAO(conn);
            Airport airport = airportDAO.findByName(airportName);
            if (airport != null) 
            {
                airportId = airport.getAirportId();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return airportId;
    }


    public String getAirportNamebyId(int airportId) 
    {
        String airportName = null;
        try 
        {
            AirportDAO airportDAO = new AirportDAO(conn);
            Airport airport = airportDAO.findById(airportId);
            if (airport != null) 
            {
                airportName = airport.getAirportName();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return airportName;
    }


    public void CreateFlightTripFromSingleHop(Hop hop) 
    {
        FlightTrip flightTrip = new FlightTrip();

        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            FareDAO fareDAO = new FareDAO(conn);
        
            flightTrip.setHops(new ArrayList<>());
            flightTrip.getHops().add(hop);
            flightTrip.setDepartAirport(hop.getDepart());
            flightTrip.setArrivalAirport(hop.getArrive());
            flightTrip.setDepartTime(hop.getDepartTime());
            flightTrip.setArrivalTime(hop.getArrivalTime());
            flightTrip.setPrice(fareDAO.findById(hop.getHopId()).getFinalAmount());
            flightTripDAO.insert(flightTrip);
        
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public FlightTrip getFlightTripByHop(Hop hop) 
    {
        FlightTrip flightTrip = null;
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            System.out.println("\n Passé ici 1\n");

            flightTrip = flightTripDAO.findSingletonFlightTripHop(hop);

            if (flightTrip == null) 
            {
                System.out.println("\n Passé ici 3\n");

                CreateFlightTripFromSingleHop(hop);
                flightTrip = flightTripDAO.findSingletonFlightTripHop(hop);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return flightTrip;
    }

    public int getAirplaneIdByFlightTrip(FlightTrip flightTrip, int step) 
    {
        int airplaneId = -1;
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            Hop hop = hopDAO.findByHopid(flightTrip.getHops().get(step).getHopId());
            airplaneId = hop.getAirplaneId();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return airplaneId;
    }



    public List<FlightTrip> getNonSingletonFlightTrips(int departure, int arrival, String date, double maxPrice, int maxSeats) 
    {
        List<FlightTrip> flightTrips = new ArrayList<>();
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            flightTrips = flightTripDAO.findNonSingletonFlightTrips(departure, arrival, date, maxPrice, maxSeats);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return flightTrips;
    }


    public List<Integer> getAllSeatsForFlightTrip(FlightTrip flightTrip) 
    {
        List<Integer> seatIds = new ArrayList<>();
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            seatIds = flightTripDAO.getSeatsForFlightTrip(flightTrip.getFlightTripId());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return seatIds;
    }

    public List<Integer> getAllSeatsForHop(Hop hop) 
    {
        List<Integer> seatIds = new ArrayList<>();
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            seatIds = hopDAO.getSeatsForHop(hop.getHopId());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return seatIds;
    }

    public int getSeatIdByHop(Hop hop, String location) 
    {
        int seatId = -1;
        try 
        {
            SeatDAO seatDAO = new SeatDAO(conn);
            seatId = seatDAO.getSeatIdByLocation(location, hop.getAirplaneId());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();

            
        }
        return seatId;
    }




    public List<Hop> getAllHops(String date, int maxSeat, double maxPrice, int departure, int arrival) 
    {
        List<Hop> hops = new ArrayList<>();
        LocalDate targetDate = LocalDate.parse(date.substring(0, 10)); // "2025-06-11"

        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            hops = hopDAO.findAll();

            if (!hops.isEmpty()) {
                Iterator<Hop> iterator = hops.iterator();
                while (iterator.hasNext()) {
                    Hop hop = iterator.next();

                    LocalDate hopDate = hop.getDepartTime().toLocalDateTime().toLocalDate();

                    System.out.println("hop date :"+hopDate.toString() +" ?= "+targetDate.toString());
                    boolean sameDate = hopDate.equals(targetDate);                    
                    boolean notEnoughSeats = (getSeatsNumberHop(hop) < maxSeat);
                    boolean isValidDeparture = (hop.getDepart() == departure);
                    boolean isValidArrival = (hop.getArrive() == arrival);
                    boolean isValidPrice = (hop.getPrice() <= maxPrice);
                    if (!isValidDeparture || !isValidArrival || !isValidPrice || !sameDate || !notEnoughSeats) {
                        iterator.remove();
                    }

                }
            }


            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return hops;
    }




    public double getMaxPriceFlightTripHop()
    {
        double maxPrice = 0;
        try 
        {
            FareDAO fareDAO = new FareDAO(conn);
            List<Fare> fares = fareDAO.findAll();
            for (Fare fare : fares) 
            {
                if (fare.getFinalAmount() > maxPrice) 
                {
                    maxPrice = fare.getFinalAmount();
                }
            }

            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            List<FlightTrip> flightTrips = flightTripDAO.findAll();
            for (FlightTrip flightTrip : flightTrips) 
            {
                if(flightTrip.getPrice() > maxPrice) 
                {
                    maxPrice = flightTrip.getPrice();
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return maxPrice;
    }


    public int getSeatsNumberHop(Hop hop) 
    {
        int seatsNumber = 0;
        try 
        {
            SeatDAO seatDAO = new SeatDAO(conn);
            seatsNumber = seatDAO.findByAirplaneId(hop.getAirplaneId()).size();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return seatsNumber;
    }


    public void UpdateFlightTrip(FlightTrip flightTrip) 
    {
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            flightTripDAO.update(flightTrip);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public boolean DeleteFlightTrip(FlightTrip flightTrip) 
    {
        boolean isDeleted = false;
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            isDeleted = flightTripDAO.delete(flightTrip.getFlightTripId());
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public List<Hop> getHopsByAirplaneId(int airplaneId) 
    {
        List<Hop> hops = new ArrayList<>();
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            hops = hopDAO.findByAirplaneId(airplaneId);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return hops;
    }

    public void UpdateHop(Hop hop) 
    {
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            hopDAO.update(hop);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public boolean DeleteHop(Hop hop) 
    {
        boolean isDeleted = true;
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            isDeleted = hopDAO.delete(hop.getHopId());
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            isDeleted = false;
        }
        return isDeleted;
    }


    public List<FlightTrip> getFlightTripsByHop(Hop hop) 
    {
        List<FlightTrip> flightTrips = new ArrayList<>();
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            flightTrips = flightTripDAO.findFlightTripsByHop(hop);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return flightTrips;
    }



    public void UpdateFlightTripPrice(Hop hop) 
    {
        try 
        {
            FlightTripDAO flightTripDAO = new FlightTripDAO(conn);
            List<FlightTrip> flights = getFlightTripsByHop(hop);

            for (FlightTrip flightTrip : flights) 
            {
                double totalPrice = 0;
                for (Hop h : flightTrip.getHops()) 
                {
                    totalPrice += h.getPrice();
                }
                flightTrip.setPrice(totalPrice);
                flightTripDAO.update(flightTrip);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }



    public void addHop(Hop hop) 
    {
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            hopDAO.insert(hop);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public boolean isHopExists(Hop hop) 
    {
        boolean exists = false;
        try 
        {
            HopDAO hopDAO = new HopDAO(conn);
            exists = hopDAO.isHopExists(hop);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return exists;
    }
}





