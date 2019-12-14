package com.driver.porttrucker.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.driver.porttrucker.Activity.ShowImageInFullScreenActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.driver.porttrucker.API.GetCommentAPI;
import com.driver.porttrucker.Adapter.PortReportAdapter;
import com.driver.porttrucker.Adapter.UserCommentAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.Model.PortReportModel;
import com.driver.porttrucker.Model.UserCommentModel;
import com.driver.porttrucker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PortReportDetailFragment extends BaseFragment {

    public static ArrayList<PortReportModel> mPortReportModel = new ArrayList<>();
    public static int portReportID;

    View fragment;

    ImageView imvPhoto1, imvPhoto2, imvPhoto3, imvPhoto4;
    RoundedImageView imvUserPhoto;
    RecyclerView rvCommentList, rvPortReport;
    TextView txvUserName, txvCntOfViews, txvCntOfComments;
    LinearLayout lytContainer1, lytContainer2;

    PortReportModel model;

    ArrayList<PortReportModel> allData =  new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();

    UserCommentAdapter mUserCommentAdapter;
    PortReportAdapter  mPortReportAdapter;


    public PortReportDetailFragment(int portReportID, ArrayList<PortReportModel> allData) {
        this.portReportID = portReportID;
        this.allData = allData;
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment = LayoutInflater.from(getContext()).inflate(R.layout.frm_port_report_detail, container, false);

        txvCntOfViews = (TextView)fragment.findViewById(R.id.txvCntOfViews);
        txvCntOfComments = (TextView)fragment.findViewById(R.id.txvCntOfComments);

        lytContainer1 = (LinearLayout)fragment.findViewById(R.id.lytContainer1); lytContainer1.setOnClickListener(this);
        lytContainer2 = (LinearLayout)fragment.findViewById(R.id.lytContainer2); lytContainer2.setOnClickListener(this);

        imvPhoto1 = (ImageView)fragment.findViewById(R.id.imvPhoto1);
        imvPhoto2 = (ImageView)fragment.findViewById(R.id.imvPhoto2);
        imvPhoto3 = (ImageView)fragment.findViewById(R.id.imvPhoto3);
        imvPhoto4 = (ImageView)fragment.findViewById(R.id.imvPhoto4);

        rvCommentList = (RecyclerView)fragment.findViewById(R.id.rvCommentList);
        rvCommentList.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mUserCommentAdapter = new UserCommentAdapter(getContext());
        rvCommentList.setAdapter(mUserCommentAdapter);
        rvCommentList.setNestedScrollingEnabled(false);

        imvUserPhoto = (RoundedImageView)fragment.findViewById(R.id.imvUserPhoto);
        txvUserName = (TextView)fragment.findViewById(R.id.txvUserName);

        rvPortReport = (RecyclerView)fragment.findViewById(R.id.rvPortReport);
        rvPortReport.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadData ();

        getComment();

        return fragment;
    }

    public void loadData() {
        Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.profile).into(imvUserPhoto);
        txvUserName.setText(Common.userModel.firstName + " " + Common.userModel.lastName);

        ArrayList<PortReportModel> otherPortReports = new ArrayList<>();

        for (PortReportModel model :  allData){
            if (portReportID == model.id){
                String imgRootRul = "https://porttrucker.app/images/news/" + model.created_at.split("-")[0] + "/" + model.created_at.split("-")[1] + "/";

                Picasso.get().load(imgRootRul + model.photo1).into(imvPhoto1); imageUrls.add(imgRootRul + model.photo1);
                Picasso.get().load(imgRootRul + model.photo2).into(imvPhoto2); imageUrls.add(imgRootRul + model.photo2);
                Picasso.get().load(imgRootRul + model.photo3).into(imvPhoto3); imageUrls.add(imgRootRul + model.photo3);
                Picasso.get().load(imgRootRul + model.photo4).into(imvPhoto4); imageUrls.add(imgRootRul + model.photo4);

                txvCntOfViews.setText(model.views);
                txvCntOfComments.setText(model.comments);
            }else {
                otherPortReports.add(model);
            }
        }

        mPortReportAdapter = new PortReportAdapter(getContext(), otherPortReports);
        rvPortReport.setAdapter(mPortReportAdapter);
    }

    private void getComment() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        JSONArray jsonAllComment = res.getJSONArray("allComments");
                        ArrayList<UserCommentModel> allUserCommentModels = new ArrayList<>();
                        for (int i = 0; i < jsonAllComment.length(); i ++){
                            JSONObject jsonUserCommentModel = (JSONObject) jsonAllComment.get(i);
                            JSONObject jsonUserInfo = jsonUserCommentModel.getJSONObject("userInfo");

                            UserCommentModel model = new UserCommentModel(
                                    jsonUserCommentModel.getInt("id"),
                                    jsonUserInfo.getString("first_name") + " " + jsonUserInfo.getString("last_name"),
                                    jsonUserCommentModel.getString("comment"),
                                    jsonUserInfo.getString("photo")
                            );

                            allUserCommentModels.add(model);
                        }

                        mUserCommentAdapter.loadData(allUserCommentModels);

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
        GetCommentAPI req = new GetCommentAPI(String.valueOf(portReportID), res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(req);
    }

    private void showImageInFullScreen(){
        Intent intent = new Intent(context, ShowImageInFullScreenActivity.class);
        intent.putExtra("imageUrls", imageUrls);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lytContainer1:
                showImageInFullScreen();
                break;
            case R.id.lytContainer2:
                showImageInFullScreen();
                break;
        }
    }
}
