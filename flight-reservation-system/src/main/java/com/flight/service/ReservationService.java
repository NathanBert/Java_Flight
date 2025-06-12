package com.flight.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.util.List;

import com.flight.model.ReservationDetails;

public class ReservationService {

    
    public static List<ReservationDetails> getReservationsByUserId(int userId, Connection conn) throws SQLException 
    {
        List<ReservationDetails> reservations = new ArrayList<>();

        String sql = "SELECT " +
                    "    t.User_Id, ft.Flight_Trip_Id, " +
                    "    dep.Airport_Name AS Depart_Airport, " +
                    "    arr.Airport_Name AS Arrival_Airport, " +
                    "    ft.Depart_Time, ft.Arrival_Time, " +
                    "    s.Location AS Seat_Location, s.Class AS Seat_Class, " +
                    "    ac.Company_Name AS Airplane_Model, " +
                    "    f.Amount, f.Currency " +
                    "FROM Traveller t " +
                    "JOIN Seat s ON t.Seat_Id = s.Seat_Id " +
                    "JOIN Airplane ap ON s.Airplane_Id = ap.Airplane_Id " +
                    "JOIN Hop h ON ap.Airplane_Id = h.Airplane_Id " +
                    "JOIN Flight_Trip ft ON h.Flight_Trip_Id = ft.Flight_Trip_Id " +
                    "JOIN Airport dep ON ft.Depart_Airport = dep.Airport_Id " +
                    "JOIN Airport arr ON ft.Arrival_Airport = arr.Airport_Id " +
                    "LEFT JOIN Fare f ON f.Flight_Trip_Id = ft.Flight_Trip_Id " +
                    "JOIN Airline_Company ac ON ap.Airline_Company_Id = ac.Airline_Company_Id " +
                    "WHERE t.User_Id = ? " +
                    "GROUP BY ft.Flight_Trip_Id, s.Seat_Id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationDetails res = new ReservationDetails();
                    res.setUserId(rs.getInt("User_Id"));
                    res.setFlightTripId(rs.getInt("Flight_Trip_Id"));
                    res.setDepartAirportName(rs.getString("Depart_Airport"));
                    res.setArrivalAirportName(rs.getString("Arrival_Airport"));
                    res.setDepartTime(rs.getTimestamp("Depart_Time"));
                    res.setArrivalTime(rs.getTimestamp("Arrival_Time"));
                    res.setSeatLocation(rs.getString("Location"));
                    res.setSeatClass(rs.getString("Class"));
                    res.setFareAmount(rs.getDouble("Amount"));
                    res.setCurrency(rs.getString("Currency"));

                    reservations.add(res);
                }
            }
        }

        return reservations;
    }

    
    

}
