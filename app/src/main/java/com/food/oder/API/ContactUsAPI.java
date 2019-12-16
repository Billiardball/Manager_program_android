package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class ContactUsAPI extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_contact_us.php";
    private Map<String,String> params;

    public ContactUsAPI(String name, String email, String phone, String department, String message,
                        Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("department", department);
        params.put("message", message);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
