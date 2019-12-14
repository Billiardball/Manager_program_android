package com.driver.porttrucker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.driver.porttrucker.API.GetCameraDetail;
import com.driver.porttrucker.Adapter.CameraDetailaAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.Model.CameraDetailModel;
import com.driver.porttrucker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraDetailFragment extends BaseFragment {

    CameraDetailaAdapter adapter;

    View fragment;

    ListView lstCameraDetail;
    TextView txvNoData;
    ImageView imvPhoto;

    String portId;

    public CameraDetailFragment(String portId) {
        this.portId = portId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_camera_detail_fragment, container, false);
        lstCameraDetail = (ListView)fragment.findViewById(R.id.lstCameraDetail);

        txvNoData = (TextView)fragment.findViewById(R.id.txvNoData);

        imvPhoto = (ImageView)fragment.findViewById(R.id.imvPhoto);

        getAllData();
        return fragment;
    }

    private void getAllData() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
//                    JSONArray res = new JSONArray(json);
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){

                        JSONArray jsonCameras = res.getJSONArray("cameras");
                        ArrayList cameras = new ArrayList();

                        for (int i = 0; i < jsonCameras.length(); i++){
                            JSONObject jsonCamera = jsonCameras.getJSONObject(i);

                            CameraDetailModel camera = new CameraDetailModel(
                                    jsonCamera.getInt("id"),
                                    jsonCamera.getString("name"),
                                    jsonCamera.getString("url")
                            );

                            cameras.add(camera);
                        }

                        if (cameras.size() == 0){
                            txvNoData.setVisibility(View.VISIBLE);
                            lstCameraDetail.setVisibility(View.GONE);
                        }else {
                            txvNoData.setVisibility(View.GONE);
                            lstCameraDetail.setVisibility(View.VISIBLE);
                            adapter = new CameraDetailaAdapter(context, cameras);
                            lstCameraDetail.setAdapter(adapter);
                        }

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
        GetCameraDetail req = new GetCameraDetail(portId, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }
}
