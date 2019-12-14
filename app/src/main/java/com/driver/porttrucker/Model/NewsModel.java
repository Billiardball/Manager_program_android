package com.driver.porttrucker.Model;

import java.io.Serializable;

public class NewsModel implements Serializable {
    int id;
    String title = "", photo = "", body = "", createdDate = "";

    public NewsModel(int id, String title, String photo, String body, String createdDate) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.body = body;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
