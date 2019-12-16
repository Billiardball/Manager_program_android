package com.food.oder.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    public int id;
    public String firstName = "", lastName = "", email = "", profile = "", phone = "", driverType = "", state = "";

    public UserModel(int id, String firstName, String lastName, String email, String profile, String phone, String driverType, String state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profile = profile;
        this.phone = phone;
        this.driverType = driverType;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public String getPhone() {
        return phone;
    }

    public String getDriverType() {
        return driverType;
    }

    public String getState() {
        return state;
    }
}
