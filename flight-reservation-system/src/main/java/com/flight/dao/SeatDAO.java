package com.flight.dao;

import com.flight.model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    private Connection conn;

    public SeatDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Seat seat) throws SQLException {
        String sql = "INSERT INTO Seat (seat_id, availability, location, class, airplane_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seat.getSeatId());
            stmt.setBoolean(2, seat.isAvailability());
            stmt.setString(3, seat.getLocation());
            stmt.setString(4, seat.getSeatClass());
            stmt.setInt(5, seat.getAirplaneId());
            stmt.executeUpdate();
        }
    }

    public Seat findById(int id) throws SQLException {
        String sql = "SELECT * FROM Seat WHERE seat_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Seat seat = new Seat();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setAvailability(rs.getBoolean("availability"));
                    seat.setLocation(rs.getString("location"));
                    seat.setSeatClass(rs.getString("class"));
                    seat.setAirplaneId(rs.getInt("airplane_id"));
                    return seat;
                }
            }
        }
        return null;
    }


    public List<Seat> findByAirplaneId(int id) throws SQLException {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM Seat WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setAvailability(rs.getBoolean("availability"));
                    seat.setLocation(rs.getString("location"));
                    seat.setSeatClass(rs.getString("class"));
                    seat.setAirplaneId(rs.getInt("airplane_id"));
                    list.add(seat);
                }

            }
            if (list.size() > 0) {
                return list;
            } else {
                return null;
            }
        }
    }

    public List<Seat> findAll() throws SQLException {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM Seat";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setAvailability(rs.getBoolean("availability"));
                seat.setLocation(rs.getString("location"));
                seat.setSeatClass(rs.getString("class"));
                seat.setAirplaneId(rs.getInt("airplane_id"));
                list.add(seat);
            }
        }
        return list;
    }

    public void update(Seat seat) throws SQLException {
        String sql = "UPDATE Seat SET availability = ?, location = ?, class = ?, airplane_id = ? WHERE seat_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, seat.isAvailability());
            stmt.setString(2, seat.getLocation());
            stmt.setString(3, seat.getSeatClass());
            stmt.setInt(4, seat.getAirplaneId());
            stmt.setInt(5, seat.getSeatId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Seat WHERE seat_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int getNumberOfAvailableSeats(int airplaneId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Seat WHERE airplane_id = ? AND availability = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airplaneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getSeatIdByLocation(String location, int airplaneId) throws SQLException {
        String sql = "SELECT seat_id FROM Seat WHERE location = ? AND airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location);
            stmt.setInt(2, airplaneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seat_id");
                }
            }
        }
        return -1; // Return -1 if no seat found
    }

    public Connection getConn()
    {
        return conn;
    }
}