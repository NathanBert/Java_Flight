package com.flight.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;

import com.flight.model.User;
import com.flight.service.UserService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SignInController 
{
    private Stage stage;
    private UserService userService;
    
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Label errorLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;


    public void init() {
    }

    public void init(Connection conn, Stage stage) {
        this.stage = stage;
        this.userService = new UserService(conn);
    }


    @FXML
    public void onBack() {
        // Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            LoginController ctrl = loader.getController();
            ctrl.init(userService.getUserDAO().getConnection(), stage);
        } catch (Exception e) { e.printStackTrace(); }
    }


    @FXML
    public void onSign() {
        try
        {
            String email = emailField.getText().trim();
            String pass  = passField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (email.isEmpty() || pass.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                errorLabel.setText("All fields are required");
                return;
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                errorLabel.setText("Invalid email format");
                return;
            }
            if (pass.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                errorLabel.setText("Phone number must be 10 digits");
                return;
            }
            if (userService.getUserDAO().findIfExist(email)) {
                errorLabel.setText("Email already exists");
                return;
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(pass);
            user.setFName(firstName);
            user.setLName(lastName);
            user.setPhone(phone);

            if (userService.SignInUser(user)) {
                // Rediriger vers le tableau de bord ou une autre vue
                userService.setCurrentUser(user);
                errorLabel.setText("Inscription réussie");
                loadLogin();
            } else {
                errorLabel.setText("Erreur lors de l'inscription");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de l'inscription");
            e.printStackTrace();
        }
    }

    private void loadLogin() {  
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            // Passe la connexion au controller
            LoginController ctrl = loader.getController();
            ctrl.init(userService.getUserDAO().getConnection(), stage);
        } catch (Exception e) { e.printStackTrace(); }

    }

}
