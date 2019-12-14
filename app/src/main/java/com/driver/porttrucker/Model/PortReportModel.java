package com.driver.porttrucker.Model;

import java.io.Serializable;

public class PortReportModel implements Serializable {

    public int id;
    public String title, body, photo1, photo2, photo3, photo4,
            views, comments, status, created_by, created_at, last_updated_by,  last_updated_at;

    public PortReportModel(int id, String title, String body, String photo1, String photo2, String photo3, String photo4, String views, String comments, String status, String created_by, String created_at, String last_updated_by, String last_updated_at) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.photo4 = photo4;
        this.views = views;
        this.comments = comments;
        this.status = status;
        this.created_by = created_by;
        this.created_at = created_at;
        this.last_updated_by = last_updated_by;
        this.last_updated_at = last_updated_at;
    }
}
