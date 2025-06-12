package com.flight.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.flight.dao.AirplaneDAO;
import com.flight.dao.SeatDAO;
import com.flight.model.Airplane;
import com.flight.model.DBConnector;

public class SeatGenerationService {
    private static final String[] SEAT_LETTERS = {"A", "B", "C", "D", "E", "F"};

    public static void generation(Connection conn)
    {
        try 
        {
            SeatDAO seatDAO = new SeatDAO(conn);
            if (seatDAO.findAll().size() > 0) {
                System.out.println("Les sièges existent déjà dans la base de données.");
                return;
            }
            else
            {
                AirplaneDAO airplaneDAO = new AirplaneDAO(conn);

                List<Airplane> airplanes = airplaneDAO.findAll();
                System.out.println("/n nombre d'avions à generer : "+ airplanes.size()+"/n");
        
                for (Airplane airplane : airplanes) {
                    System.out.println("Traitement de l'avion ID " + airplane.getAirplaneId() + " (" + airplane.getSeatingCapacity() + " sièges)");
                    generateSeatsForAirplane(conn, airplane);
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    









    public static void generateSeatsForAirplane(Connection conn, Airplane airplane) throws SQLException 
    {
        int total = airplane.getSeatingCapacity();
        int perRow = SEAT_LETTERS.length;   
        int totalRows = (int) Math.ceil((double) total / perRow);
        int inserted = 0;

        String insertSQL = "INSERT IGNORE INTO Seat (Availability, Location, Class, Airplane_Id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            for (int row = 1; row <= totalRows && inserted < total; row++) {
                for (String letter : SEAT_LETTERS) {
                    if (inserted >= total) break;

                    String location = row + letter;
                    String seatClass =
                            row <= 2 ? "First" :
                            row <= 8 ? "Business" : "Economy";

                    stmt.setBoolean(1, true); // Availability
                    stmt.setString(2, location);
                    stmt.setString(3, seatClass);
                    stmt.setInt(4, airplane.getAirplaneId());
                    stmt.addBatch();

                    inserted++;
                }
            }
            stmt.executeBatch();
        }
    }
}
