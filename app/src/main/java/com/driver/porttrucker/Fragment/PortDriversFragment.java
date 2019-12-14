package com.driver.porttrucker.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.driver.porttrucker.API.GetPortDriverAPI;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Adapter.PortDriverAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.Model.DriverModel;
import com.driver.porttrucker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PortDriversFragment extends BaseFragment {

    View fragment;

    PortDriversFragment instance;
    PortDriverAdapter adapter;

    RecyclerView rvPortDriver;

    public PortDriversFragment() { }

    public static PortDriversFragment newInstance(){
        PortDriversFragment newInstance = new PortDriversFragment();
        return  newInstance;
    }


    @SuppressLint("WrongConstant" )
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_port_drivers, parent, false);

        rvPortDriver = (RecyclerView)fragment.findViewById(R.id.rvPortDriver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPortDriver.setLayoutManager(linearLayoutManager);

        getAllData();

        return fragment;
    }

    private void getAllData() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                ((MainActivity)context).closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        JSONArray jsonPortDrivers = res.getJSONArray("portDrivers");
                        ArrayList<DriverModel> driverModels = new ArrayList<>();
                        for (int i = 0; i < jsonPortDrivers.length(); i++){
                            JSONObject jsonPortDriver = (JSONObject)jsonPortDrivers.get(i);
                            DriverModel model = new DriverModel(
                                    jsonPortDriver.getInt("id"),
                                    jsonPortDriver.getString("first_name") + " " + jsonPortDriver.getString("last_name"),
                                    jsonPortDriver.getString("photo")
                            );
                            driverModels.add(model);
                        }

                        adapter = new PortDriverAdapter(context, driverModels);
                        rvPortDriver.setAdapter(adapter);
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
        GetPortDriverAPI req = new GetPortDriverAPI(res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }
}
