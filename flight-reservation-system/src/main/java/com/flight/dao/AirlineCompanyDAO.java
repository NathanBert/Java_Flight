package com.flight.dao;


import com.flight.model.AirlineCompany;
import com.flight.model.Airplane;

import java.sql.*;
import java.util.*;

public class AirlineCompanyDAO {
    private Connection conn;

    public AirlineCompanyDAO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }

    public void insert(AirlineCompany company) throws SQLException {
        String sql = "INSERT INTO Airline_Company (Airline_Company_Id, Company_Name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, company.getCompanyId());
            stmt.setString(2, company.getName());
            stmt.executeUpdate();
        }
    }

    public AirlineCompany findById(int id) throws SQLException {
        String sql = "SELECT * FROM Airline_Company WHERE Airline_Company_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AirlineCompany company = new AirlineCompany();
                    company.setCompanyId(rs.getInt("Airline_Company_Id"));
                    company.setName(rs.getString("Company_Name"));
                    return company;
                }
            }
        }
        return null;
    }

    public AirlineCompany findByName(String name) throws SQLException {
        String sql = "SELECT * FROM Airline_Company WHERE Company_Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AirlineCompany company = new AirlineCompany();
                    company.setCompanyId(rs.getInt("Airline_Company_Id"));
                    company.setName(rs.getString("Company_Name"));
                    return company;
                }
            }
        }
        return null;
    }

    public List<Airplane> findAirplanesByCompany(AirlineCompany company) throws SQLException {
        List<Airplane> airplanes = new ArrayList<>();
        String sql = "SELECT * FROM Airplane WHERE Airline_Company_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, company.getCompanyId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Airplane airplane = new Airplane();
                    airplane.setAirplaneId(rs.getInt("Airplane_Id"));
                    airplane.setSeatingCapacity(rs.getInt("Seating_Capacity"));
                    airplane.setAirlineCompanyId(rs.getInt("Airline_Company_Id"));
                    airplanes.add(airplane);
                }
            }
        }
        return airplanes;
    }


    

    public List<AirlineCompany> findAll() throws SQLException {
        List<AirlineCompany> list = new ArrayList<>();
        String sql = "SELECT * FROM Airline_Company";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AirlineCompany company = new AirlineCompany();
                company.setCompanyId(rs.getInt("Airline_Company_Id"));
                company.setName(rs.getString("Company_Name"));
                list.add(company);
            }
        }
        return list;
    }

    public void update(AirlineCompany company) throws SQLException {
        String sql = "UPDATE Airline_Company SET Company_Name = ? WHERE Airline_Company_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, company.getName());
            stmt.setInt(2, company.getCompanyId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Airline_Company WHERE Airline_Company_Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
