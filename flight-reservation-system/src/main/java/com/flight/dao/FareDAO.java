package com.flight.dao;

import com.flight.model.Fare;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FareDAO {
    private Connection conn;

    public FareDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Fare fare) throws SQLException {
        String sql = "INSERT INTO Fare (Hop_id, amount, currency, discount, tax, final_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fare.getHopId());
            stmt.setDouble(2, fare.getAmount());
            stmt.setString(3, fare.getCurrency());
            stmt.setDouble(4, fare.getDiscount());
            stmt.setDouble(5, fare.getTax());
            stmt.setDouble(6, fare.getFinalAmount());
            stmt.executeUpdate();
        }
    }

    public Fare findById(int Hop_id) throws SQLException {
        String sql = "SELECT * FROM Fare WHERE Hop_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Hop_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    Fare fare = new Fare();
                    fare.setHopId(rs.getInt("Hop_id"));
                    fare.setAmount(rs.getDouble("amount"));
                    fare.setCurrency(rs.getString("currency"));
                    fare.setDiscount(rs.getDouble("discount"));
                    fare.setTax(rs.getDouble("tax"));
                    fare.setFinalAmount(rs.getDouble("final_amount"));
                    return fare;
                }
            }
        }
        return null;
    }

    public List<Fare> findAll() throws SQLException {
        List<Fare> list = new ArrayList<>();
        String sql = "SELECT * FROM Fare";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                Fare fare = new Fare();
                fare.setHopId(rs.getInt("Hop_id"));
                fare.setAmount(rs.getDouble("amount"));
                fare.setCurrency(rs.getString("currency"));
                fare.setDiscount(rs.getDouble("discount"));
                fare.setTax(rs.getDouble("tax"));
                fare.setFinalAmount(rs.getDouble("final_amount"));
                list.add(fare);
            }
        }
        return list;
    }

    public void update(Fare fare) throws SQLException {
        String sql = "UPDATE Fare SET amount = ?, currency = ?, discount = ?, tax = ?, final_amount = ? WHERE flight_trip_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, fare.getAmount());
            stmt.setString(2, fare.getCurrency());
            stmt.setDouble(3, fare.getDiscount());
            stmt.setDouble(4, fare.getTax());
            stmt.setDouble(5, fare.getFinalAmount());
            stmt.setInt(6, fare.getHopId());
            stmt.executeUpdate();
        }
    }

    public void delete(int Hop_id) throws SQLException {
        String sql = "DELETE FROM Fare WHERE Hop_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Hop_id);
            stmt.executeUpdate();
        }
    }
}