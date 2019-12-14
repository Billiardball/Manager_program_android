package com.driver.porttrucker.Model;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationModel implements Serializable {

    public int id;
    public String title, message, createdAt;
    public static ArrayList<NotificationModel> allData = new ArrayList<>();

    public NotificationModel(int id, String title, String message, String createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public NotificationModel(JSONArray jsonNotifications){

        allData.clear();

        try {
            for ( int i = 0; i < jsonNotifications.length(); i++){
                JSONObject jsonNotification = (JSONObject) jsonNotifications.get(i);
                NotificationModel model = new NotificationModel(
                        jsonNotification.getInt("id"),
                        jsonNotification.getString("title"),
                        jsonNotification.getString("message"),
                        jsonNotification.getString("created_at")
                );

                allData.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
