package com.flight.ui.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.core.util.SystemClock;
import org.controlsfx.control.textfield.TextFields;

import com.flight.model.User;
import com.flight.service.CompanyService;
import com.flight.service.FlightSearchService;
import com.flight.service.SeatReservationService;
import com.flight.service.UserService;
import com.flight.dao.HopDAO;
import com.flight.model.Hop;
import com.flight.model.AirlineCompany;
import com.flight.model.Airplane;
import com.flight.model.FlightTrip;
import java.util.List;

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


public class AdminDashboardController {

    private Stage stage;
    private User currentUser;
    private UserService userService;


    private boolean menuVisible = false;
    @FXML private VBox sideMenu;


    @FXML private BorderPane mainBorderPane;
    

    // FXML ELEMENTS FOR SEARCH PANEL
    @FXML private  GridPane SearchGridPane;
    @FXML private TextField DepartTextField;
    @FXML private TextField ArrivalTextField;
    @FXML private DatePicker DatePicker;
    @FXML private Slider PriceSlider;
    @FXML private Slider SeatSlider;
    @FXML private Label SeatSlideLabel;
    @FXML private Label PriceSlideLabel;

    @FXML private ScrollPane FlightsScrollPane;
    @FXML private Label ErrorSearchLabel;




    @FXML private GridPane addFlightPanel;
    @FXML private TextField DepartureLabel;
    @FXML private TextField ArrivalLabel;
    @FXML private DatePicker DepartureDateLabel;
    @FXML private DatePicker ArrivalDateLabel;
    @FXML private Spinner<LocalTime> departSpinner;
    @FXML private Spinner<LocalTime> arrivalSpinner;
    @FXML private ComboBox<String> AirplaneComboBox;
    @FXML private ComboBox<String> CompanyComboBox;
    @FXML private Spinner<Double> PriceSpinner;
    @FXML private Label errorLabel;







    public void init( User currentUser, UserService userService, Stage stage) {
       
        this.currentUser = currentUser;
        this.userService = userService;
        this.stage = stage;

        hideAll();


    }


    public void setUpTimePicker()
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

        departSpinner.setValueFactory(valueFactory);
        arrivalSpinner.setValueFactory(valueFactory);
            
        PriceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 100, 0.5));


        DepartureDateLabel.setValue(LocalDate.now());
        ArrivalDateLabel.setValue(LocalDate.now().plusDays(1)); // Default to the next day
        
    }

    public void setUpComboBoxes()
    {
        // Set up the Airplane and Company ComboBoxes
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        CompanyService  companyService = new CompanyService(userService.getUserDAO().getConnection());

        List<AirlineCompany> companies = companyService.getAllCompanies();
        List<String> companyNames = companies.stream()
            .map(AirlineCompany::getName)
            .toList(); // Extract company names
        CompanyComboBox.getItems().addAll(companyNames);

        // Add listener to update AirplaneComboBox when company selection changes
        CompanyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
            // Clear previous airplanes
            AirplaneComboBox.getItems().clear();
            // Get airplanes for the selected company

            AirlineCompany selectedCompany = companyService.getCompanyByName(newVal);


            LocalTime Dtime = departSpinner.getValue();
            LocalDate Ddate = DepartureDateLabel.getValue();

            LocalTime Atime = arrivalSpinner.getValue();
            LocalDate Adate = ArrivalDateLabel.getValue();

            LocalDateTime DdateTime = LocalDateTime.of(Ddate, Dtime);
            Timestamp Dtimestamp = Timestamp.valueOf(DdateTime);

            LocalDateTime AdateTime = LocalDateTime.of(Adate, Atime);
            Timestamp Atimestamp = Timestamp.valueOf(AdateTime);




            List<Airplane> airplanesForCompany = companyService.getAirplanesByCompanyBetweenDates(selectedCompany, Dtimestamp.toString(), Atimestamp.toString());
            
            for(Airplane airplane : airplanesForCompany) {
                AirplaneComboBox.getItems().add(String.valueOf(airplane.getAirplaneId()));
            }
            if (!airplanesForCompany.isEmpty()) {
                AirplaneComboBox.setValue(String.valueOf(airplanesForCompany.get(0).getAirlineCompanyId()));
            }
            }
        });


        if (!companies.isEmpty()) {
            CompanyComboBox.setValue(companies.get(0).getName()); // Set default value
        }
    }



    public void hideAll()
    {
        // Hide all elements in the dashboard
        SearchGridPane.setVisible(false);
        addFlightPanel.setVisible(false);
        
    }


    public void showSearchPanel() {
        // Show the search panel
        SearchGridPane.setVisible(true);
        
    }

    public void showAddFlightPanel() {
        // Show the add flight panel
        addFlightPanel.setVisible(true);
    }

    @FXML
    public void onAddFlight() {
        // Logic to handle adding a flight
        System.out.println("Add Flight button clicked");
        hideAll();
        showAddFlightPanel();
        setupAutoCompletion();
        setUpTimePicker();
        setUpComboBoxes();
    }

    @FXML 
    public void onAddFlightToDatabase()
    {
        // Logic to handle adding a flight to the database
        System.out.println("Add Flight to Database button clicked");


        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        SeatReservationService seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        String departure = DepartureLabel.getText();
        String arrival = ArrivalLabel.getText();

        LocalTime departTime = departSpinner.getValue();
        LocalDate departDate = DepartureDateLabel.getValue();
        LocalTime arrivalTime = arrivalSpinner.getValue();
        LocalDate arrivalDate = ArrivalDateLabel.getValue();
        Timestamp departTimestamp = Timestamp.valueOf(departDate.atTime(departTime));
        Timestamp arrivalTimestamp = Timestamp.valueOf(arrivalDate.atTime(arrivalTime));
        String airplaneId = AirplaneComboBox.getValue();
        String companyName = CompanyComboBox.getValue();
        double price = PriceSpinner.getValue();

        if (departure.isEmpty() || arrival.isEmpty() || departTimestamp == null || arrivalTimestamp == null || airplaneId == null || companyName == null) {
            errorLabel.setText("Please fill in all fields.");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Hop hop = new Hop();
        hop.setDepartTime(departTimestamp);
        hop.setArrivalTime(arrivalTimestamp);
        hop.setDepart(flightSearchService.getAirportIdByName(departure));
        hop.setArrive(flightSearchService.getAirportIdByName(arrival));
        hop.setPrice(price);
        hop.setAirplaneId(Integer.parseInt(airplaneId));
        

        if (flightSearchService.isHopExists(hop)) {
            errorLabel.setText("This flight already exists.");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }


        flightSearchService.addHop(hop);
        errorLabel.setText("Flight added successfully.");
        errorLabel.setStyle("-fx-text-fill: green;");

        setUpComboBoxes();
        
        
    }

    @FXML
    public void onUpdateFlights() {
        // Logic to handle updating flights
        System.out.println("Update Flights button clicked");
        hideAll();
        showSearchPanel();
        setupAutoCompletion();

    }

    @FXML
    public void onViewBookings() {

        // Logic to handle viewing bookings
        System.out.println("View Bookings button clicked");
    }

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

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }



    @FXML
    public void onSearch() {
        // Logic to handle searching for flights
        System.out.println("Search button clicked");


        setupAutoCompletion();


        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());



        String departure = DepartTextField.getText();
        String arrival = ArrivalTextField.getText();
        String date = DatePicker.getValue() != null ? DatePicker.getValue().toString() : null;
        double maxPrice = PriceSlider.getValue();
        int maxSeats = (int) SeatSlider.getValue();

        if (maxPrice == 0)
        {
            maxPrice = PriceSlider.getMax(); // If no price is set, use the maximum price
        }
        if (maxSeats == 0)
        {
            maxSeats = (int) SeatSlider.getMax(); // If no seats are set, use the maximum seats
        }

        if (date == null || date.isEmpty()) {
            LocalDate myObj = LocalDate.now(); // Create a date object        
            Timestamp timestamp = Timestamp.valueOf(myObj.atStartOfDay());
            date = timestamp.toString();
            System.out.println("Date not provided, using current date: " + date);
        }


        int departureId = flightSearchService.getAirportIdByName(departure);
        int arrivalId = flightSearchService.getAirportIdByName(arrival);
        if (departureId == -1 || arrivalId == -1) {
            ErrorSearchLabel.setText("Invalid departure or arrival airport.");
            return;
        }





        List<FlightTrip> filteredFlightTrips = flightSearchService.getNonSingletonFlightTrips(departureId, arrivalId, date, maxPrice, maxSeats);
        List<Hop> hops = flightSearchService.getAllHops(date, maxSeats, maxPrice, departureId, arrivalId);

        setFlights(filteredFlightTrips, hops);



    }

    private void setFlights(List<FlightTrip> flightTrips, List<Hop> hops) {
        // Clear previous results
        FlightsScrollPane.setContent(null);
        ErrorSearchLabel.setText("");


        // Create a VBox to hold the flight details
        VBox flightsVBox = new VBox();
        flightsVBox.setSpacing(10);

        for (FlightTrip flightTrip : flightTrips) {
            // Create a label for each flight trip
            try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_flight_details.fxml"));
                    GridPane item = loader.load();

                    AdminFlightDetailsController controller = loader.getController();
                    controller.init(currentUser, userService, stage);
                    controller.setData(flightTrip, this);

                    flightsVBox.getChildren().add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }

        for (Hop hop : hops) {
            try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_flight_details.fxml"));
                    GridPane item = loader.load();

                    AdminFlightDetailsController controller = loader.getController();
                    controller.init(currentUser, userService, stage);
                    controller.setData(hop, this);

                    flightsVBox.getChildren().add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }



        // Set the content of the ScrollPane to the VBox
        FlightsScrollPane.setContent(flightsVBox);
    }


    public void setupAutoCompletion() {
        // Setup auto-completion for the departure and arrival text fields
        FlightSearchService flightSearchService = new FlightSearchService(userService.getUserDAO().getConnection());
        SeatReservationService  seatReservationService = new SeatReservationService(userService.getUserDAO().getConnection());
        List<String> airportNames = flightSearchService.getAllAirportsName(); // méthode à écrire dans AirportDAO
        TextFields.bindAutoCompletion(DepartTextField, airportNames);
        TextFields.bindAutoCompletion(ArrivalTextField, airportNames);

        SeatSlider.setMinorTickCount(1);
        SeatSlider.setMajorTickUnit(1);


        double maxSeats = (double) seatReservationService.getMaxSeatsByAirplane();
        SeatSlider.setMax(maxSeats);
        SeatSlider.setMin(0);


        PriceSlider.setMax(flightSearchService.getMaxPriceFlightTripHop());
        PriceSlider.setMin(0);

        SeatSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SeatSlideLabel.setText(String.format("%.0f", newVal.doubleValue()));
        }); 

        PriceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            PriceSlideLabel.setText(String.format("%.0f", newVal.doubleValue()));
        });



        TextFields.bindAutoCompletion(DepartureLabel, airportNames);
        TextFields.bindAutoCompletion(ArrivalLabel, airportNames);

    }

}
