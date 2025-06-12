package com.flight.model;

import com.flight.model.FlightTrip;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FlightTripTest {
    
    @Test
    void gettersSetters() {
        FlightTrip flightTrip = new FlightTrip();
        flightTrip.setFlightTripId(1);
        flightTrip.setDepartAirport(2);
        flightTrip.setArrivalAirport(5);


        LocalDate AlocalDate = LocalDate.of(2025, 4, 28);
        // Convertir en java.util.Date
        Date Adate = Date.from(AlocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp Atimestamp = new Timestamp(Adate.getTime());

        flightTrip.setArrivalTime(Atimestamp);
//////////////////////////////////////////////////////////////////

        LocalDate DlocalDate = LocalDate.of(2025, 4, 27);

        Date Ddate = Date.from(DlocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp Dtimestamp = new Timestamp(Ddate.getTime());

        flightTrip.setDepartTime(Dtimestamp);
        System.out.println("\n Depart Time: " + flightTrip.getDepartTime()+ "\n");
        System.out.println("\n Arrival Time: " + flightTrip.getArrivalTime()+ "\n");


        flightTrip.setDistance(1500.0);
        // Test getters and setters
        assertEquals(1, flightTrip.getFlightTripId());
        assertEquals(2, flightTrip.getDepartAirport());
        assertEquals(5, flightTrip.getArrivalAirport());
        assertEquals("2025-04-27 00:00:00.0", flightTrip.getDepartTime().toString());
        assertEquals("2025-04-28 00:00:00.0", flightTrip.getArrivalTime().toString());
        assertEquals(1500.0, flightTrip.getDistance());
    }

}
