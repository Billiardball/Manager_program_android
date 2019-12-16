package com.food.oder.Model;

import java.io.Serializable;

public class UserCommentModel implements Serializable {

    public int id;
    public String userName, comment, userPhoto;

    public UserCommentModel(int id, String userName, String comment, String userPhoto) {
        this.id = id;
        this.userName = userName;
        this.comment = comment;
        this.userPhoto = userPhoto;
    }
}
