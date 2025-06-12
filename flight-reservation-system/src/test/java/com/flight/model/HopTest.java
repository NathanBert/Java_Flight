package com.flight.model;
import com.flight.model.Hop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class HopTest 
{
    @Test 
    void gettersSetters() 
    {
        Hop h = new Hop();
        h.setHopId(42);
        h.setAirplaneId(1);
        h.setArrive(2);
        h.setDepart(3);
        
        LocalDate DlocalDate = LocalDate.of(2025, 4, 27);

        Date Ddate = Date.from(DlocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp Dtimestamp = new Timestamp(Ddate.getTime());

        h.setDepartTime(Dtimestamp);



        LocalDate AlocalDate = LocalDate.of(2025, 4, 28);

        Date Adate = Date.from(AlocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp Atimestamp = new Timestamp(Adate.getTime());

        h.setArrivalTime(Atimestamp);


        // Test getters et setters  
        assertEquals(42, h.getHopId());
        assertEquals("2025-04-27 00:00:00.0", h.getDepartTime().toString());
        assertEquals("2025-04-28 00:00:00.0", h.getArrivalTime().toString());
        assertEquals(1, h.getAirplaneId());
        assertEquals(2, h.getArrive());
        assertEquals(3, h.getDepart());
    }
    
}
