
package com.flight.model;
import com.flight.model.Airport;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class AirportTest 
{
  @Test 
  void gettersSetters() 
  {
    Airport a = new Airport();
    a.setAirportId(42);
    a.setAirportName("TestAir");
    a.setZip("12345");
    a.setLatitude(12.34);
    a.setLongitude(56.78);
    a.setCity("TestCity");
    a.setCountry("TestCountry");

    // Test getters et setters  
    assertEquals(42, a.getAirportId());
    assertEquals("TestAir", a.getAirportName());
    assertEquals("12345", a.getZip());
    assertEquals(12.34, a.getLatitude());
    assertEquals(56.78, a.getLongitude());
    assertEquals("TestCity", a.getCity());
    assertEquals("TestCountry", a.getCountry());
  }



  
}
