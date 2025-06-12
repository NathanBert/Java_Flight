package com.flight.service;

import com.flight.dao.TravellerDAO;
import com.flight.model.Traveller;
import com.flight.dao.AirlineCompanyDAO;
import com.flight.model.AirlineCompany;
import com.flight.model.Airplane;
import com.flight.model.User;
import com.flight.model.AirlineCompany;
import com.flight.model.FlightTrip;
import com.flight.model.Seat;
import com.flight.model.Hop;
import com.flight.service.FlightSearchService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.sql.Connection;

public class CompanyService 
{
    private AirlineCompanyDAO airlineCompanyDAO;

    public CompanyService(Connection conn) 
    {
        airlineCompanyDAO = new AirlineCompanyDAO(conn);
    }

    public List<AirlineCompany> getAllCompanies() 
    {
        try {
            return airlineCompanyDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Airplane> getAirplanesByCompanyBetweenDates(AirlineCompany company, String startDate, String endDate) 
    {
        FlightSearchService flightSearchService = new FlightSearchService(airlineCompanyDAO.getConnection());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        try {
            List<Airplane> Airplanes = airlineCompanyDAO.findAirplanesByCompany(company);

            for (Airplane airplane : Airplanes) {
                List<Hop> hops = flightSearchService.getHopsByAirplaneId(airplane.getAirplaneId());
                for (Hop hop : hops) {
                    System.out.println("Hop Depart Time: " + hop.getDepartTime().toString()+"endDate: " + endDate+ (LocalDate.parse(hop.getDepartTime().toString(),formatter)).isAfter(LocalDate.parse(endDate, formatter)));
                    System.out.println("Hop Arrival Time: " + hop.getArrivalTime().toString()+"startDate: " + startDate+(LocalDate.parse(hop.getArrivalTime().toString(),formatter)).isBefore(LocalDate.parse(startDate, formatter)));
                    if (!(LocalDate.parse(hop.getDepartTime().toString(), formatter)).isAfter(LocalDate.parse(endDate, formatter)) &&
                        !(LocalDate.parse(hop.getArrivalTime().toString(), formatter)).isBefore(LocalDate.parse(startDate, formatter))) {
                        Airplanes.remove(airplane);
                        break; 
                    }
                }


            }
            return Airplanes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AirlineCompany getCompanyByName(String companyName) 
    {
        try {
            return airlineCompanyDAO.findByName(companyName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
