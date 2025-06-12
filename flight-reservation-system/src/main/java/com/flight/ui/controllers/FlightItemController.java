package com.flight.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import com.flight.model.FlightTrip;
import com.flight.model.FlightTripInfo;
import com.flight.dao.AirlineCompanyDAO;
import com.flight.dao.AirplaneDAO;
import com.flight.dao.AirportDAO;
import com.flight.dao.FlightTripDAO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import com.flight.model.Seat;
import com.flight.model.User;
import com.flight.service.UserService;

import javafx.stage.Stage;


public class FlightItemController {
    @FXML private Label DepartureAirport;
    @FXML private Label ArrivalAirport;
    @FXML private Label DepartureHour;
    @FXML private Label ArrivalHour;
    @FXML private Label DepartureDate;
    @FXML private Label ArrivalDate;
    @FXML private Label PriceLabel;
    @FXML private Label NumberOfStepovers;
    @FXML private Label SeatNumber;
    @FXML private Label LabelCompany;
    @FXML private Label DepartureZIP;
    @FXML private Label ArrivalZIP;
    @FXML private Button detailButton;

    private Stage stage;
    private UserService userService;
    private FlightTrip flightTrip;
    // Define if the flight is an upper level flight or a hop of the principal flight
    private Boolean upperLevelFlight = true;
    // Define the state of the flight item, used to manage the visibility of the details button
    // 0: Search, 1: Booked Flights, 2: Reservations, 3: Profile
    private int state;
    private int Traveller_Id = -1; // ID of the traveller, used to link the flight item to a specific traveller
    private DashboardController dashboardController;

    public void setData(FlightTrip trip, Connection conn, Seat seat, Stage stage, UserService userService, int state, int Traveller_Id, DashboardController dashboardController) throws Exception
    {
        this.dashboardController = dashboardController; // Store the dashboard controller for later use
        this.Traveller_Id = Traveller_Id; // Set the traveller ID
        this.userService = userService;
        this.stage = stage;
        this.state = state; // Store the state for later use

        AirportDAO airportDAO = new AirportDAO(conn);
        AirplaneDAO airplaneDAO = new AirplaneDAO(conn);
        AirlineCompanyDAO airlineCompanyDAO = new AirlineCompanyDAO(conn);



        DepartureAirport.setText(airportDAO.findById(trip.getDepartAirport()).getAirportName());
        ArrivalAirport.setText(airportDAO.findById(trip.getArrivalAirport()).getAirportName());


        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        DepartureHour.setText(hourFormat.format(trip.getDepartTime().getTime()));
        ArrivalHour.setText(hourFormat.format(trip.getArrivalTime().getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        DepartureDate.setText(dateFormat.format(trip.getDepartTime().getTime()));
        ArrivalDate.setText(dateFormat.format(trip.getArrivalTime().getTime()));

        PriceLabel.setText(trip.getPrice() + "€");
        NumberOfStepovers.setText(String.valueOf(trip.getHops().size() - 1));

        SeatNumber.setText(seat == null ? "N/A" : seat.getLocation());
        LabelCompany.setText(airlineCompanyDAO.findById(airplaneDAO.findById(trip.getHops().get(0).getAirplaneId()).getAirlineCompanyId()).getName());

        DepartureZIP.setText(airportDAO.findById(trip.getDepartAirport()).getZip());
        ArrivalZIP.setText(airportDAO.findById(trip.getArrivalAirport()).getZip());

        this.flightTrip = trip; // Store the flight trip for later use
    }

    public void setData(FlightTrip trip, Connection conn, Seat seat, Stage stage, UserService userService, int state) throws Exception
    {
        this.userService = userService;
        this.stage = stage;
        this.state = state; // Store the state for later use

        AirportDAO airportDAO = new AirportDAO(conn);
        AirplaneDAO airplaneDAO = new AirplaneDAO(conn);
        AirlineCompanyDAO airlineCompanyDAO = new AirlineCompanyDAO(conn);



        DepartureAirport.setText(airportDAO.findById(trip.getDepartAirport()).getAirportName());
        ArrivalAirport.setText(airportDAO.findById(trip.getArrivalAirport()).getAirportName());


        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        DepartureHour.setText(hourFormat.format(trip.getDepartTime().getTime()));
        ArrivalHour.setText(hourFormat.format(trip.getArrivalTime().getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        DepartureDate.setText(dateFormat.format(trip.getDepartTime().getTime()));
        ArrivalDate.setText(dateFormat.format(trip.getArrivalTime().getTime()));

        PriceLabel.setText(trip.getPrice() + "€");
        NumberOfStepovers.setText(String.valueOf(trip.getHops().size() - 1));

        SeatNumber.setText(seat == null ? "N/A" : seat.getLocation());
        LabelCompany.setText(airlineCompanyDAO.findById(airplaneDAO.findById(trip.getHops().get(0).getAirplaneId()).getAirlineCompanyId()).getName());

        DepartureZIP.setText(airportDAO.findById(trip.getDepartAirport()).getZip());
        ArrivalZIP.setText(airportDAO.findById(trip.getArrivalAirport()).getZip());

        this.flightTrip = trip; // Store the flight trip for later use
    }

    public void setData(FlightTrip trip, Connection conn, Seat seat, Stage stage, UserService userService, int state, int Traveller_Id) throws Exception
    {
        this.Traveller_Id = Traveller_Id; // Set the traveller ID
        this.userService = userService;
        this.stage = stage;
        this.state = state; // Store the state for later use

        AirportDAO airportDAO = new AirportDAO(conn);
        AirplaneDAO airplaneDAO = new AirplaneDAO(conn);
        AirlineCompanyDAO airlineCompanyDAO = new AirlineCompanyDAO(conn);



        DepartureAirport.setText(airportDAO.findById(trip.getDepartAirport()).getAirportName());
        ArrivalAirport.setText(airportDAO.findById(trip.getArrivalAirport()).getAirportName());


        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        DepartureHour.setText(hourFormat.format(trip.getDepartTime().getTime()));
        ArrivalHour.setText(hourFormat.format(trip.getArrivalTime().getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        DepartureDate.setText(dateFormat.format(trip.getDepartTime().getTime()));
        ArrivalDate.setText(dateFormat.format(trip.getArrivalTime().getTime()));

        PriceLabel.setText(trip.getPrice() + "€");
        NumberOfStepovers.setText(String.valueOf(trip.getHops().size() - 1));

        SeatNumber.setText(seat == null ? "N/A" : seat.getLocation());
        LabelCompany.setText(airlineCompanyDAO.findById(airplaneDAO.findById(trip.getHops().get(0).getAirplaneId()).getAirlineCompanyId()).getName());

        DepartureZIP.setText(airportDAO.findById(trip.getDepartAirport()).getZip());
        ArrivalZIP.setText(airportDAO.findById(trip.getArrivalAirport()).getZip());

        this.flightTrip = trip; // Store the flight trip for later use
    }

    @FXML
    public void OnReservationDetails()
    {
        // This method can be used to show more details about the reservation
        // For example, open a new window with detailed information
        //DashboardController.setVisible(false);
        System.out.println("OnReservationDetails called");
        loadDetails();


    }

    private void loadDetails() {  
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flight_details.fxml"));
            Scene scene = new Scene(loader.load());
            Scene currentScene = stage.getScene();
            stage.setScene(scene);
            FlightDetailsController ctrl = loader.getController();
            ctrl.init(userService, stage, this, currentScene); // passe l’utilisateur
            
        } catch (Exception e) { e.printStackTrace(); }
    }


    public Label getDepartureAirport() {
        return DepartureAirport;
    }

    public void setDepartureAirport(Label departureAirport) {
        DepartureAirport = departureAirport;
    }

    public Label getArrivalAirport() {
        return ArrivalAirport;
    }

    public void setArrivalAirport(Label arrivalAirport) {
        ArrivalAirport = arrivalAirport;
    }

    public Label getDepartureHour() {
        return DepartureHour;
    }

    public void setDepartureHour(Label departureHour) {
        DepartureHour = departureHour;
    }

    public Label getArrivalHour() {
        return ArrivalHour;
    }

    public void setArrivalHour(Label arrivalHour) {
        ArrivalHour = arrivalHour;
    }

    public Label getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(Label departureDate) {
        DepartureDate = departureDate;
    }

    public Label getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(Label arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public Label getPriceLabel() {
        return PriceLabel;
    }

    public void setPriceLabel(Label priceLabel) {
        PriceLabel = priceLabel;
    }

    public Label getNumberOfStepovers() {
        return NumberOfStepovers;
    }

    public void setNumberOfStepovers(Label numberOfStepovers) {
        NumberOfStepovers = numberOfStepovers;
    }

    public Label getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(Label seatNumber) {
        SeatNumber = seatNumber;
    }

    public void setSeatNumberText(String seatNumber) {
        this.SeatNumber.setText(seatNumber);
    }

    public Label getLabelCompany() {
        return LabelCompany;
    }

    public void setLabelCompany(Label labelCompany) {
        LabelCompany = labelCompany;
    }

    public Label getDepartureZIP() {
        return DepartureZIP;
    }

    public void setDepartureZIP(Label departureZIP) {
        DepartureZIP = departureZIP;
    }

    public Label getArrivalZIP() {
        return ArrivalZIP;
    }

    public void setArrivalZIP(Label arrivalZIP) {
        ArrivalZIP = arrivalZIP;
    }



    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public FlightTrip getFlightTrip() {
        return flightTrip;
    }

    public void setFlightTrip(FlightTrip flightTrip) {
        this.flightTrip = flightTrip;
    }

    public Button getDetailButton() {
        return detailButton;
    }

    public void setDetailButton(Button detailButton) {
        this.detailButton = detailButton;
    }

    public Boolean getUpperLevelFlight() {
        return upperLevelFlight;
    }
    public void setUpperLevelFlight(Boolean upperLevelFlight) {
        this.upperLevelFlight = upperLevelFlight;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getTraveller_Id() {
        return Traveller_Id;
    }
    public void setTraveller_Id(int traveller_Id) {
        Traveller_Id = traveller_Id;
    }
    public DashboardController getDashboardController() {
        return dashboardController;
    }
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

}
