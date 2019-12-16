package com.food.oder.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.food.oder.API.PortReportsAPI;
import com.food.oder.API.UpdatePwdAPI;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Adapter.PortReportAdapter;
import com.food.oder.Base.BaseFragment;
import com.food.oder.Common.Common;
import com.food.oder.Common.Constant;
import com.food.oder.Common.Preference;
import com.food.oder.Common.ReqConst;
import com.food.oder.Model.PortReportModel;
import com.food.oder.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdatePasswordFragment extends BaseFragment {

    View fragment;
    EditText edtActualPwd, edtNewPwd, edtRepeatPwd;
    Button btnUpdatePwd;
    RoundedImageView imvUserPhoto;

    Context context;
    String actualPwd, newPwd, repeatPwd;

    public UpdatePasswordFragment(Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_update_password, container, false);
        edtActualPwd = (EditText)fragment.findViewById(R.id.edtActualPwd);
        edtNewPwd = (EditText)fragment.findViewById(R.id.edtNewPwd);
        edtRepeatPwd = (EditText)fragment.findViewById(R.id.edtRepeatPwd);
        btnUpdatePwd = (Button)fragment.findViewById(R.id.btnUpdatePwd); btnUpdatePwd.setOnClickListener(this);
        imvUserPhoto = (RoundedImageView)fragment.findViewById(R.id.imvUserPhoto);
        Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.profile).into(imvUserPhoto);

        return fragment;
    }

    private boolean isValid() {
        actualPwd = edtActualPwd.getText().toString().trim();
        newPwd = edtNewPwd.getText().toString().trim();
        repeatPwd = edtRepeatPwd.getText().toString().trim();

        if (!actualPwd.equals(Preference.getInstance().getValue(context, Constant.PWD, ""))){
            showToast("Actual password wrong");
            return false;
        }

        if (newPwd.length() == 0 || !newPwd.equals(repeatPwd)){
            showToast("Confirm new password");
            return false;
        }

        return true;
    }

    private void updatePassword() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                ((MainActivity)context).closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        showToast("Success");
                        Preference.getInstance().put(context, Constant.PWD, newPwd);
                    }else {
                        showToast(res.getString("message"));
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
        UpdatePwdAPI req = new UpdatePwdAPI(Common.userModel.email, newPwd, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdatePwd:
                if (isValid()){
                    updatePassword();
                }
                break;
        }
    }
}
