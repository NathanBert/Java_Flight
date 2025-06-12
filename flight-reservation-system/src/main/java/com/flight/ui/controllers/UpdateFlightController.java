package com.flight.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.flight.model.FlightTrip;
import com.flight.service.CreateTravellerService;
import com.flight.service.FlightSearchService;
import com.flight.service.SeatReservationService;
import com.flight.service.UserService;
import com.flight.model.User;
import com.flight.model.Hop;
import com.flight.model.Seat;
import java.util.List;
import com.flight.dao.HopDAO;








public class UpdateFlightController 
{

    @FXML private Label DepartAirportLabel;
    @FXML private Label ArrivalAirportLabel;
    @FXML private Label DepartDateLabel;
    @FXML private TextField priceTextField;
    @FXML private ComboBox<String> SeatComboBox;
    @FXML private Button updateButton;
    @FXML private Label errorLabel;
    @FXML private Label availabilityLabel;

    private Stage stage;
    private User currentUser;
    private UserService userService;


    private Hop hop;
    private FlightTrip flightTrip;
    private AdminDashboardController adminDashboardController;

    public void init(User currentUser, UserService userService, Stage stage) {
        this.currentUser = currentUser;
        this.userService = userService;
        this.stage = stage;


        stage.setOnCloseRequest(event -> {
            event.consume(); // Prevent the default close action
            closeWindow(); // Call the custom close method
        });
    }

    public void setData(FlightTrip flight, AdminDashboardController adminDashboardController) {

        this.adminDashboardController = adminDashboardController;
        this.flightTrip = flight;

        adminDashboardController.getMainBorderPane().setDisable(true);

        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());

        DepartAirportLabel.setText(flightSearchService.getAirportNamebyId(flight.getDepartAirport()));
        ArrivalAirportLabel.setText(flightSearchService.getAirportNamebyId(flight.getArrivalAirport()));
        DepartDateLabel.setText(flight.getDepartTime().toLocalDateTime().toString());
        priceTextField.setText(String.valueOf(flight.getPrice()));

        setSeatComboBox(flight);
        //SeatComboBox.getItems().addAll("Economy", "Business", "First Class");
        //SeatComboBox.setValue(hop.getFare().getSeatClass());
    }

    public void setData(Hop hop, AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
        this.hop = hop;

        adminDashboardController.getMainBorderPane().setDisable(true);

        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());

        DepartAirportLabel.setText(flightSearchService.getAirportNamebyId(hop.getDepart()));
        ArrivalAirportLabel.setText(flightSearchService.getAirportNamebyId(hop.getArrive()));
        DepartDateLabel.setText(hop.getDepartTime().toLocalDateTime().toString());
        priceTextField.setText(String.valueOf(hop.getPrice()));

        setSeatComboBox(hop);

        //SeatComboBox.getItems().addAll("Economy", "Business", "First Class");
        //SeatComboBox.setValue(hop.getFare().getSeatClass());
    }


    public void setSeatComboBox(Hop hop)
    {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        List<Integer> seatsID = flightSearchService.getAllSeatsForHop(hop);

        try
        {
            for (int seatID : seatsID) 
            {
                Seat seat = seatReservationService.getSeatById(seatID);

                if (seat != null)
                {
                    SeatComboBox.getItems().add(seat.getLocation());
                }
            }

            
            SeatComboBox.setOnAction(event -> {
                try
                {   
                String selectedItem = SeatComboBox.getValue();
                setAvailabilityLabel(seatReservationService.getSeatById(flightSearchService.getSeatIdByHop(hop, selectedItem)));
                }
                
                catch (Exception e) 
                {
                    e.printStackTrace();
                    errorLabel.setText("Error retrieving seat information.");
                }
                
            });

        } 
        catch (Exception e)
        {
            e.printStackTrace();    
        }
    }


    public void setAvailabilityLabel(Seat seat) {
        if (seat != null) {
            if (seat.getAvailability()) {
                availabilityLabel.setText("True");
                availabilityLabel.setStyle("-fx-text-fill: green;"); 
            } else {
                availabilityLabel.setText("False");
                availabilityLabel.setStyle("-fx-text-fill: red;"); 
            }
        } else {
            availabilityLabel.setText("Seat not found");
        }
    }



    public void setSeatComboBox(FlightTrip flight)
    {

        /*
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        List<Integer> seatsID = flightSearchService.getAllSeatsForFlightTrip(flight);
        try
        {
            for (int seatID : seatsID) 
            {
                Seat seat = seatReservationService.getSeatById(seatID);

                if (seat != null)
                {
                    SeatComboBox.getItems().add(seat.getLocation());
                }
            }
        }   
        catch (Exception e)
        {
            e.printStackTrace();    
        }*/

        SeatComboBox.setDisable(true);

    }




    @FXML 
    public void onUpdate() 
    {
        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        CreateTravellerService createTravellerService = new CreateTravellerService(userService.getUserDAO().getConnection());
        
        double newPrice = 0;
        try
        {
            newPrice = (Double.valueOf(priceTextField.getText()));
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid price format. Please enter a valid number.");
            return;
        }
        if (newPrice < 0) {
            errorLabel.setText("Price must be greater than zero.");
            return;
        }
        if (hop != null)
        {
            updatePriceForHop(hop, newPrice);
        }
        else if (flightTrip != null)
        {
            updatePriceForFlightTrip(flightTrip, newPrice);
        }   
    }


    public void updatePriceForFlightTrip(FlightTrip flightTrip, double newPrice) 
    {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        flightTrip.setPrice(newPrice);

        try 
        {
            flightSearchService.UpdateFlightTrip(flightTrip);
            errorLabel.setText("Flight trip price updated successfully.");
            closeWindow();

        } 
        catch (Exception e) 
        {
            errorLabel.setText("Error updating flight trip price.");
            e.printStackTrace();
        }

    }

    public void updatePriceForHop(Hop hop, double newPrice) 
    {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        hop.setPrice(newPrice);

        try 
        {
            flightSearchService.UpdateHop(hop);
            flightSearchService.UpdateFlightTripPrice(hop);
            errorLabel.setText("Hop price updated successfully.");
            closeWindow();
        } 
        catch (Exception e) 
        {
            errorLabel.setText("Error updating hop price.");
            e.printStackTrace();
        }
    }






    @FXML
    public void onSeatAvailabilityChanges()
    {

        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        CreateTravellerService createTravellerService = new CreateTravellerService(userService.getUserDAO().getConnection());

        Seat seat = null;

        String selectedSeatLocation = SeatComboBox.getValue();
        if (selectedSeatLocation != null && !selectedSeatLocation.isEmpty() && hop != null) 
        {
            int seatID = flightSearchService.getSeatIdByHop(hop, selectedSeatLocation);
            try
            {
                seat = seatReservationService.getSeatById(seatID);
                if (seat.getAvailability() == false) 
                {
                    createTravellerService.DeleteTraveller(flightTrip, seat);
                    seat.setAvailability(true);
                }
                else
                {
                    seatReservationService.SeatReservation(seatID);
                    seat.setAvailability(false);
                }
            }
            catch (Exception e)
            {
                errorLabel.setText("Error retrieving seat information.");
                e.printStackTrace();
                return;
            }
        }
        setAvailabilityLabel(seat);

    }




    public void closeWindow() {
        if (stage != null) {
            adminDashboardController.getMainBorderPane().setDisable(false);
            adminDashboardController.onSearch();
            stage.close();
        }
    }

}


