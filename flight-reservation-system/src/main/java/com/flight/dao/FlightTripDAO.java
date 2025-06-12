package com.flight.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import com.flight.model.FlightTrip;
import com.flight.model.FlightTripInfo;
import com.flight.model.Hop;
import com.flight.model.AirlineCompany;
import com.flight.model.Airplane;
import com.flight.model.Airport;
import com.flight.model.DBConnector;
import com.flight.dao.HopDAO;
import com.flight.model.Seat;


public class FlightTripDAO {
    private Connection conn;

    public FlightTripDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(FlightTrip trip) throws SQLException {

        if (!flightTripExists(trip))
        {

            String sql = "INSERT IGNORE INTO Flight_Trip (flight_trip_id, distance, depart_time, arrival_time, depart_airport, arrival_airport, Price) VALUES (?, ?, ?, ?, ?, ?, ?)";
            trip.setFlightTripId(FlightTrip.getNumFlights()+1);
            FlightTrip.incrementNumFlights();
            System.out.println("Inserting flight trip with ID: " + trip.getFlightTripId());
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, trip.getFlightTripId());
                stmt.setDouble(2, trip.getDistance());
                stmt.setTimestamp(3, trip.getDepartTime());
                stmt.setTimestamp(4, trip.getArrivalTime());
                stmt.setInt(5, trip.getDepartAirport());
                stmt.setInt(6, trip.getArrivalAirport());
                stmt.setDouble(7, trip.getPrice());
                stmt.executeUpdate();

                sql = "INSERT INTO Flight_Trip_Hop (flight_trip_id, hop_id) VALUES (?, ?)";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    for (Hop hop : trip.getHops()) {
                        st.setInt(1, trip.getFlightTripId());
                        st.setInt(2, hop.getHopId()); 
                        //stmt.setInt(3, trip.getNoTravellers().get(trip.getHops().indexOf(hop)));

                        st.executeUpdate();
                    }

                }
                catch (SQLException e) {
                    System.err.println("Error inserting hops for flight trip: " + e.getMessage());
                    throw e;
                }
                System.out.println("Flight trip inserted successfully with ID: " + trip.getFlightTripId());
            } catch (SQLException e) {
                System.err.println("Error inserting flight trip: " + e.getMessage());
                throw e;

            }
        }
        




    }

    public FlightTrip findById(int id) throws SQLException {
        FlightTrip trip = new FlightTrip();
        HopDAO hopDAO = new HopDAO(conn);


        String sql = "SELECT * FROM Flight_Trip WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    trip.setFlightTripId(rs.getInt("flight_trip_id"));
                    trip.setDistance(rs.getDouble("distance"));
                    trip.setDepartTime(rs.getTimestamp("depart_time"));
                    trip.setArrivalTime(rs.getTimestamp("arrival_time"));
                    trip.setDepartAirport(rs.getInt("depart_airport"));
                    trip.setArrivalAirport(rs.getInt("arrival_airport"));
                    trip.setPrice(rs.getDouble("Price"));
                }
            }
        }

        sql = "SELECT * FROM Flight_Trip_Hop WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Hop> hops = new ArrayList<>();
                //List<Integer> noTravellers = new ArrayList<>();
                while(rs.next()){
                    int hop_id = rs.getInt("hop_id");
                    //noTravellers.add(rs.getInt("No_Travellers"));
                    hops.add(hopDAO.findByHopid(hop_id));
                
                }
                trip.setHops(hops);
                
               // trip.setNoTravellers(noTravellers);
                return trip;
            }
        }
    }

    public List<FlightTrip> findAll() throws SQLException {
        List<FlightTrip> list = new ArrayList<>();
        HopDAO hopDAO = new HopDAO(conn);

        String sql = "SELECT * FROM Flight_Trip";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                FlightTrip trip = new FlightTrip();
                trip.setFlightTripId(rs.getInt("flight_trip_id"));
                trip.setDistance(rs.getDouble("distance"));
                trip.setDepartTime(rs.getTimestamp("depart_time"));
                trip.setArrivalTime(rs.getTimestamp("arrival_time"));
                trip.setDepartAirport(rs.getInt("depart_airport"));
                trip.setArrivalAirport(rs.getInt("arrival_airport"));
                trip.setPrice(rs.getDouble("Price"));

                list.add(trip);
            }
        }


        sql = "SELECT * FROM Flight_Trip_Hop";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                int flightTripId = rs.getInt("flight_trip_id");
                int hopId = rs.getInt("hop_id");
                //int noTravellers = rs.getInt("No_Travellers");

                for (FlightTrip trip : list) {
                    if (trip.getFlightTripId() == flightTripId) {
                        Hop hop = hopDAO.findByHopid(hopId);
                        
                        trip.getHops().add(hop);
                        
                        //trip.getNoTravellers().add(noTravellers);
                    }
                }
            }
        }
        return list;
    }

    public static List<FlightTrip> buildFlightTrips(int startAirport, int endAirport, int maxHops,
                                                    Connection conn) throws SQLException {
        List<FlightTrip> allTrips = new ArrayList<>();
        List<Hop> path = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        HopDAO hopDAO = new HopDAO(conn); 
        List<Hop> availableHops = hopDAO.findAll();


        dfs(startAirport, endAirport, availableHops, path, visited, allTrips, maxHops);

        return allTrips;
    }

    private static void dfs(int currentAirport, int endAirport, List<Hop> availableHops,
                             List<Hop> path, Set<Integer> visited, List<FlightTrip> allTrips,
                             int maxHops) {
        if (currentAirport == endAirport || maxHops == 0) {
            FlightTrip trip = new FlightTrip();
            trip.setHops(new ArrayList<>(path)); // Cloner la liste actuelle
            trip.setDepartAirport(path.get(0).getDepart());
            trip.setArrivalAirport(path.get(path.size() - 1).getArrive());
            trip.setDepartTime(path.get(0).getDepartTime());
            trip.setArrivalTime(path.get(path.size() - 1).getArrivalTime());
            allTrips.add(trip);
            return;
        }

        visited.add(currentAirport);

        for (Hop hop : availableHops) {
            if (hop.getDepart() == currentAirport && !visited.contains(hop.getArrive())) {
                path.add(hop);
                dfs(hop.getArrive(), endAirport, availableHops, path, visited, allTrips, maxHops - 1);
                path.remove(path.size() - 1); // backtrack
            }
        }

        visited.remove(currentAirport);
    }

    public void update(FlightTrip trip) throws SQLException {
        String sql = "UPDATE Flight_Trip SET distance = ?, depart_time = ?, arrival_time = ?, depart_airport = ?, arrival_airport = ?, Price = ? WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, trip.getDistance());
            stmt.setTimestamp(2, trip.getDepartTime());
            stmt.setTimestamp(3, trip.getArrivalTime());
            stmt.setInt(4, trip.getDepartAirport());
            stmt.setInt(5, trip.getArrivalAirport());
            stmt.setDouble(6, trip.getPrice());

            stmt.setInt(7, trip.getFlightTripId());

            stmt.executeUpdate();
        }
        /*

        sql = "UPDATE Flight_Trip_Hop SET No_Travellers = ? WHERE flight_trip_id = ? AND hop_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Hop hop : trip.getHops()) {
                //stmt.setInt(1, trip.getNoTravellers().get(trip.getHops().indexOf(hop)));
                stmt.setInt(1, trip.getFlightTripId());
                stmt.setInt(2, hop.getHopId());
                stmt.executeUpdate();
            }
        }
            */
    }

    public boolean delete(int id) throws SQLException {
        boolean deleted = true;



        String sql = "DELETE FROM Flight_Trip_Hop WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("Error deleting flight trip: " + e.getMessage());
            deleted = false;
        }


        sql = "DELETE FROM Flight_Trip WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("Error deleting flight trip: " + e.getMessage());
            deleted = false;
        }

        return deleted;


    }

    public List<FlightTripInfo> getFlightTripInfosByUserId(int userId) throws SQLException {
        List<FlightTripInfo> flightTripInfos = new ArrayList<>();
        List<Seat> Seats = new ArrayList<>();
        List<Airplane> Airplanes = new ArrayList<>();
        List<AirlineCompany> AirlineCompanies = new ArrayList<>();
        List<String> ZIPS = new ArrayList<>();
        
        String sql = """
            SELECT ft.Flight_Trip_Id, ft.Depart_Time, ft.Arrival_Time,
                   ft.Depart_Airport, ft.Arrival_Airport,
                   s.Seat_Id, s.Location, s.Class,
                   ap.Airplane_Id, ap.Seating_Capacity,
                   dc.Airport_Name AS Depart_Name, ac.Airport_Name AS Arrival_Name, dc.ZIP,
                   ac2.Airline_Company_Id, ac2.Company_Name, t.Traveller_Id
            FROM Traveller t
            JOIN Seat s ON t.Seat_Id = s.Seat_Id
            JOIN Airplane ap ON s.Airplane_Id = ap.Airplane_Id
            JOIN Hop h ON h.Airplane_Id = ap.Airplane_Id
            JOIN Flight_Trip_Hop fth ON fth.Hop_Id = h.Hop_Id
            JOIN Flight_Trip ft ON ft.Flight_Trip_Id = fth.Flight_Trip_Id
            JOIN Airport dc ON dc.Airport_Id = ft.Depart_Airport
            JOIN Airport ac ON ac.Airport_Id = ft.Arrival_Airport
            JOIN Airline_Company ac2 ON ac2.Airline_Company_Id = ap.Airline_Company_Id
            WHERE s.Availability = FALSE AND t.User_Id = ? AND ft.Flight_Trip_Id = t.Flight_Trip_Id
            
        """;
        

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FlightTrip trip = findById(rs.getInt("Flight_Trip_Id"));

                    Seat seat = new Seat();
                    seat.setSeatId(rs.getInt("Seat_Id"));
                    seat.setLocation(rs.getString("Location"));
                    seat.setSeatClass(rs.getString("Class"));
                    Seats.add(seat);

                    Airplane airplane = new Airplane();
                    airplane.setAirplaneId(rs.getInt("Airplane_Id"));
                    airplane.setSeatingCapacity(rs.getInt("Seating_Capacity"));
                    Airplanes.add(airplane);

                    AirlineCompany airlineCompany = new AirlineCompany();
                    airlineCompany.setCompanyId(rs.getInt("Airline_Company_Id"));
                    airlineCompany.setName(rs.getString("Company_Name"));
                    AirlineCompanies.add(airlineCompany);

                    ZIPS.add(rs.getString("ZIP"));

                    int traveller_Id = rs.getInt("Traveller_Id");




                    

                    flightTripInfos.add(new FlightTripInfo(trip, Seats, Airplanes, AirlineCompanies, ZIPS,traveller_Id ));
                }
            }
        }

        return flightTripInfos;
    }

    public FlightTrip findSingletonFlightTripHop(Hop hop) throws SQLException {
        System.out.println("\n Passé ici 2\n");

        String sql = "SELECT * FROM Flight_Trip_Hop WHERE hop_id = ? AND flight_trip_id IN (SELECT flight_trip_id FROM Flight_Trip_Hop GROUP BY flight_trip_id HAVING COUNT(*) = 1)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hop.getHopId());
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    FlightTrip trip = findById(rs.getInt("flight_trip_id"));
                    System.out.println("Found trip with Price = " + trip.getPrice());
                    List<Hop> hops = new ArrayList<>();
                    hops.add(hop);
                    trip.setHops(hops);

                    System.out.println("\n Trip find for hop id" + hop.getHopId()+" for trip"+trip.getFlightTripId()+"\n");

                    return trip;
                }
            }
        }
        return null;
    }




    public List<FlightTrip> findNonSingletonFlightTrips(int departure, int arrival, String date, double maxPrice, int maxSeat) throws SQLException 
    {
        List<FlightTrip> trips = new ArrayList<>();
        String sql = "SELECT ft.Flight_Trip_Id, ft.Depart_Time, ft.Arrival_Time, ft.Depart_Airport, ft.Arrival_Airport, ft.Price "+
        "FROM Flight_Trip ft JOIN Flight_Trip_Hop fth ON ft.Flight_Trip_Id = fth.Flight_Trip_Id"
        +" WHERE ft.Depart_Airport = ? AND ft.Arrival_Airport = ? AND ft.Price <= ? GROUP BY ft.flight_trip_id HAVING COUNT(*) > 1 ";;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setInt(1, departure);
            stmt.setInt(2, arrival);
            stmt.setDouble(3, maxPrice);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FlightTrip trip = new FlightTrip();
                    trip.setFlightTripId(rs.getInt("Flight_Trip_Id"));
                    trip.setDepartTime(rs.getTimestamp("Depart_Time"));
                    trip.setArrivalTime(rs.getTimestamp("Arrival_Time"));
                    trip.setDepartAirport(rs.getInt("Depart_Airport"));
                    trip.setArrivalAirport(rs.getInt("Arrival_Airport"));
                    trip.setPrice(rs.getDouble("Price"));

                    trips.add(trip);
                }
            }
      
            
        
      

        } catch (SQLException e) {
            System.err.println("Error finding non-singleton flight trips: " + e.getMessage());
            throw e;
        }



        return trips;

    }




    public Boolean isEnoughSeatsFlightTrip(int flightTripId, int requiredSeats) throws SQLException {
        String sql = "SELECT a.Seating_Capacity AS total_available_seats " +
                     "FROM Flight_Trip_Hop fth " +
                     "JOIN Hop h ON fth.hop_id = h.hop_id " +
                     "JOIN Airplane a ON h.airplane_id = a.airplane_id " +
                     "WHERE fth.flight_trip_id = ?";

        List<Integer> availableSeats = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flightTripId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int totalAvailableSeats = rs.getInt("total_available_seats");
                    availableSeats.add(totalAvailableSeats);
                }
            }
        }

        for (int seats : availableSeats) {
            if (seats < requiredSeats) {
                return false;
            }
        }
        return true;
    }



    public List<Integer> getSeatsForFlightTrip(int flightTripId) throws SQLException {
        String sql = "SELECT a.Seating_Capacity AS total_available_seats " +
                     "FROM Flight_Trip_Hop fth " +
                     "JOIN Hop h ON fth.hop_id = h.hop_id " +
                     "JOIN Airplane a ON h.airplane_id = a.airplane_id " +
                     "WHERE fth.flight_trip_id = ?";

        List<Integer> availableSeats = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flightTripId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int totalAvailableSeats = rs.getInt("total_available_seats");
                    availableSeats.add(totalAvailableSeats);
                }
            }
        }

        return availableSeats;
    }

    public boolean flightTripExists(FlightTrip flightTrip) throws SQLException {
    String sql = """
        SELECT 1 FROM Flight_Trip
        WHERE Depart_Time = ? AND Arrival_Time = ?
          AND Depart_Airport = ? AND Arrival_Airport = ?
        LIMIT 1
    """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setTimestamp(1, flightTrip.getDepartTime());
        stmt.setTimestamp(2, flightTrip.getArrivalTime());
        stmt.setInt(3, flightTrip.getDepartAirport());
        stmt.setInt(4, flightTrip.getArrivalAirport());

        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // true if a record was found
        }
    }
    }

    public FlightTrip findByInfo(FlightTrip trip) throws SQLException {
        String sql = "SELECT * FROM Flight_Trip WHERE depart_airport = ? AND arrival_airport = ? AND depart_time = ? AND arrival_time = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trip.getDepartAirport());
            stmt.setInt(2, trip.getArrivalAirport());
            stmt.setTimestamp(3, trip.getDepartTime());
            stmt.setTimestamp(4, trip.getArrivalTime());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return findById(rs.getInt("flight_trip_id"));
                }
            }
        }
        return null;
    }


    public List<FlightTrip> findFlightTripsByHop(Hop hop) throws SQLException {
        List<FlightTrip> trips = new ArrayList<>();
        String sql = "SELECT ft.* FROM Flight_Trip ft " +
                     "JOIN Flight_Trip_Hop fth ON ft.flight_trip_id = fth.flight_trip_id " +
                     "WHERE fth.hop_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hop.getHopId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FlightTrip trip = findById(rs.getInt("flight_trip_id"));
                    trips.add(trip);
                }
            }
        }
        return trips;
    }

}

