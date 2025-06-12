package com.flight.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.flight.model.FlightTrip;
import com.flight.model.Traveller;
import com.flight.model.Seat;
import com.flight.model.User;

public class TravellerDAO {
    private Connection conn;

    public TravellerDAO(Connection conn) {
        this.conn = conn;
    }

    

    public void insert(User user, Seat seat, FlightTrip flight, int step) throws SQLException {
        System.out.println("Inserting traveller: " + user.getUserId() + ", Flight Trip ID: " + flight.getFlightTripId() + ", Seat ID: " + seat.getSeatId());
        
        String sql = "INSERT INTO Traveller (user_id, Flight_Trip_Id, Seat_Id, Step, Traveller_Id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, user.getUserId());
            stmt.setInt(2, flight.getFlightTripId());
            stmt.setInt(3, seat.getSeatId());
            stmt.setInt(4, step);
            stmt.setInt(5, Traveller.getTravellerIdCounter());
            stmt.executeUpdate();
            
        }
    }

    public List<Traveller> findByUserId(int userId) throws SQLException {
        List<Traveller> list = new ArrayList<>();
        String sql = "SELECT * FROM Traveller WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    Traveller traveller = new Traveller();
                    traveller.setUserId(rs.getInt("user_id"));
                    traveller.setFlightTripId(rs.getInt("flight_trip_id"));
                    traveller.setSeatId(rs.getInt("Seat_Id"));
                    traveller.setStep(rs.getInt("Step"));
                    traveller.setTraveller_Id(rs.getInt("Traveller_Id"));
                    list.add(traveller);
                }
            }
        }
        return list;
    }

    public Traveller findById(int Id) throws SQLException {
        String sql = "SELECT * FROM Traveller WHERE Traveller_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    Traveller traveller = new Traveller();
                    traveller.setUserId(rs.getInt("user_id"));
                    traveller.setFlightTripId(rs.getInt("flight_trip_id"));
                    traveller.setSeatId(rs.getInt("Seat_Id"));
                    traveller.setStep(rs.getInt("Step"));
                    
                    return traveller;
                }
            }
        }
        return null;
    }

    public Traveller findByIds(int userId, int Flight_Id, int step, int traveller_Id) throws SQLException {
        String sql = "SELECT * FROM Traveller WHERE user_id = ? AND flight_trip_id = ? AND Step = ? AND Traveller_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, Flight_Id);
            stmt.setInt(3, step);
            stmt.setInt(4, traveller_Id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    Traveller traveller = new Traveller();
                    traveller.setUserId(rs.getInt("user_id"));
                    traveller.setFlightTripId(rs.getInt("flight_trip_id"));
                    traveller.setSeatId(rs.getInt("Seat_Id"));
                    traveller.setStep(rs.getInt("Step"));
                    
                    return traveller;
                }
            }
        }
        return null;
    }

    public List<Traveller> findAll() throws SQLException {
        List<Traveller> list = new ArrayList<>();
        String sql = "SELECT * FROM Traveller";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                Traveller traveller = new Traveller();
                traveller.setUserId(rs.getInt("user_id"));
                traveller.setFlightTripId(rs.getInt("flight_trip_id"));
                traveller.setSeatId(rs.getInt("Seat_Id"));
                traveller.setStep(rs.getInt("Step"));
                list.add(traveller);
            }
        }
        return list;
    }

    public void delete(FlightTrip flight, Seat seat) throws SQLException {
        String sql = "DELETE FROM Traveller WHERE flight_trip_id = ? AND Seat_Id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flight.getFlightTripId());
            stmt.setInt(2, seat.getSeatId());
            stmt.executeUpdate();
        }

        
    }

    public void deleteBySeatId(int seatId) throws SQLException {
        String sql = "DELETE FROM Traveller WHERE Seat_Id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            stmt.executeUpdate();
        }
    }

    public Connection getConnection()
    {
        return conn;
    }
}