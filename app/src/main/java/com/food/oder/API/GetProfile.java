package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class GetProfile extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_my_profile.php";
    private Map<String,String> params;

    public GetProfile(String email,
                      Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
