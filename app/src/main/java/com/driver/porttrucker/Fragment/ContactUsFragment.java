package com.driver.porttrucker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.driver.porttrucker.API.ContactUsAPI;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsFragment extends BaseFragment {

    View fragment;

    ContactUsFragment instance;

    TextView txvUserName, txvEmail;
    EditText edtPhone;
    Spinner spDepartment;
    EditText edtMessage;
    Button btnSend;

    String name, phone, department, message;

    public ContactUsFragment() { }

    public static ContactUsFragment newInstance(){
        ContactUsFragment newInstance = new ContactUsFragment();
        return  newInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_contact_us, parent, false);

        txvUserName = (TextView)fragment.findViewById(R.id.txvUserName);
        txvUserName.setText(Common.userModel.getFirstName() + " " + Common.userModel.getLastName());

        txvEmail = (TextView)fragment.findViewById(R.id.txvEmail);
        txvEmail.setText(Common.userModel.getEmail());

        edtPhone = (EditText)fragment.findViewById(R.id.edtPhone);
        edtPhone.setText(Common.userModel.getPhone());

        spDepartment = (Spinner)fragment.findViewById(R.id.spDepartment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_city, getResources().getStringArray(R.array.departments));
        spDepartment.setAdapter(adapter);
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spDepartment.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spDepartment.setSelection(0);

        edtMessage = (EditText)fragment.findViewById(R.id.edtMessage);

        btnSend = (Button)fragment.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        return fragment;
    }

    private void sendEmail() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                ((MainActivity)context).closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        showToast("Success");
                    }else {
                        showToast("Failed");
                    }
                }catch (JSONException e){
                    ((MainActivity)context).showAlertDialog(e.getMessage());
                }
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((MainActivity)context).closeProgress();
                ((MainActivity)context).showAlertDialog(((MainActivity)context).getString(R.string.serverFailed));
            }
        };

        ((MainActivity)context).showProgress();
        ContactUsAPI req = new ContactUsAPI(name, Common.userModel.email, phone, department, message, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }

    private boolean isValid() {
        name = txvUserName.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        if (phone.equals("null")){
            showAlertDialog("Input your phone number");
            return false;
        }

        message = edtMessage.getText().toString().trim();
        if (message.length() == 0) {
            showAlertDialog("Input your message");
            return false;
        }

        department = spDepartment.getSelectedItem().toString().trim();
        if (department.equals("Select")){
            showAlertDialog("Select Department");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                if (isValid()){
                    sendEmail();
                }
                break;
        }
    }
}
