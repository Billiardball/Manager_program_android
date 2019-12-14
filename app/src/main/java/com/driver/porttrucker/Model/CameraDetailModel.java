package com.driver.porttrucker.Model;

import java.io.Serializable;

public class CameraDetailModel implements Serializable {
    public int id;
    public String name, photoUrl;

    public CameraDetailModel(int id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }
}
