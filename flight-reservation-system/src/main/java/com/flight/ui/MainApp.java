package com.flight.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.flight.model.DBConnector;
import com.flight.model.FlightTrip;
import com.flight.service.UserService;
import com.flight.ui.controllers.LoginController;
import java.sql.Connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.File;
import com.flight.*;
import com.flight.model.User;

public class MainApp extends Application {
    private static Connection dbConn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.main(null);
        primaryStage.setResizable(false);
/*
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            // Si c'est Windows, lance le script setup.bat
            runScript("sql\\setup.bat");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Si c'est Linux ou macOS, lance le script setup.sh
            runScript("sql/setup.sh");
        }
*/

        // Initialise la connexion DB (ServiceLocator possible)
        dbConn = DBConnector.getConnection();
        
        UserService userService = new UserService(dbConn);
        User admin = new User("Alice", "Bob", "control@admin.com", "38512345678", "Admin", "notsecure");
        userService.SignInAdmin(admin);



        FlightTrip.setNumFlight(dbConn);






        
        // Charge la vue de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("Flight Reservation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Passe la connexion au controller
        LoginController ctrl = loader.getController();
        ctrl.init(dbConn, primaryStage);
    }


    private static void runScript(String scriptPath) {
        try {
            // Crée un processus pour lancer le script
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
            processBuilder.directory(new File(System.getProperty("user.dir")));
            Process process = processBuilder.start();
            
            // Lire les sorties du script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // Attendre que le processus se termine
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script exécuté avec succès.");
            } else {
                System.out.println("Erreur lors de l'exécution du script.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
