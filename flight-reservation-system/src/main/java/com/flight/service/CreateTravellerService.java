package com.flight.service;

import com.flight.dao.TravellerDAO;
import com.flight.model.Traveller;

import com.flight.model.User;
import com.flight.model.FlightTrip;
import com.flight.model.Seat;

import java.util.List;

import java.sql.Connection;

public class CreateTravellerService 
{

    TravellerDAO travellerDAO;

    public CreateTravellerService(Connection conn)
    {
        travellerDAO = new TravellerDAO(conn);
    }


    /**
     * Create a Traveller and update all the linked informations, Seats availability, Airplane capacity...
     * 
    */ 
    public void NewTraveller(User user, Seat seat, FlightTrip flight, int step)
    {
        try
        {
            travellerDAO.insert(user, seat,flight,step);
            //SeatReservationService seatReservation = new SeatReservationService(travellerDAO.getConnection());
            //seatReservation.SeatReservation(seat.getSeatId());
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }


    public void DeleteTraveller(FlightTrip flight, Seat seat)
    {
        try
        {
            travellerDAO.delete(flight, seat);
            SeatReservationService seatReservation = new SeatReservationService(travellerDAO.getConnection());
            seatReservation.SeatCancel(seat.getSeatId());
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public void DeleteTravellerBySeatId(int seatId)
    {
        try
        {
            travellerDAO.deleteBySeatId(seatId);
            SeatReservationService seatReservation = new SeatReservationService(travellerDAO.getConnection());
            seatReservation.SeatCancel(seatId);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }
}
