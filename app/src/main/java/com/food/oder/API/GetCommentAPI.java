package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class GetCommentAPI extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_get_comment.php";
    private Map<String,String> params;

    public GetCommentAPI(String portReportID, Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("portReportID", portReportID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
