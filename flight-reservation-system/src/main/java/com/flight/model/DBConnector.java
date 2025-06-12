
package com.flight.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DBConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    
    private static final String DB_NAME = "flightdb";
    private static String USER = null;
    private static String PASS = null;
    //System.getProperty("db.password");;



    // private static final String HOST = System.getProperty("host");
    // private static final String PORT = System.getProperty("port");
    // private static final String USER = System.getProperty("user");
    // private static final String PASS = System.getProperty("password");

     //private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/"+DB_NAME;
    






    private static Connection conn; 


    public static void fetchUserCredentials() {
        try (BufferedReader br = new BufferedReader(new FileReader("sql/mysql_credentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("MySQL User:")) {
                    USER = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("MySQL Password:")) {
                    PASS = line.substring(line.indexOf(":") + 1).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Méthode pour établir la connexion à la base de données
    public static Connection getConnection() throws Exception {
        // Crée la base de données si elle n'existe pas déjà
        //createDatabaseIfNotExists();

        fetchUserCredentials();
        if (USER == null || PASS == null) {
            throw new Exception("identification for mysql not found in sql/mysql_credentials.txt");
        }
        // Se connecter à la base de données 'flightdb'
        conn = DriverManager.getConnection(URL + DB_NAME, USER, PASS);
        return conn;
    }

    
    // Méthode pour créer la base de données si elle n'existe pas
    public static void createDatabaseIfNotExists(String sqlFilePath) {
        fetchUserCredentials();

        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Diviser les requêtes par le point-virgule ";"
            String[] queries = sql.toString().split(";");
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = connection.createStatement()) {
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query.trim());  // Exécuter chaque requête séparée
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
