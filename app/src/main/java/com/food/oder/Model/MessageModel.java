package com.food.oder.Model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    public int userId;
    public String photoUrl, userName, message, date;

    public MessageModel(){}

    public MessageModel(int userId, String photoUrl, String userName, String message, String date) {
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.userName = userName;
        this.message = message;
        this.date = date;
    }
}
