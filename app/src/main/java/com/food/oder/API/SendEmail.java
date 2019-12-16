package com.food.oder.API;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Common.ReqConst;

import java.util.HashMap;
import java.util.Map;

public class SendEmail extends StringRequest {

    private static  final String url =  ReqConst.SERVER_URL + "mobile_send_email.php";
    private Map<String,String> params;

    public SendEmail(String senderEmail, String senderPhone, String receiverEmail, String message,
                     Response.Listener<String> listener, Response.ErrorListener error) {


        super(Method.POST, url, listener,error);
        params = new HashMap<>();
        params.put("senderEmail", senderEmail);
        params.put("senderPhone", senderPhone);
        params.put("receiverEmail", receiverEmail);
        params.put("message", message);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
