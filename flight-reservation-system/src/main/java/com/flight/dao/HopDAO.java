package com.flight.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.flight.model.Hop;

public class HopDAO {
    private Connection conn;

    public HopDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Hop hop) throws SQLException {
        String sql = "INSERT INTO Hop (airplane_id, depart_time, arrival_time, depart, arrival) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hop.getAirplaneId());
            stmt.setTimestamp(2, hop.getDepartTime());
            stmt.setTimestamp(3, hop.getArrivalTime());
            stmt.setInt(4, hop.getDepart());
            stmt.setInt(5, hop.getArrive());
            stmt.executeUpdate();
        }

        hop.setHopId(getHopIdByAll(hop.getAirplaneId(), hop.getDepartTime(), hop.getArrivalTime(), hop.getDepart(), hop.getArrive()));
        

        sql = "INSERT INTO Fare (Hop_Id, Final_Amount) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, hop.getHopId());
            stmt.setDouble(2, hop.getPrice());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    hop.setHopId(rs.getInt(1));
                }
            }
        }
    }

    public int getHopIdByAll(int airplaneId, Timestamp departTime, Timestamp arrivalTime, int depart, int arrive) throws SQLException {
        String sql = "SELECT hop_id FROM Hop WHERE airplane_id = ? AND depart_time = ? AND arrival_time = ? AND depart = ? AND arrival = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airplaneId);
            stmt.setTimestamp(2, departTime);
            stmt.setTimestamp(3, arrivalTime);
            stmt.setInt(4, depart);
            stmt.setInt(5, arrive);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hop_id");
                }
            }
        }
        return -1; // Return -1 if no matching hop is found
    }


    public boolean isHopExists(Hop hop) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Hop WHERE airplane_id = ? AND depart_time = ? AND arrival_time = ? AND depart = ? AND arrival = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hop.getAirplaneId());
            stmt.setTimestamp(2, hop.getDepartTime());
            stmt.setTimestamp(3, hop.getArrivalTime());
            stmt.setInt(4, hop.getDepart());
            stmt.setInt(5, hop.getArrive());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Integer> getSeatsForHop(int hop_id) throws SQLException {
        List<Integer> seats = new ArrayList<>();
        String sql = "SELECT s.seat_id FROM Seat s JOIN Hop h ON h.Airplane_Id = s.Airplane_Id  WHERE h.hop_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hop_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(rs.getInt("seat_id"));
                }
            }
        }
        return seats;
    }


    public Hop findByHopid(int hop_id) throws SQLException {
        Hop list = new Hop();
        String sql = "SELECT * FROM Hop WHERE hop_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt (1, hop_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    list.setAirplaneId(rs.getInt("airplane_id"));
                    list.setHopId(rs.getInt("hop_id"));
                    list.setDepartTime(rs.getTimestamp("depart_time"));
                    list.setArrivalTime(rs.getTimestamp("arrival_time"));
                    list.setDepart(rs.getInt("depart"));
                    list.setArrive(rs.getInt("arrival"));
                }
            }

        }
        return list;

    }

    public List<Hop> findByAirplaneId(int airplane_id) throws SQLException {
        List<Hop> list = new ArrayList<>();
        String sql = "SELECT * FROM Hop WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airplane_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    Hop hop = new Hop();
                    hop.setAirplaneId(rs.getInt("airplane_id"));
                    hop.setHopId(rs.getInt("hop_id"));
                    hop.setDepartTime(rs.getTimestamp("depart_time"));
                    hop.setArrivalTime(rs.getTimestamp("arrival_time"));
                    hop.setDepart(rs.getInt("depart"));
                    hop.setArrive(rs.getInt("arrival"));
                    list.add(hop);
                }
            }
        }
        return list;
    }


    public List<Hop> findAll() throws SQLException {
        List<Hop> list = new ArrayList<>();
        String sql = "SELECT * FROM Hop h JOIN Fare f ON f.Hop_id = h.hop_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                Hop hop = new Hop();
                hop.setAirplaneId(rs.getInt("airplane_id"));
                hop.setHopId(rs.getInt("hop_id"));
                hop.setDepartTime(rs.getTimestamp("depart_time"));
                hop.setArrivalTime(rs.getTimestamp("arrival_time"));
                hop.setDepart(rs.getInt("depart"));
                hop.setArrive(rs.getInt("arrival"));
                hop.setPrice(rs.getDouble("Final_Amount"));
                list.add(hop);
            }
        }
        return list;
    }

    public void update(Hop hop) throws SQLException {
        String sql = "UPDATE Hop SET depart_time = ?, arrival_time = ?, depart = ?, arrival = ? WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, hop.getDepartTime());
            stmt.setTimestamp(2, hop.getArrivalTime());
            stmt.setInt(3, hop.getDepart());
            stmt.setInt(4, hop.getArrive());
            stmt.setInt(5, hop.getAirplaneId());
            stmt.executeUpdate();
        }

        sql = "UPDATE Fare SET Final_Amount = ? WHERE Hop_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, hop.getPrice());
            stmt.setInt(2, hop.getHopId());
            stmt.executeUpdate();
        }
    }

    public boolean delete(int hop_id) throws SQLException {


        List<String> sqlQueries = new ArrayList<>();
        sqlQueries.add("DELETE FROM Flight_Trip \n" + //
                        "WHERE Flight_Trip_Id IN (\n" + //
                        "    SELECT Flight_Trip_Id \n" + //
                        "    FROM Flight_Trip_Hop \n" + //
                        "    WHERE Hop_Id = ?)\n");
        sqlQueries.add("DELETE FROM Flight_Trip_Hop WHERE Hop_Id = ?");
        sqlQueries.add("DELETE FROM Fare WHERE Hop_Id = ?");
        sqlQueries.add("DELETE FROM Hop WHERE Hop_Id = ?");

        for (String sql : sqlQueries) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, hop_id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
                return false;
            }
        }

        return true;
    }
}


