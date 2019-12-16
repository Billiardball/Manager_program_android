package com.food.oder.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SaleModel implements Serializable {
    int id;
    String first_name, last_name, phone, email, photo, make, engine_type, horses, mileage, many_gears,
            color, rear_axle_capacity, price, year, truck_1, truck_2, truck_3, truck_4;

    public SaleModel (JSONObject jsonObject){
        try {
            id = jsonObject.getInt("id");
            first_name = jsonObject.getString("first_name");
            last_name = jsonObject.getString("last_name");
            phone = jsonObject.getString("phone");
            email = jsonObject.getString("email");
            photo = jsonObject.getString("photo");
            make = jsonObject.getString("make");
            engine_type = jsonObject.getString("engine_type");
            horses = jsonObject.getString("horses");
            mileage = jsonObject.getString("mileage");
            many_gears = jsonObject.getString("many_gears");
            color = jsonObject.getString("color");
            rear_axle_capacity = jsonObject.getString("rear_axle_capacity");
            price = jsonObject.getString("price");
            year = jsonObject.getString("year");
            truck_1 = jsonObject.getString("truck_1");
            truck_2 = jsonObject.getString("truck_2");
            truck_3 = jsonObject.getString("truck_3");
            truck_4 = jsonObject.getString("truck_4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getMake() {
        return make;
    }

    public String getEngine_type() {
        return engine_type;
    }

    public String getHorses() {
        return horses;
    }

    public String getMileage() {
        return mileage;
    }

    public String getMany_gears() {
        return many_gears;
    }

    public String getColor() {
        return color;
    }

    public String getRear_axle_capacity() {
        return rear_axle_capacity;
    }

    public String getPrice() {
        return price;
    }

    public String getYear() {
        return year;
    }

    public String getTruck_1() {
        return truck_1;
    }

    public String getTruck_2() {
        return truck_2;
    }

    public String getTruck_3() {
        return truck_3;
    }

    public String getTruck_4() {
        return truck_4;
    }
}
