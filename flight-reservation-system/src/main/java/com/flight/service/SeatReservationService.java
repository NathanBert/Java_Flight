package com.flight.service;

import com.flight.dao.AirplaneDAO;
import com.flight.dao.HopDAO;
import com.flight.dao.SeatDAO;
import com.flight.model.Seat;
import com.flight.model.Airplane;
import com.flight.model.Hop;
import com.flight.model.FlightTrip;



import java.sql.Connection;
import java.util.List;

public class SeatReservationService 
{
    SeatDAO seatDAO;
    
    public SeatReservationService(Connection conn)
    {
        seatDAO = new SeatDAO(conn);
    }

    public void SeatReservation(int seatId)
    {
        try
        {
            Seat seat = seatDAO.findById(seatId);
            seat.setAvailability(false); // Set Seat availability to false
            seatDAO.update(seat); // Update the seat in the database
            // update the plane capacity because ofthe new traveller registered
            AirplaneDAO airplaneDAO = new AirplaneDAO(seatDAO.getConn());
            Airplane airplane = airplaneDAO.findById(seat.getAirplaneId());
            airplane.setSeatingCapacity(airplane.getSeatingCapacity()-1);
            airplaneDAO.update(airplane);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }
    

    public void SeatCancel(int seatId)
    {
        try
        {
            Seat seat = seatDAO.findById(seatId);
            seat.setAvailability(true); // Set Seat availability to false
            seatDAO.update(seat); // Update the seat in the database
            // update the plane capacity because ofthe new traveller registered
            AirplaneDAO airplaneDAO = new AirplaneDAO(seatDAO.getConn());
            Airplane airplane = airplaneDAO.findById(seat.getAirplaneId());
            airplane.setSeatingCapacity(airplane.getSeatingCapacity()+1);
            airplaneDAO.update(airplane);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }


    public List<Seat> getAvailableSeatsByAirplaneId(int airplaneId) throws Exception {
        return seatDAO.findByAirplaneId(airplaneId);
    }
    public List<Seat> getAvailableSeatsByFlightTrip(FlightTrip flightTrip) throws Exception {
        AirplaneDAO airplaneDAO = new AirplaneDAO(seatDAO.getConn());
        HopDAO hopDAO = new HopDAO(seatDAO.getConn());
        Hop hop = hopDAO.findByHopid(flightTrip.getHops().get(0).getHopId());

        Airplane airplane = airplaneDAO.findById(hop.getAirplaneId());
        return seatDAO.findByAirplaneId(airplane.getAirplaneId());
    }

    public int getSeatIdByLocation(String location,  int airplane_id) throws Exception {
        
        int seatId = seatDAO.getSeatIdByLocation(location, airplane_id);
        return seatId;
    }

    public Seat getSeatById(int seatId) throws Exception 
    {
        return seatDAO.findById(seatId);
    }


    public int getMaxSeatsByAirplane()
    {
        try {
            AirplaneDAO airplaneDAO = new AirplaneDAO(seatDAO.getConn());
            List<Airplane> airplanes = airplaneDAO.findAll();
            int max = 0;
            for (Airplane airplane : airplanes) {
                if (airplane.getSeatingCapacity() > max) {
                    max = airplane.getSeatingCapacity();
                }
            }
            return max;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;   
    }



    public SeatDAO getSeatDAO() {
        return seatDAO;
    }


}
