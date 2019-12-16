package com.food.oder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;
import com.food.oder.R;

import java.util.ArrayList;

public class ShowImageVPAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> imageUrls = new ArrayList<>();

    public ShowImageVPAdapter(Context context, ArrayList<String> galleryUrls) {
        this.context = context;
        this.imageUrls = galleryUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View customView = LayoutInflater.from(context).inflate(R.layout.item_show_gallery_photo, container, false);

        TouchImageView imvGalleryPhoto = (TouchImageView)customView.findViewById(R.id.imvGalleryPhoto);
        Picasso.get().load(imageUrls.get(position)).into(imvGalleryPhoto);

        container.addView(customView);
        return customView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object );
    }
}
