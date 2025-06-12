package com.flight.dao;

import java.sql.*;
import java.util.*;

import com.flight.model.Airport;

public class AirportDAO {
    private Connection conn;

    public AirportDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Airport airport) throws SQLException {
        String sql = "INSERT INTO Airport (airport_id, airport_name, zip, latitude, longitude, city, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airport.getAirportId());
            stmt.setString(2, airport.getAirportName());
            stmt.setString(3, airport.getZip());
            stmt.setDouble(4, airport.getLatitude());
            stmt.setDouble(5, airport.getLongitude());
            stmt.setString(6, airport.getCity());
            stmt.setString(7, airport.getCountry());
            stmt.executeUpdate();
        }
    }

    public Airport findById(int id) throws SQLException {
        String sql = "SELECT * FROM Airport WHERE airport_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Airport airport = new Airport();
                    airport.setAirportId(rs.getInt("airport_id"));
                    airport.setAirportName(rs.getString("airport_name"));
                    airport.setZip(rs.getString("zip"));
                    airport.setLatitude(rs.getDouble("latitude"));
                    airport.setLongitude(rs.getDouble("longitude"));
                    airport.setCity(rs.getString("city"));
                    airport.setCountry(rs.getString("country"));
                    return airport;
                }
            }
        }
        return null;
    }


    public Airport findByName(String name) throws SQLException {
        String sql = "SELECT * FROM Airport WHERE Airport_Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Airport airport = new Airport();
                    airport.setAirportId(rs.getInt("airport_id"));
                    airport.setAirportName(rs.getString("airport_name"));
                    airport.setZip(rs.getString("zip"));
                    airport.setLatitude(rs.getDouble("latitude"));
                    airport.setLongitude(rs.getDouble("longitude"));
                    airport.setCity(rs.getString("city"));
                    airport.setCountry(rs.getString("country"));
                    return airport;
                }
            }
        }
        return null;
    }

    /**
     * Find all airports in the databasethatcorrespond to the given zip name.
     * @return a list of airports
     * @throws SQLException
     */
    public List<Airport> findAll() throws SQLException {
        List<Airport> list = new ArrayList<>();
        String sql = "SELECT * FROM Airport";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Airport airport = new Airport();
                airport.setAirportId(rs.getInt("airport_id"));
                airport.setAirportName(rs.getString("airport_name"));
                airport.setZip(rs.getString("zip"));
                airport.setLatitude(rs.getDouble("latitude"));
                airport.setLongitude(rs.getDouble("longitude"));
                airport.setCity(rs.getString("city"));
                airport.setCountry(rs.getString("country"));
                list.add(airport);
            }
        }
        return list;
    }


    public List<String> findAllNamesAirports() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT airport_name FROM Airport";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("airport_name"));
            }
        }
        return list;
    }




    public void update(Airport airport) throws SQLException {
        String sql = "UPDATE Airport SET airport_name = ?, zip = ?, latitude = ?, longitude = ?, city = ?, country = ? WHERE airport_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, airport.getAirportName());
            stmt.setString(2, airport.getZip());
            stmt.setDouble(3, airport.getLatitude());
            stmt.setDouble(4, airport.getLongitude());
            stmt.setString(5, airport.getCity());
            stmt.setString(6, airport.getCountry());
            stmt.setInt(7, airport.getAirportId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Airport WHERE airport_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
