package com.flight.model;


public class User {
    private int userId;
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String userType;
    private String password;

    public User() {
        this.fName = "";
        this.lName = "";
        this.email = "";
        this.phone = "";
        this.userType = "User"; // Default user type
        this.password = "";
    }

    public User(String fName, String lName, String email, String phone, String userType, String password) {
    
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.password = password;
    }






    // Getters and setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getFName() {
        return fName;
    }
    public void setFName(String fName) {
        this.fName = fName;
    }
    public String getLName() {
        return lName;
    }
    public void setLName(String lName) {
        this.lName = lName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
