package com.flight.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.flight.model.Airplane;

/**
 * Data Access Object (DAO) for the Airplane entity.
 * This class provides methods to interact with the Airplane table in the database.
 */
public class AirplaneDAO {
    private Connection conn;

    /**
     * Constructor that initializes the DAO with a database connection.
     *
     * @param conn the database connection
     */
    public AirplaneDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new Airplane record into the database.
     *
     * @param airplane the Airplane object to be inserted
     * @throws SQLException if a database access error occurs
     */
    public void insert(Airplane airplane) throws SQLException {
        String sql = "INSERT INTO Airplane (airplane_id, seating_capacity, airline_company_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airplane.getAirplaneId());
            stmt.setInt(2, airplane.getSeatingCapacity());
            stmt.setInt(3, airplane.getAirlineCompanyId());
            stmt.executeUpdate();
        }
    }

    /**
     * Finds an Airplane record by its ID.
     *
     * @param id the ID of the Airplane to be found
     * @return the Airplane object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Airplane findById(int id) throws SQLException {
        String sql = "SELECT * FROM Airplane WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Airplane airplane = new Airplane();
                    airplane.setAirplaneId(rs.getInt("airplane_id"));
                    airplane.setSeatingCapacity(rs.getInt("seating_capacity"));
                    airplane.setAirlineCompanyId(rs.getInt("airline_company_id"));
                    return airplane;
                }
            }
        }
        return null;
    }

    /**
     * Finds all Airplane records in the database.
     *
     * @return a list of Airplane objects
     * @throws SQLException if a database access error occurs
     */
    public List<Airplane> findAll() throws SQLException {
        List<Airplane> list = new ArrayList<>();
        String sql = "SELECT * FROM Airplane";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Airplane airplane = new Airplane();
                airplane.setAirplaneId(rs.getInt("airplane_id"));
                airplane.setSeatingCapacity(rs.getInt("seating_capacity"));
                airplane.setAirlineCompanyId(rs.getInt("airline_company_id"));
                list.add(airplane);
            }
        }
        return list;
    }

    /**
     * Updates an existing Airplane record in the database.
     *
     * @param airplane the Airplane object with updated values
     * @throws SQLException if a database access error occurs
     */
    public void update(Airplane airplane) throws SQLException {
        String sql = "UPDATE Airplane SET seating_capacity = ?, airline_company_id = ? WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airplane.getSeatingCapacity());
            stmt.setInt(2, airplane.getAirlineCompanyId());
            stmt.setInt(3, airplane.getAirplaneId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an Airplane record from the database by its ID.
     *
     * @param id the ID of the Airplane to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Airplane WHERE airplane_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}