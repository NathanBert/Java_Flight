

package com.flight.ui.controllers;



import com.flight.model.FlightTrip;
import com.flight.model.User;
import com.flight.service.FlightSearchService;
import com.flight.service.UserService;

import javafx.stage.Stage;


import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.scene.Scene;
import java.util.List;
import com.flight.model.Hop;



public class AdminFlightDetailsController 
{

    private Stage stage;
    private User currentUser;
    private UserService userService;

    @FXML private Label DepartLabel;
    @FXML private Label ArrivalLabel;
    @FXML private Label DateLabel;
    @FXML private Label PriceLabel;
    @FXML private Label SeatLabel;

    private FlightTrip flightTrip = null;
    private Hop hop = null;
    private AdminDashboardController adminDashboardController = null;


    public void init(User currentUser, UserService userService, Stage stage) {
        // Initialize the controller with the current user and user service
        this.currentUser = currentUser;
        this.userService = userService;
        this.stage = stage;
    }


    public void setData(FlightTrip flight, AdminDashboardController adminDashboardController)
    {
        this.adminDashboardController = adminDashboardController;

        flightTrip = flight;
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());


        // Set the flight details in the labels
        DepartLabel.setText(flightSearchService.getAirportNamebyId(flight.getDepartAirport()));
        ArrivalLabel.setText(flightSearchService.getAirportNamebyId(flight.getArrivalAirport()));
        DateLabel.setText(flight.getDepartTime().toString());
        PriceLabel.setText(String.valueOf(flight.getPrice()));

        String Seats = "";
        List<Integer> listSeats = flightSearchService.getAllSeatsForFlightTrip(flight);
        for (int i = 0; i < listSeats.size(); i++) {
            Seats+= (String.valueOf(listSeats.get(i)));
            if (i < listSeats.size() - 1) {
                Seats += (", ");
            }
        }


        SeatLabel.setText(Seats);

    }

    public void setData(Hop hop, AdminDashboardController adminDashboardController)
    {
        this.adminDashboardController = adminDashboardController;
        this.hop = hop;
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());

        // Set the flight details in the labels
        DepartLabel.setText(flightSearchService.getAirportNamebyId(hop.getDepart()));
        ArrivalLabel.setText(flightSearchService.getAirportNamebyId(hop.getArrive()));
        DateLabel.setText(hop.getDepartTime().toString());
        PriceLabel.setText(String.valueOf(hop.getPrice()));

        SeatLabel.setText(String.valueOf(flightSearchService.getSeatsNumberHop(hop)));
        

    }


    @FXML
    public void onDelete()
    {
        System.out.println("Delete flight functionality implemented.");
        if(flightTrip == null)
        {
            onDeleteHop();
        }
        else if(hop == null)
        {
            onDeleteFlight();
        }
        adminDashboardController.onSearch();
        
    }

    public boolean onDeleteFlight()
    {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        return flightSearchService.DeleteFlightTrip(flightTrip) ;

    }


    public boolean onDeleteHop()
    {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        return flightSearchService.DeleteHop(hop) ;
    }
    
    @FXML
    public void onUpdate()
    {
        System.out.println("Update flight functionality not implemented yet.");

        Stage updateStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/update_flight.fxml"));
            Node root = loader.load();
            UpdateFlightController updateFlightController = loader.getController();
            updateFlightController.init(currentUser, userService, updateStage);
            if(flightTrip != null)
            {
                updateFlightController.setData(flightTrip, adminDashboardController);
            }
            else if(hop != null)
            {
                updateFlightController.setData(hop, adminDashboardController);
            }
            
            Scene scene = new Scene((Pane) root);
            updateStage.setScene(scene);
            updateStage.setTitle("Update Flight");
            updateStage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}