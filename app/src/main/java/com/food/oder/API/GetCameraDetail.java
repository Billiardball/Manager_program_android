package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class GetCameraDetail extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_view_camera.php";
    private Map<String,String> params;

    public GetCameraDetail(String portId,
                           Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("portId", portId);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
