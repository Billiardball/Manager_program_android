package com.driver.porttrucker.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.driver.porttrucker.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserInfo extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_update_user_info.php";
    private Map<String,String> params;

    public UpdateUserInfo(String id, String firstName, String lastName, String phone, String driverType, String state,
                          Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("id", id);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("phone", phone);
        params.put("driverType", driverType);
        params.put("state", state);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
