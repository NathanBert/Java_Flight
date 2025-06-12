package com.flight;



import com.flight.model.*;
import com.flight.dao.*;
import com.flight.service.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {

        DBConnector.createDatabaseIfNotExists("sql/create_db.sql");
        
        try (Connection conn = DBConnector.getConnection();
            Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== FLIGHT RESERVATION SYSTEM ===\n");
            System.out.println("=== Initialisation de la base de données ===\n");
            //executeSQLFile(conn, "sql/create_db.sql"); 
            System.out.println("=== Initialisation de la base de données ===\n");

            executeSQLFile(conn, "sql/schema.sql"); 
            SeatGenerationService.generation(conn);
            FlightSearchService flightSearchService = new FlightSearchService(conn);
            /*
            boolean running = true;
            while (running) {
                System.out.println("=== MENU ===");
                System.out.println("1. Rechercher un vol");
                System.out.println("2. Quitter");
                System.out.println("3. Find by Id");

                System.out.print("Choix : ");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        scanner.nextLine(); // Consommer le retour chariot
                        System.out.print("ID aéroport de départ (ou vide) : ");
                        String depStr = scanner.nextLine();
                        String dep = depStr.isEmpty() ? null : depStr;

                        System.out.print("ID aéroport d’arrivée (ou vide) : ");
                        String arrStr = scanner.nextLine();
                        String arr = arrStr.isEmpty() ? null : arrStr;

                        System.out.print("Date départ min (yyyy-MM-dd HH:mm) ou vide : ");
                        String dateDepStr = scanner.nextLine();
                        Timestamp minDepart = dateDepStr.isEmpty() ? null :
                                Timestamp.valueOf(LocalDateTime.parse(dateDepStr.replace(" ", "T")));

                        System.out.print("Date arrivée max (yyyy-MM-dd HH:mm) ou vide : ");
                        String dateArrStr = scanner.nextLine();
                        Timestamp maxArrive = dateArrStr.isEmpty() ? null :
                                Timestamp.valueOf(LocalDateTime.parse(dateArrStr.replace(" ", "T")));

                        System.out.print("Classe de siège (economy/business/...) ou vide : ");
                        String classe = scanner.nextLine();
                        classe = classe.isEmpty() ? null : classe;

                        System.out.print("Nombre minimum de sièges disponibles : ");
                        int nb = scanner.nextInt();

                        List<FlightTrip> vols = flightSearchService.searchFlights(Integer.parseInt(arr), Integer.parseInt(dep), minDepart, maxArrive, nb, classe);
                        if (vols == null || vols.isEmpty()) {
                            System.out.println("Aucun vol trouvé.");
                        } else {
                            for (FlightTrip info : vols) {
                                System.out.println("Vol #" + info.getFlightTripId() +
                                        " | Depart: " + info.getDepartAirport() +
                                        " | Sièges disponibles: " + info.getHops().toString());
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Fin du programme.");
                        running = false;
                        break;

                    case 3:
                        System.out.print("ID de l'aéroport à rechercher : ");
                        int airportId = scanner.nextInt();
                        AirportDAO airportDAO = new AirportDAO(conn);
                        Airport airport = airportDAO.findById(airportId);
                        System.out.println(airport.getAirportName() + " " + airport.getCity() + " " + airport.getCountry());
                        break;

                    default:
                        System.out.println("Choix invalide.");
                }
                        
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        
    }


    public static void executeSQLFile(Connection conn, String filePath) throws SQLException {
        // Lire le fichier SQL
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Diviser les requêtes par le point-virgule ";"
            String[] queries = sql.toString().split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query.trim());  // Exécuter chaque requête séparée
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
