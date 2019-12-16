package com.food.oder.Model;

import java.io.Serializable;

public class GalleryModel implements Serializable {
    int id;
    String photo = "", foot = "";

    public GalleryModel(int id, String photo, String foot) {
        this.id = id;
        this.photo = photo;
        this.foot = foot;
    }

    public int getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getFoot() {
        return foot;
    }
}
