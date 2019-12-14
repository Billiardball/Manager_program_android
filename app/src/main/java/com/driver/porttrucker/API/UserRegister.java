package com.driver.porttrucker.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.driver.porttrucker.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_signup.php";
    private Map<String,String> params;

    public UserRegister(String firstName, String lastName, String email, String password,
                        Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
