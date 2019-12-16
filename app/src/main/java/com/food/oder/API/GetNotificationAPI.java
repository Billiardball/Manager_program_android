package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class GetNotificationAPI extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_view_notification.php";
    private Map<String,String> params;

    public GetNotificationAPI(Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
