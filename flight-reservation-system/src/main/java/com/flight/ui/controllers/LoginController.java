package com.flight.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.flight.service.UserService;
import java.sql.Connection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import com.flight.model.User;
import java.io.IOException;

import com.flight.model.Traveller;


public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Label errorLabel;

    private Stage stage;
    private UserService userService;


    
    public void init(Connection conn, Stage stage) {
        this.stage = stage;
        Scene scene = stage.getScene();
        scene.getStylesheets().add(getClass().getResource("/css/Create_Account.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        stage.setScene(scene);
        this.userService = new UserService(conn);
    }

    @FXML
    public void onLogin() {
        String email = emailField.getText().trim();
        String pass  = passField.getText().trim();

        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);
        User tempUser = userService.login(user);

        if ( tempUser != null) {
            System.out.println("User type : *" + tempUser.getUserType()+"*\n");

            if (tempUser.getUserType().equals("User"))
            {
                //We set a temporary user to the user service
                userService.setCurrentUser(tempUser);
                //We make sure the traveller ID counter is set up for this user
                Traveller.setUpTravellerIdCounter( userService.getCurrentUser(),  userService.getUserDAO().getConnection());
                System.out.println("Number travels for user :"+userService.getCurrentUser().getUserId()+" is "+Traveller.getTravellerIdCounter());
                // We load the dashboard for the user
                loadDashboard();
            }
            else
            {
                if (tempUser.getUserType().equals("Admin"))
                { 
                    System.out.println("Admin logged in");
                    loadAdminDashboard();

                }
                
            }
            errorLabel.setText("Unclear user type");
            
        } else {
            errorLabel.setText("Email ou mot de passe invalide");
        }
    }

    @FXML
    public void onSignIn()
    {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signIn.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            SignInController ctrl = loader.getController();
            ctrl.init(userService.getUserDAO().getConnection(), stage);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadDashboard() {  
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            DashboardController ctrl = loader.getController();
            ctrl.init(userService.getCurrentUser(), userService, stage); // passe l’utilisateur
            ctrl.setStage(stage);
            ctrl.setUserService(userService);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadAdminDashboard() {  
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            AdminDashboardController ctrl = loader.getController();
            ctrl.init(userService.getCurrentUser(), userService, stage); // passe l’utilisateur
            ctrl.setStage(stage);
            ctrl.setUserService(userService);
        } catch (Exception e) { e.printStackTrace(); }
    }



}
 