package com.driver.porttrucker.Model;

import java.io.Serializable;

public class DriverModel implements Serializable {
    public int id;
    public String driverName, driverPhoto;

    public DriverModel(int id, String driverName, String driverPhoto) {
        this.id = id;
        this.driverName = driverName;
        this.driverPhoto = driverPhoto;
    }
}
