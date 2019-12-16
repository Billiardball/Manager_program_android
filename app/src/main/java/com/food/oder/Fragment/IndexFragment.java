package com.food.oder.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.food.oder.API.GetIndexPageContentAPI;
import com.food.oder.API.PortReportsAPI;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Adapter.CameraAdapter;
import com.food.oder.Adapter.GalleryAdapter;
import com.food.oder.Adapter.IndexVPAdapter;
import com.food.oder.Adapter.NewsAdapter;
import com.food.oder.Adapter.PortReportAdapter;
import com.food.oder.Adapter.SaleAdapter;
import com.food.oder.Adapter.VideoAdapter;
import com.food.oder.Base.BaseFragment;
import com.food.oder.Common.Constant;
import com.food.oder.Common.ReqConst;
import com.food.oder.CustomView.NonScrollListView;
import com.food.oder.Model.CameraModel;
import com.food.oder.Model.GalleryModel;
import com.food.oder.Model.NewsModel;
import com.food.oder.Model.PortReportModel;
import com.food.oder.Model.SaleModel;
import com.food.oder.Model.VideoModel;
import com.food.oder.R;
import com.google.gson.JsonArray;
import com.rd.PageIndicatorView;
import com.santalu.autoviewpager.AutoViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndexFragment extends BaseFragment {

    Context context;

    View fragment;
    AutoViewPager viewPager;
    PageIndicatorView pageIndicatorView;
    NonScrollListView lstNews, lstCamera, lstGallery, lstVideos, lstSales;
    LinearLayout lytPortCamera, lytPortGallery, lytPortVideo, lytTruckForSale, lytStore;

    IndexVPAdapter mIndexVPAdapter;
    NewsAdapter mNewsAdapter;
    CameraAdapter mCameraAdapter;
    GalleryAdapter mGalleryAdapter;
    VideoAdapter mVideoAdapter;
    SaleAdapter mSaleAdapter;

    public IndexFragment(Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_index, container, false);

        lytPortCamera = (LinearLayout)fragment.findViewById(R.id.lytPortCamera);
        lytPortGallery = (LinearLayout)fragment.findViewById(R.id.lytPortGallery);
        lytPortVideo = (LinearLayout)fragment.findViewById(R.id.lytPortVideo);
        lytTruckForSale = (LinearLayout)fragment.findViewById(R.id.lytTruckForSale);
        lytStore = (LinearLayout)fragment.findViewById(R.id.lytStore);

        mIndexVPAdapter = new IndexVPAdapter(context);
        viewPager = (AutoViewPager)fragment.findViewById(R.id.viewPager);
        viewPager.setAdapter(mIndexVPAdapter);

        pageIndicatorView = (PageIndicatorView)fragment.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);

        lstNews = (NonScrollListView)fragment.findViewById(R.id.lstNews);

        lstGallery = (NonScrollListView)fragment.findViewById(R.id.lstGallery);

        lstCamera = (NonScrollListView)fragment.findViewById(R.id.lstCamera);

        lstVideos = (NonScrollListView)fragment.findViewById(R.id.lstVideos);

        lstSales = (NonScrollListView)fragment.findViewById(R.id.lstSales);

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
                        ArrayList<String> sliderImages = new ArrayList<>();
                        JSONArray jsonSliderImages = res.getJSONArray("sliderImgs");
                        for (int i = 0; i < jsonSliderImages.length(); i++){
                            sliderImages.add(jsonSliderImages.get(i).toString());
                        }
                        mIndexVPAdapter.setData(sliderImages);

                        ArrayList<NewsModel> newsModels = new ArrayList<>();
                        JSONArray jsonNewsModels = res.getJSONArray("news");
                        for (int i = 0; i < jsonNewsModels.length(); i++){
                            JSONObject jsonNewsModel = (JSONObject) jsonNewsModels.get(i);
                            NewsModel newsModel = new NewsModel(
                                    jsonNewsModel.getInt("id"),
                                    jsonNewsModel.getString("title"),
                                    jsonNewsModel.getString("photo"),
                                    jsonNewsModel.getString("body"),
                                    jsonNewsModel.getString("created_at")
                            );
                            newsModels.add(newsModel);
                        }
                        mNewsAdapter = new NewsAdapter(context, newsModels);
                        lstNews.setAdapter(mNewsAdapter);

                        ArrayList<CameraModel> cameraModels = new ArrayList<>();
                        JSONArray jsonCameraModels = res.getJSONArray("cameras");
                        for (int i = 0; i < jsonCameraModels.length(); i++){
                            JSONObject jsonCameraModel = (JSONObject)jsonCameraModels.get(i);
                            CameraModel cameraModel = new CameraModel(
                                    jsonCameraModel.getInt("id"),
                                    jsonCameraModel.getString("name"),
                                    jsonCameraModel.getString("url")
                            );
                            cameraModels.add(cameraModel);
                        }

                        mCameraAdapter = new CameraAdapter(context, cameraModels);
                        lstCamera.setAdapter(mCameraAdapter);

                        ArrayList<GalleryModel> galleryModels = new ArrayList<>();
                        JSONArray jsonGalleryModels = res.getJSONArray("gallerys");
                        for (int i = 0; i < jsonGalleryModels.length(); i ++){
                            JSONObject jsonGalleryModel = (JSONObject) jsonGalleryModels.get(i);
                            GalleryModel galleryModel = new GalleryModel(
                                    jsonGalleryModel.getInt("id"),
                                    jsonGalleryModel.getString("photo"),
                                    jsonGalleryModel.getString("foot")
                            );
                            galleryModels.add(galleryModel);
                        }

                        mGalleryAdapter = new GalleryAdapter(context, galleryModels);
                        lstGallery.setAdapter(mGalleryAdapter);

                        ArrayList<VideoModel> videoModels = new ArrayList<>();
                        JSONArray jsonVideoModels = res.getJSONArray("videos");
                        for (int i = 0; i < jsonVideoModels.length(); i++){
                            JSONObject jsonVideoModel = (JSONObject) jsonVideoModels.get(i);
                            VideoModel videoModel = new VideoModel(
                                    jsonVideoModel.getInt("id"),
                                    jsonVideoModel.getString("link"),
                                    jsonVideoModel.getString("title"),
                                    jsonVideoModel.getString("thumb")
                            );
                            videoModels.add(videoModel);
                        }
                        mVideoAdapter = new VideoAdapter(context, videoModels);
                        lstVideos.setAdapter(mVideoAdapter);

                        ArrayList<SaleModel> saleModels =  new ArrayList<>();
                        JSONArray jsonSaleModels = res.getJSONArray("trucks");
                        for (int i = 0; i < jsonSaleModels.length(); i ++){
                            JSONObject jsonSaleModel = (JSONObject)jsonSaleModels.get(i);
                            SaleModel saleModel = new SaleModel(jsonSaleModel);
                            saleModels.add(saleModel);
                        }
                        mSaleAdapter = new SaleAdapter(context, saleModels);
                        lstSales.setAdapter(mSaleAdapter);

                        lytPortCamera.setVisibility(View.VISIBLE);
                        lytPortGallery.setVisibility(View.VISIBLE);
                        lytPortVideo.setVisibility(View.VISIBLE);
                        lytTruckForSale.setVisibility(View.VISIBLE);
                        lytStore.setVisibility(View.VISIBLE);
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
        GetIndexPageContentAPI req = new GetIndexPageContentAPI(res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }
}
