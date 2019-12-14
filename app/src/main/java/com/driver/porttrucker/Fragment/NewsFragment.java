package com.driver.porttrucker.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Adapter.NewsAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.Model.NewsModel;
import com.driver.porttrucker.MyApp;
import com.driver.porttrucker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends BaseFragment {

    View fragment;

    NewsFragment instance;

    NewsAdapter adapter;

    ListView lstNews;

    public NewsFragment() { }

    public static NewsFragment newInstance(){
        NewsFragment newInstance = new NewsFragment();
        return  newInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_news, parent, false);

        lstNews = (ListView)fragment.findViewById(R.id.lstNews);

        getAllData();

        return fragment;
    }

    private void getAllData() {
        final String tag = "getAllCamera";
        String url = ReqConst.SERVER_URL + "mobile_port_news.php";
        ((MainActivity)getActivity()).showProgress();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {

                ((MainActivity)getActivity()).closeProgress();

                try {
                    JSONObject res = new JSONObject(json);
                    JSONArray jsonNewsModels = res.getJSONArray("allNews");

                    ArrayList<NewsModel> models = new ArrayList<>();
                    for (int i = 0; i < jsonNewsModels.length(); i++) {
                        JSONObject jsonNewsModel = jsonNewsModels.getJSONObject(i);

                        NewsModel model = new NewsModel(
                                jsonNewsModel.getInt("id"),
                                jsonNewsModel.getString("title"),
                                jsonNewsModel.getString("photo"),
                                jsonNewsModel.getString("body"),
                                jsonNewsModel.getString("created_at"));

                        models.add(model);
                    }

                    adapter = new NewsAdapter(getContext(), models);
                    lstNews.setAdapter(adapter);

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
