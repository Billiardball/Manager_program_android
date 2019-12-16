package com.food.oder.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TruckerModel implements Serializable {
    public int userId;
    public String firstName, lastName, phone, email, photo, driverType,
            state, type, status, createdAt, make, year, color, mileage, engineType,
            horses, manyGears, rearAxleCapacity, transmisionMake, transmisionSpeed, wantShell, price,
            contactedByPhone, contactByEmail,
            truck1, truck2, truck3, truck4;
    public TruckerModel(JSONObject object){
        try {
            userId = object.getInt("id");
            firstName = object.getString("first_name");
            lastName = object.getString("last_name");
            phone = object.getString("phone");
            email = object.getString("email");
            photo = object.getString("photo");
            driverType = object.getString("driver_type");
            state = object.getString("state");
            type = object.getString("type");
            status = object.getString("status");
            createdAt = object.getString("created_at");
            make = object.getString("make");
            year = object.getString("year");
            color = object.getString("color");
            mileage = object.getString("mileage");
            engineType = object.getString("engine_type");
            horses = object.getString("horses");
            manyGears = object.getString("many_gears");
            rearAxleCapacity = object.getString("rear_axle_capacity");
            transmisionMake = object.getString("transmision_make");
            wantShell = object.getString("want_shell");
            price = object.getString("price");
            contactedByPhone = object.getString("contact_by_phone");
            contactByEmail = object.getString("contact_by_email");
            truck1 = object.getString("truck_1");
            truck2 = object.getString("truck_2");
            truck3 = object.getString("truck_3");
            truck4 = object.getString("truck_4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
