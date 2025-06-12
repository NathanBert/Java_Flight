package com.flight.model;

import com.flight.model.Seat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SeatTest 
{
    @Test 
    void gettersSetters() 
    {
        Seat s = new Seat();
        s.setSeatId(42);
        s.setAvailability(true);
        s.setAirplaneId(1);
        s.setLocation("12A");
        s.setSeatClass("Economy");

        // Test getters et setters  
        assertEquals(42, s.getSeatId());
        assertEquals("Economy", s.getSeatClass());
        assertEquals("12A", s.getLocation());
        assertEquals(1, s.getAirplaneId());
        assertTrue(s.isAvailability());
    }
    
}
