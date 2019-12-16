package com.food.oder.Fragment;

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
import com.food.oder.API.PortReportsAPI;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Adapter.PortReportAdapter;
import com.food.oder.Base.BaseFragment;
import com.food.oder.Common.Constant;
import com.food.oder.Common.ReqConst;
import com.food.oder.Model.PortReportModel;
import com.food.oder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PortReportsFragment extends BaseFragment {

    View fragment;

    RecyclerView rcvPortReports;

    PortReportsFragment instance;

    PortReportAdapter adapter;

    public PortReportsFragment() { }

    public static PortReportsFragment newInstance(){
        PortReportsFragment newInstance = new PortReportsFragment();
        return  newInstance;
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_port_reports, parent, false);

        rcvPortReports = (RecyclerView)fragment.findViewById(R.id.rcvPortReports);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvPortReports.setLayoutManager(linearLayoutManager);

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
                        JSONArray jsonPortReports = res.getJSONArray("allReports");
                        ArrayList<PortReportModel> portReportModels = new ArrayList<>();
                        for (int i = 0; i < jsonPortReports.length(); i++){
                            JSONObject jsonPortReport = (JSONObject)jsonPortReports.get(i);
                            PortReportModel model = new PortReportModel(
                                    jsonPortReport.getInt("id"),
                                    jsonPortReport.getString("title"),
                                    jsonPortReport.getString("body"),
                                    jsonPortReport.getString("photo1"),
                                    jsonPortReport.getString("photo2"),
                                    jsonPortReport.getString("photo3"),
                                    jsonPortReport.getString("photo4"),
                                    jsonPortReport.getString("views"),
                                    jsonPortReport.getString("comments"),
                                    jsonPortReport.getString("status"),
                                    jsonPortReport.getString("created_by"),
                                    jsonPortReport.getString("created_at"),
                                    jsonPortReport.getString("last_updated_by"),
                                    jsonPortReport.getString("last_updated_at")
                            );

                            portReportModels.add(model);
                        }

                        adapter = new PortReportAdapter(context, portReportModels);
                        rcvPortReports.setAdapter(adapter);
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
        PortReportsAPI req = new PortReportsAPI(res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }
}
