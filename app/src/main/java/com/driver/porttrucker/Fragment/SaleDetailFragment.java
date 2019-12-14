package com.driver.porttrucker.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.driver.porttrucker.Adapter.SaleDetailVPAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Model.SaleModel;
import com.driver.porttrucker.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rd.PageIndicatorView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SaleDetailFragment extends BaseFragment {

    Context context;

    View fragment;
    ViewPager vpTruckPhotos;
    PageIndicatorView pageIndicatorView;
    RoundedImageView imvUserPhoto;
    TextView txvMaker, txvEngine, txvHorsePower, txvMileage, txvTransSpeed, txvRearCapacity, txvPrice;
    View viewColor;
    Button btnCall, btnEmail;

    SaleModel saleModel;
    SaleDetailVPAdapter adapter;

    ArrayList<String> truckPhotos = new ArrayList<>();

    public SaleDetailFragment(Context context, SaleModel saleModel) {
        this.context = context;
        this.saleModel = saleModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_sale_detail, container, false);

        btnCall = (Button)fragment.findViewById(R.id.btnCall);
        btnEmail = (Button)fragment.findViewById(R.id.btnEmail);
        if (saleModel.getPhone().length() == 0){
            btnCall.setVisibility(View.GONE);
        }

        viewColor = (View)fragment.findViewById(R.id.viewColor);
        viewColor.setBackgroundColor(Color.parseColor(saleModel.getColor()));

        txvPrice = (TextView)fragment.findViewById(R.id.txvPrice);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String price = formatter.format(Integer.valueOf(saleModel.getPrice()));
        txvPrice.setText(price);
        txvRearCapacity = (TextView)fragment.findViewById(R.id.txvRearCapacity);
        txvRearCapacity.setText("Rear Axle Capacity: " + saleModel.getRear_axle_capacity());
        txvTransSpeed = (TextView)fragment.findViewById(R.id.txvTransSpeed);
        txvTransSpeed.setText("Trans Speed: " + saleModel.getMany_gears());
        txvMileage = (TextView)fragment.findViewById(R.id.txvMileage);
        txvMileage.setText("Mileage: " + saleModel.getMileage());
        txvHorsePower = (TextView)fragment.findViewById(R.id.txvHorsePower);
        txvHorsePower.setText("Horse Power: " + saleModel.getHorses());

        txvEngine = (TextView)fragment.findViewById(R.id.txvEngine);
        txvEngine.setText("Engine: " + saleModel.getEngine_type());

        txvMaker = (TextView)fragment.findViewById(R.id.txvMaker);
        txvMaker.setText(saleModel.getYear() + " " + saleModel.getMake());

        imvUserPhoto = (RoundedImageView)fragment.findViewById(R.id.imvUserPhoto);
        Glide.with(context).load(Constant.USER_PHOTO_BASE_URL + saleModel.getPhoto()).into(imvUserPhoto);

        adapter = new SaleDetailVPAdapter(context);
        vpTruckPhotos = (ViewPager)fragment.findViewById(R.id.vpTruckPhotos);
        vpTruckPhotos.setAdapter(adapter);
        if (saleModel.getTruck_1().length() > 0){
            truckPhotos.add(saleModel.getTruck_1());
        }
        if (saleModel.getTruck_2().length() > 0){
            truckPhotos.add(saleModel.getTruck_2());
        }
        if (saleModel.getTruck_3().length() > 0){
            truckPhotos.add(saleModel.getTruck_3());
        }
        if (saleModel.getTruck_4().length() > 0){
            truckPhotos.add(saleModel.getTruck_4());
        }
        adapter.setData(truckPhotos);

        pageIndicatorView = (PageIndicatorView)fragment.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(vpTruckPhotos);

        return fragment;
    }
}
