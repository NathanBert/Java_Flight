package com.flight.model;

public class AirlineCompany {
    private int companyId;
    private String name;

    public AirlineCompany(int companyId, String name) {
        this.companyId = companyId;
        this.name = name;
    }

    public AirlineCompany() {
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
}