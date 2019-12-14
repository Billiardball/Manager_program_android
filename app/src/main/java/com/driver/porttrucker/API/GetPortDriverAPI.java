package com.driver.porttrucker.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.driver.porttrucker.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class GetPortDriverAPI extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_port_drivers.php";
    private Map<String,String> params;

    public GetPortDriverAPI(Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
