package com.food.oder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.food.oder.R;

import java.util.ArrayList;

public class SaleDetailVPAdapter extends PagerAdapter {

    Context context;

    ArrayList<String> allData = new ArrayList<>();

    public SaleDetailVPAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<String> allData) {
        this.allData =  allData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View customView = LayoutInflater.from(context).inflate(R.layout.item_slider_index_page, container, false);
        ImageView imvPhoto = (ImageView)customView.findViewById(R.id.imvPhoto);
        Glide.with(context).load("https://porttrucker.app/images/trucks/"+allData.get(position)).into(imvPhoto);
        container.addView(customView);
        return customView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
