package com.food.oder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.food.oder.API.UserRegister;
import com.food.oder.Base.BaseActivity;
import com.food.oder.Common.Common;
import com.food.oder.Common.ReqConst;
import com.food.oder.Model.UserModel;
import com.food.oder.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends BaseActivity {

    EditText edtFirstName, edtLastName, edtEmail, edtPassword, edtConfirmPassword;
    CheckBox cbAgreedTermsCondition;

    String firstName = "", lastName = "", email = "", password = "", confirmPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addActivity(this);

        loadLayout();
    }

    private void loadLayout() {
        edtFirstName = (EditText)findViewById(R.id.edtFirstName);
        edtLastName = (EditText)findViewById(R.id.edtLastName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText)findViewById(R.id.edtConfirmPassword);

        cbAgreedTermsCondition = (CheckBox)findViewById(R.id.cbAgreedTermsCondition);
    }

    private boolean isValid() {
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (firstName.length() == 0) { showToast("Input first name"); return  false;}
        if (lastName.length() == 0) { showToast("Input last name"); return  false;}
        if (email.length() == 0 && !email.contains("@")) {showToast("Input email address"); return  false;}
        if (password.length() == 0){ showToast("Input Password"); return false;}
        if (!password.equals(confirmPassword)) { showToast("Confirm password"); return  false;}

        if (!cbAgreedTermsCondition.isChecked()){ showToast("You must agreed with Terms and Conditions"); return false;}

        return true;
    }

    public void signup(View view){
        if (isValid()){
            final Response.Listener<String> res = new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    closeProgress();
                    try{
                        JSONObject res = new JSONObject(json);
                        if (res.getBoolean("status")){
                            Common.userModel = new UserModel(
                                    res.getInt("id"),
                                    res.getString("firstName"),
                                    res.getString("lastName"),
                                    res.getString("email"),
                                    res.getString("profile"),
                                    res.getString("phone"),
                                    res.getString("driverType"),
                                    res.getString("state"));

                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        }else {
                            showToast(res.getString("message"));
                        }
                    }catch (JSONException e){
                        showAlertDialog(e.getMessage());
                    }
                }
            };
            final Response.ErrorListener error = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    closeProgress();
                    showAlertDialog(getString(R.string.serverFailed));
                }
            };

            showProgress();
            UserRegister req = new UserRegister(firstName, lastName, email, password, res, error);
            req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue request = Volley.newRequestQueue(this);
            request.add(req);
        }
    }
}
