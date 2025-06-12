package com.flight.model;

import com.flight.model.Airplane;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class AirplaneTest 
{

    @Test
    void gettersSetters() 
    {
        Airplane airplane = new Airplane();
        airplane.setAirplaneId(1);
        airplane.setSeatingCapacity(400);
        airplane.setAirlineCompanyId(100);

        // Test getters and setters
        assertEquals(1, airplane.getAirplaneId());
        assertEquals(400, airplane.getSeatingCapacity());
        assertEquals(100, airplane.getAirlineCompanyId());

    }      



    
}
