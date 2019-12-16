package com.food.oder.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Adapter.SaleAdapter;
import com.food.oder.Base.BaseFragment;
import com.food.oder.Common.ReqConst;
import com.food.oder.Model.SaleModel;
import com.food.oder.MyApp;
import com.food.oder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaleFragment extends BaseFragment {

    View fragment;

    SaleFragment instance;

    SaleAdapter adapter;

    ListView lstSales;

    public SaleFragment() { }

    public static SaleFragment newInstance(){
        SaleFragment newInstance = new SaleFragment();
        return  newInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_sale, parent, false);

        lstSales = (ListView)fragment.findViewById(R.id.lstSales);

        getAllData();

        return fragment;
    }

    private void getAllData() {
        final String tag = "getAllCamera";
        String url = ReqConst.SERVER_URL + "mobile_truck_for_sale.php";
        ((MainActivity)getActivity()).showProgress();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {

                ((MainActivity)getActivity()).closeProgress();

                try {
                    JSONObject res = new JSONObject(json);
                    JSONArray jsonSaleModels = res.getJSONArray("allSales");

                    ArrayList<SaleModel> models = new ArrayList<>();
                    for (int i = 0; i < jsonSaleModels.length(); i++) {
                        JSONObject jsonSaleModel = jsonSaleModels.getJSONObject(i);

                        SaleModel model = new SaleModel(jsonSaleModel);

                        models.add(model);
                    }

                    adapter = new SaleAdapter(getContext(), models);
                    lstSales.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((MainActivity)getActivity()).closeProgress();
                if (error.getMessage() != null)
                    Log.e(tag, error.getMessage());
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApp.getInstance().addToRequestQueue(request, tag);
    }
}
