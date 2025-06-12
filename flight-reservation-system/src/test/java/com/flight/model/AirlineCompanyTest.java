package com.flight.model;
import com.flight.model.AirlineCompany;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class AirlineCompanyTest 
{
    @Test 
    void gettersSetters() 
    {
        AirlineCompany ac = new AirlineCompany();
        ac.setCompanyId(42);
        ac.setName("TestAir");
    
        // Test getters et setters  
        assertEquals(42, ac.getCompanyId());
        assertEquals("TestAir", ac.getName());
    }

    
}
