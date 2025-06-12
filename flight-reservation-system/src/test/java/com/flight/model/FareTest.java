
package com.flight.model;
import com.flight.model.Fare;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class FareTest 
{
    @Test 
    void gettersSetters() 
    {
        Fare f = new Fare();
        f.setTax(0.2);
        f.setCurrency("USD");
        f.setAmount(120.0);
        Double price = f.getAmount();

        f.setDiscount(0.1);
        f.setFinalAmount(price - f.getDiscount()*price+ f.getTax()*price);


        // Test getters et setters
        assertEquals(0.2, f.getTax());
        assertEquals("USD", f.getCurrency());
        assertEquals(120.0, f.getAmount());
        assertEquals(0.1, f.getDiscount());
        assertEquals(price - f.getDiscount()*price+ f.getTax()*price, f.getFinalAmount());
        assertEquals(132.0, f.getFinalAmount());
    }
}