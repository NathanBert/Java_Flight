package com.flight.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.flight.model.Hop;
import com.flight.service.CreateTravellerService;
import com.flight.service.FlightSearchService;
import com.flight.service.SeatReservationService;
import com.flight.service.UserService;
import com.flight.model.Seat;
import com.flight.model.Traveller;

import java.util.List;
import java.util.ArrayList;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.flight.dao.FlightTripDAO;
import com.flight.dao.SeatDAO;
import com.flight.dao.TravellerDAO;
import com.flight.model.FlightTrip;
import com.flight.model.User;

public class FlightDetailsController 
{
    // This class can be used to handle flight details logic
    // It can include methods to fetch and display detailed information about a flight trip
    // For example, it can retrieve flight trip information from the database and display it in the UI
    // Additional methods can be added as needed to support the functionality of the flight reservation system

    private UserService userService;
    private Stage stage;
    private User currentUser;
    private Scene previousScene;

    @FXML private GridPane root;
    @FXML private Label DepartureAirport;
    @FXML private Label ArrivalAirport;
    @FXML private Label DepartureHour;
    @FXML private Label ArrivalHour;
    @FXML private Label DepartureDate;
    @FXML private Label ArrivalDate;
    @FXML private Label PriceLabel;
    @FXML private Label NumberOfStepovers;
    @FXML private Label LabelCompany;
    @FXML private Label DepartureZIP;
    @FXML private Label ArrivalZIP;
    @FXML private TextField seatSelector;
    @FXML private ScrollPane reservationContainer;
    @FXML private Button bookButton;
    @FXML private Label BookingProblemsLabel;
    @FXML private Pane cancelPane;
    @FXML private Button cancelFlightButton;

    private FlightTrip flightTrip;
    private FlightItemController flightItemController;

    private int seatSelected = -1; // Default value indicating no seat is selected
    private VBox reservationBox = new VBox(10);


    


    public void init (UserService userService, Stage stage, FlightItemController flightItemController, Scene previousScene) 
    {
        this.previousScene = previousScene;
        this.flightItemController = flightItemController;

        this.currentUser = userService.getCurrentUser();
        this.userService = userService;
        this.stage = stage;
        this.DepartureAirport.setText(flightItemController.getDepartureAirport().getText());
        this.ArrivalAirport.setText(flightItemController.getArrivalAirport().getText());
        this.DepartureHour.setText(flightItemController.getDepartureHour().getText());
        this.ArrivalHour.setText(flightItemController.getArrivalHour().getText());
        this.DepartureDate.setText(flightItemController.getDepartureDate().getText());
        this.ArrivalDate.setText(flightItemController.getArrivalDate().getText());
        this.PriceLabel.setText(flightItemController.getPriceLabel().getText());
        this.NumberOfStepovers.setText(flightItemController.getNumberOfStepovers().getText());
        this.LabelCompany.setText(flightItemController.getLabelCompany().getText());
        this.DepartureZIP.setText(flightItemController.getDepartureZIP().getText());
        this.ArrivalZIP.setText(flightItemController.getArrivalZIP().getText());


        bookButton.setDisable(!flightItemController.getUpperLevelFlight());
        //Special case of reservations already booked
        if (flightItemController.getState() == 1)
        {
            bookButton.setDisable(true);
            if (flightItemController.getUpperLevelFlight() == true)
            {
                cancelPane.setVisible(true);
            }
            else
            {
                cancelPane.setVisible(false);
            }
        }
        else
        {
            cancelPane.setVisible(false);
        }
        

        flightTrip = flightItemController.getFlightTrip();



        if (NumberOfStepovers.getText().equals("0")) {
            setupAutoCompletion();
            setSingleHop();
            if (flightItemController.getState() == 1)
            {
                seatSelector.setEditable(false);
            }
            else
            {
                seatSelector.setEditable(true);
            }

        } else {
            setHops();

            seatSelector.setEditable(false);
            cancelPane.setVisible(true);

        }

    }

    public void setupAutoCompletion() 
    {
        try 
        {
            SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
            List<Seat> availableSeats = seatReservationService.getAvailableSeatsByFlightTrip(flightTrip);
            List<String> seatLocations = new ArrayList<String>();

            for (Seat seat : availableSeats) {
                if (seat.getAvailability() == true) {
                    seatLocations.add(seat.getLocation());
                }
            }
            AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(seatSelector, seatLocations);
            //TextFields.bindAutoCompletion(seatSelector, seatLocations);
            autoCompletionBinding.setOnAutoCompleted(event -> {
                OnSeatExited();
            }); 




        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setSingleHop()
    {
        FlightSearchService flightTripDAO = new FlightSearchService(userService.getUserDAO().getConnection());
        TravellerDAO travellerDAO = new TravellerDAO(userService.getUserDAO().getConnection());
        SeatDAO seatDAO = new SeatDAO(userService.getUserDAO().getConnection());
        Seat seat = null;
        System.out.println("\nsize of hops: " + flightTrip.getHops().size()+ "\n");
        for (Hop hop : flightTrip.getHops()) {
                try {
                    System.out.println("Loading flight with hop id " + hop.getHopId());

                    FlightTrip currentFlightTrip = flightTripDAO.getFlightTripByHop(hop);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flight.fxml"));
                    GridPane item = loader.load();


           /*          if (flightItemController.getState() == 1)
                    {
                        seat = seatDAO.findById(travellerDAO.findByIds(currentUser.getUserId(), 
                                                                       flightItemController.getFlightTrip().getFlightTripId(), 
                                                                       (flightTrip.getHops().indexOf(hop) + 1),
                                                                       flightItemController.getTraveller_Id())
                                                                       .getSeatId());

                    }

 */


                    System.out.println("Loading flight with flight id " + flightTrip.getFlightTripId() + " and User id " + currentUser.getUserId()
                    + "for step " + (flightTrip.getHops().indexOf(hop) + 1)+"/"+flightTrip.getHops().size() + " and traveller id " + flightItemController.getTraveller_Id());
                    seat = seatDAO.findById(seatDAO.getSeatIdByLocation(flightItemController.getSeatNumber().getText(), 
                                                                        hop.getAirplaneId()));

                    FlightItemController controller = loader.getController();
                    item.setUserData(controller);
                    controller.setData(currentFlightTrip, userService.getUserDAO().getConnection(), seat,  stage, userService, flightItemController.getState(),flightItemController.getTraveller_Id());
                    controller.setUpperLevelFlight(false);
                    // If the reservation has no stepover, disable the detail button to ounable infinite details loop
                    if (controller.getDepartureAirport().getText().equals(DepartureAirport.getText()) 
                        && controller.getArrivalAirport().getText().equals(ArrivalAirport.getText()))
                    {
                        controller.getDetailButton().setDisable(true);
                    }   
                    else
                    {
                        controller.getDetailButton().setDisable(false);
                    }


                    reservationBox.getChildren().add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        reservationContainer.setContent(reservationBox);
        reservationContainer.setFitToWidth(true);

    }

    public void setHops()
    {
        FlightSearchService flightTripDAO = new FlightSearchService(userService.getUserDAO().getConnection());
        TravellerDAO travellerDAO = new TravellerDAO(userService.getUserDAO().getConnection());
        SeatDAO seatDAO = new SeatDAO(userService.getUserDAO().getConnection());
        Seat seat = null;
        System.out.println("\nsize of hops: " + flightTrip.getHops().size()+ "\n");
        for (Hop hop : flightTrip.getHops()) {
                try {
                    System.out.println("Loading flight with hop id " + hop.getHopId());


                    



                    FlightTrip currentFlightTrip = flightTripDAO.getFlightTripByHop(hop);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flight.fxml"));
                    GridPane item = loader.load();


                    System.out.println("Loading flight with flight id " + flightItemController.getFlightTrip().getFlightTripId() + " and User id " + currentUser.getUserId()
                    + "for step " + (flightTrip.getHops().indexOf(hop) + 1)+"/"+flightTrip.getHops().size() + " and traveller id " + flightItemController.getTraveller_Id());
                    if (flightItemController.getState() == 1)
                    {
                        seat = seatDAO.findById(travellerDAO.findByIds(currentUser.getUserId(), 
                                                                       flightItemController.getFlightTrip().getFlightTripId(), 
                                                                       (flightTrip.getHops().indexOf(hop) + 1),
                                                                       flightItemController.getTraveller_Id())
                                                                       .getSeatId());

                    }


                    FlightItemController controller = loader.getController();
                    item.setUserData(controller);
                    controller.setData(currentFlightTrip, userService.getUserDAO().getConnection(), seat,  stage, userService, flightItemController.getState(),flightItemController.getTraveller_Id());
                    controller.setUpperLevelFlight(false);
                    // If the reservation has no stepover, disable the detail button to ounable infinite details loop
                    if (controller.getDepartureAirport().getText().equals(DepartureAirport.getText()) 
                        && controller.getArrivalAirport().getText().equals(ArrivalAirport.getText()))
                    {
                        controller.getDetailButton().setDisable(true);
                    }   
                    else
                    {
                        controller.getDetailButton().setDisable(false);
                    }


                    reservationBox.getChildren().add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        reservationContainer.setContent(reservationBox);
        reservationContainer.setFitToWidth(true);

    }


    @FXML
    public void OnBackToSearch()
    {
        // This method can be used to navigate back to the flight search page
        // For example, it can close the current window and open the flight search window
        System.out.println("OnBackToSearch called");
        //DashboardController.setVisible(true);

        loadBack();
    }


    @FXML
    public void OnReservationDetails()
    {
        // This method can be used to show more details about the reservation
        // For example, open a new window with detailed information
        //DashboardController.setVisible(false);
        System.out.println("OnReservationDetails called");
    }


    @FXML
    public void OnBook()
    {
        try{
            SeatDAO seatDAO = new SeatDAO(userService.getUserDAO().getConnection());
            SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
            CreateTravellerService createTravellerService = new CreateTravellerService(userService.getUserDAO().getConnection());
            FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
            Boolean allSeatsAttributed = true;

            if (reservationContainer.getContent() instanceof VBox) {
                for (int i = 0; i < reservationBox.getChildren().size(); i++) {
                    System.out.println("Checking seat attribution for item " + i);
                    GridPane item = (GridPane) reservationBox.getChildren().get(i);
                    System.out.println("item type: "+item.getTypeSelector());
                    FlightItemController controller = item.getUserData() instanceof FlightItemController ? (FlightItemController) item.getUserData() : null;
                    System.out.println("controller = "+ controller);

                    System.out.println("controller.getSeatNumber().getText().equals(\"N/A\") = " + controller.getSeatNumber().getText().equals("N/A"));
                    if (controller != null && controller.getSeatNumber().getText().equals("N/A")) {
                        allSeatsAttributed = false;
                        BookingProblemsLabel.setText("All seats must be attributed before booking.");
                        break; // No need to check further if one seat is not attributed
                    }
                }
                System.out.println("All seats attributed: " + allSeatsAttributed);
                if (allSeatsAttributed == true)
                {
                    Traveller.incrementTravellerIdCounter();
                    for (int i = 0; i < reservationBox.getChildren().size(); i++) 
                    {
                        GridPane item = (GridPane) reservationBox.getChildren().get(i);
                        FlightItemController controller = item.getUserData() instanceof FlightItemController ? (FlightItemController) item.getUserData() : null;
                        if (controller != null) {
                            System.out.println("Creating traveller for item " + i+ " with USER: " + currentUser.getUserId() + " and FLIGHT: " + flightTrip.getFlightTripId() );

                            Seat seat = seatDAO.findById(seatReservationService.getSeatIdByLocation
                            (controller.getSeatNumber().getText(), flightSearchService.getAirplaneIdByFlightTrip(flightTrip, i)));

                            System.out.println("Seat selected: " + seat.getLocation() + " with ID: " + seat.getSeatId());

                            createTravellerService.NewTraveller(currentUser,
                            seat, 
                            flightTrip,
                            i+1);
                        }
                    }
                    OnBackToSearch();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void OnSeatEntered() 
    {
        System.out.println("Seat entered: " + seatSelector.getText());
    }

    
    public void OnSeatExited() 
    {
        try {
            SeatDAO seatDAO = new SeatDAO(userService.getUserDAO().getConnection());
            
            System.out.println("Seat exited: " + seatSelector.getText());
            String seatSelectedLocation = seatSelector.getText();

            if (seatSelected == -1 || !seatDAO.findById(seatSelected).getLocation().equals(seatSelectedLocation)) 
            {
                    
                    SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
                    FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
                    seatReservationService.SeatCancel(seatSelected);
                    int seatID = seatReservationService.getSeatIdByLocation(seatSelectedLocation, flightSearchService.getAirplaneIdByFlightTrip(flightTrip, 0));
                    seatReservationService.SeatReservation(seatID);
                    seatSelected = seatID;
                    setupAutoCompletion();
                    // Update the seat number on the upper page
                    if (!flightItemController.getUpperLevelFlight())
                    {
                        flightItemController.setSeatNumberText(seatSelectedLocation);
                    }


                    // Update the seat number on the current page (lowest level page)
                    GridPane item = (GridPane) reservationBox.getChildren().get(0);
                    FlightItemController controller = item.getUserData() instanceof FlightItemController ? (FlightItemController) item.getUserData() : null;
                    controller.setSeatNumberText(seatSelectedLocation);

                    System.out.println("Seat reserved: " + seatSelected);
                
            } else {
                System.out.println("Seat Unchanged");
            }
        } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @FXML
    public void OnCancel()
    {
        CreateTravellerService createTravellerService = new CreateTravellerService(userService.getUserDAO().getConnection());
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        SeatDAO seatDAO = new SeatDAO(userService.getUserDAO().getConnection());



        try
        {
            for (int i = 0; i < reservationBox.getChildren().size(); i++) 
                {
                    GridPane item = (GridPane) reservationBox.getChildren().get(i);
                    FlightItemController controller = item.getUserData() instanceof FlightItemController ? (FlightItemController) item.getUserData() : null;
                    if (controller != null) {

                        Seat seat = seatDAO.findById(seatReservationService.getSeatIdByLocation
                        (controller.getSeatNumber().getText(), flightSearchService.getAirplaneIdByFlightTrip(flightTrip, i)));

                        createTravellerService.DeleteTraveller(flightTrip, seat);;
                        flightItemController.getDashboardController().loadReservations();;


                    }
                }

                loadBack();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OnCancel called");
    }


    public void loadBack()
    {
        try {
            stage.setScene(previousScene);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public TextField getSeatSelector() {
        return seatSelector;
    }

    public void disableBookingButton()
    {
        bookButton.setDisable(true);
    }
    
}
