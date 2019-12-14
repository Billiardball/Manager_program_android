package com.driver.porttrucker.Model;

import java.io.Serializable;

public class CameraModel implements Serializable {
    int id;
    String name = "", photo = "";

    public CameraModel(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }
}
