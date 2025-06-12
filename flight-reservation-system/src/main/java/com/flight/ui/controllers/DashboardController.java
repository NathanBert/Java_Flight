package com.flight.ui.controllers;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.flight.service.FlightSearchService;
import com.flight.service.UserService;
import java.sql.Connection;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.flight.ui.controllers.FlightItemController;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

import org.controlsfx.control.textfield.TextFields;

import javafx.scene.layout.*;
import com.flight.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.scene.Scene;

import com.flight.dao.*;

public class DashboardController {
    @FXML private BorderPane root;
    @FXML private Pane contentPane;
    private Stage stage;
    private User currentUser;
    private UserService userService;
    private boolean menuVisible = false;
    //private boolean menuAccountVisible = false;


    @FXML private VBox reservationContainer;
    @FXML private VBox bookedContainer;

    @FXML private Button menuButton;
    //@FXML private Button accountMenu;
    @FXML private VBox sideMenu;
    @FXML private ScrollPane scrollPaneReservation;  //ScrollPane that contains the results of the search
    @FXML private ScrollPane scrollPaneBooked;  //ScrollPane that contains the booked flights
    @FXML private Label searchErrorLabel;
    @FXML private Label labelNoBookedFlight;

    
    @FXML private TextField departField; 
    @FXML private TextField arrivalField;
    @FXML private GridPane SearchPane;
    @FXML private StackPane contentArea;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<LocalTime> timeSpinner;
    @FXML private Spinner<Integer> numberSpinner;

    private int state = 0; // 0: Search, 1: Booked Flights, 2: Reservations, 3: Profile

    // services passés depuis LoginController

    public void hideDashBoard()
    {
        root.setVisible(false);
    }
    public void showDashBoard()
    {
        root.setVisible(true);
    }

    public void init( User currentUser, UserService userService, Stage stage) {
       
        this.currentUser = currentUser;
        this.userService = userService;
        this.stage = stage;
        hideAll();
        
    }

    public void showSearchPane()
    {
        SearchPane.setVisible(true);
    }

    public void hideSearchPane()
    {
        SearchPane.setVisible(false);
    }

    public void hideAll()
    {
        hideSearchPane();
        hideReservations();
        hideBookedFlights();
        
    }


    @FXML
    public void onSearchFlight() 
    { 
        state = 2; // Set state to Search
        setupTimePicker();
        setupAutoCompletion();
        hideAll();
        showSearchPane();

    }
    @FXML
    public void onMyBookings()throws Exception 
    {  
        hideAll();
        showBookedFlights();
        loadReservations();


    }
    private void showBookedFlights() {
        bookedContainer.setVisible(true);
        scrollPaneBooked.setVisible(true);
    }
    private void hideBookedFlights() {
        bookedContainer.setVisible(false);
        scrollPaneBooked.setVisible(false);
        labelNoBookedFlight.setVisible(false);
    }
    private void showReservations() {
        reservationContainer.setVisible(true);
        scrollPaneReservation.setVisible(true);



    }
    private void hideReservations() {
        reservationContainer.setVisible(false);
        scrollPaneReservation.setVisible(false);
    }
    public void loadReservations() throws Exception{
        state = 1; // Set state to Booked Flights
        hideAll();
        showBookedFlights();
        FlightTripDAO flightTripDAO = new FlightTripDAO(userService.getUserDAO().getConnection());

        List<FlightTripInfo> reservations = flightTripDAO.getFlightTripInfosByUserId(currentUser.getUserId());
        //Suppression des doublons dans la liste des réservations
        reservations = removeDuplicatesByTravellerId(reservations);


        System.out.println("Reservations: " + reservations.size());
        if (reservations.isEmpty()) {
            labelNoBookedFlight.setVisible(true);
            bookedContainer.getChildren().clear(); // Clear previous results
            return;
        } else {
            labelNoBookedFlight.setVisible(false);
            bookedContainer.getChildren().clear(); // Clear previous results
            for (FlightTripInfo trip : reservations) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flight.fxml"));
                    GridPane item = loader.load();

                    FlightItemController controller = loader.getController();
                    controller.setData(trip.getFlightTrip(), userService.getUserDAO().getConnection(), null, stage, userService, state, trip.getTraveller_Id(), this);
                    controller.setSeatNumberText(buildSeatNumberList(trip));


                    bookedContainer.getChildren().add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            
            }
            //showReservations();

        }
    }


    public static List<FlightTripInfo> removeDuplicatesByTravellerId(List<FlightTripInfo> infos) {
    Set<Integer> seenTravellerIds = new HashSet<>();
    List<FlightTripInfo> uniqueInfos = new ArrayList<>();

    for (FlightTripInfo info : infos) {
        int travellerId = info.getTraveller_Id();
        if (seenTravellerIds.add(travellerId)) {
            uniqueInfos.add(info);
        }
    }

    return uniqueInfos;
    }


    public static String buildSeatNumberList(FlightTripInfo reservations) {
        StringBuilder result = new StringBuilder();
        ListIterator<Seat> iterator = reservations.getSeats().listIterator();

        while (iterator.hasNext()) {
            Seat seat = iterator.next();
            String seatText = seat.getLocation();
            result.append(seatText);

            if (iterator.hasNext()) {
                result.append(", ");
            }
        }

        return result.toString();
    }
    

    @FXML
    public void onProfile() 
    { 
        loadView("/fxml/profile.fxml");
        state = 3; // Set state to Profile
        hideAll();

    }
    @FXML
    public void onLogout()
    { 
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

    @FXML 
    public void OnSearch()
    {
        String departure = departField.getText();
        String arrival = arrivalField.getText();
        LocalTime time = timeSpinner.getValue();
        LocalDate date = datePicker.getValue();
        int numberOfPassengers = numberSpinner.getValue();

        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        int departureAirportId = flightSearchService.getAirportIdByName(departure);
        int arrivalAirportId = flightSearchService.getAirportIdByName(arrival);

        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Timestamp Dtimestamp = Timestamp.valueOf(dateTime);

        System.out.println("Departure: " + departureAirportId);
        System.out.println("Arrival: " + arrivalAirportId);
        System.out.println("Date: " + Dtimestamp);
        System.out.println("Number of passengers: " + numberOfPassengers);

        List<FlightTrip> flightTrips = flightSearchService.searchFlights(arrivalAirportId, departureAirportId, null, Dtimestamp, numberOfPassengers, "Economy");

        if (flightTrips==null || flightTrips.size() == 0) {
            searchErrorLabel.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                    event -> searchErrorLabel.setVisible(false)
            );
            visiblePause.play();
            return;
        }
        else{
            hideAll();
            reservationContainer.getChildren().clear(); // Clear previous results
            for (FlightTrip trip : flightTrips) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flight.fxml"));
                    GridPane item = loader.load();

                    FlightItemController controller = loader.getController();
                    controller.setData(trip, userService.getUserDAO().getConnection(), null, stage, userService, state);

                    reservationContainer.getChildren().add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            showReservations();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            contentPane.getChildren().setAll(view);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


/*
    @FXML
    private void toggleAccount() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sideMenuAccount);
        if (!menuAccountVisible) {
            // slide in
            transition.setFromX(180);  // Position initiale hors écran
            transition.setToX(0);       // Position finale visible
            menuAccountVisible = true;
            sideMenuAccount.toFront();

        } else {
            // slide out
            transition.setFromX(0);
            transition.setToX(180);
            menuAccountVisible = false;
        }
        transition.play();
    }

*/

    @FXML
    private void toggleMenu() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sideMenu);
        if (!menuVisible) {
            // slide in
            transition.setFromX(-200);  // Position initiale hors écran
            transition.setToX(0);       // Position finale visible
            menuVisible = true;
            sideMenu.toFront();
        } else {
            // slide out
            transition.setFromX(0);
            transition.setToX(-200);
            menuVisible = false;
        }
        transition.play();
    }
    private void setupAutoCompletion() {
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        List<String> airportNames = flightSearchService.getAllAirportsName(); // méthode à écrire dans AirportDAO
        TextFields.bindAutoCompletion(departField, airportNames);
        TextFields.bindAutoCompletion(arrivalField, airportNames);
    }
    private void setupTimePicker() 
    {
        SpinnerValueFactory<LocalTime> valueFactory =
            new SpinnerValueFactory<LocalTime>() {
                {
                    setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null));
                    setValue(LocalTime.of(8, 0));
                }

                @Override
                public void decrement(int steps) {
                    setValue(getValue().minusMinutes(steps * 15));
                }

                @Override
                public void increment(int steps) {
                    setValue(getValue().plusMinutes(steps * 15));
                }
            };

        timeSpinner.setValueFactory(valueFactory);
    }
    @FXML
    public void initialize() 
    {
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1); // min, max, default
        numberSpinner.setValueFactory(valueFactory);
    }
}
