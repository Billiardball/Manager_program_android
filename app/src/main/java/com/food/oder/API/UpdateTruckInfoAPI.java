package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class UpdateTruckInfoAPI extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_update_truck_info.php";
    private Map<String,String> params;

    public UpdateTruckInfoAPI(String id, String make, String year, String color, String mileage,
            String engineType, String horses,String manyGears, String rearAxleCapacity, String transmisionMake,
            String wantSell, String price, String contactByPhone, String contactByEmail,
                              String photo1, String photo2, String photo3, String photo4,
                              Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("id", id);
        params.put("make", make);
        params.put("year", year);
        params.put("color", color);
        params.put("mileage", mileage);
        params.put("engineType", engineType);
        params.put("horses", horses);
        params.put("manyGears", manyGears);
        params.put("rearAxleCapacity", rearAxleCapacity);
        params.put("transmisionMake", transmisionMake);
        params.put("wantSell", wantSell);
        params.put("price", price);
        params.put("contactByPhone", contactByPhone);
        params.put("contactByEmail", contactByEmail);
        params.put("photo1", photo1);
        params.put("photo2", photo2);
        params.put("photo3", photo3);
        params.put("photo4", photo4);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
