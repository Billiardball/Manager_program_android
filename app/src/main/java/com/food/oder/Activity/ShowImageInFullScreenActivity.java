package com.food.oder.Activity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.food.oder.Adapter.ShowImageVPAdapter;
import com.food.oder.Base.BaseActivity;
import com.food.oder.Model.GalleryModel;
import com.food.oder.R;

import java.util.ArrayList;

public class ShowImageInFullScreenActivity extends BaseActivity {

    ViewPager vpGalleryPhoto;
    ShowImageVPAdapter adapter;

    TextView txvPhotoIndex;

    ArrayList<String> imageUrls = new ArrayList<>();

    ArrayList<GalleryModel> allData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gallery_photo);

        imageUrls = getIntent().getStringArrayListExtra("imageUrls");

        loadLayout();
    }

    private void loadLayout() {
        vpGalleryPhoto = (ViewPager)findViewById(R.id.vpGalleryPhoto);
        adapter = new ShowImageVPAdapter(this, imageUrls);
        vpGalleryPhoto.setAdapter(adapter);
        vpGalleryPhoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                txvPhotoIndex.setText(String.valueOf(position+ 1) + "/" + String.valueOf(imageUrls.size()));
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        txvPhotoIndex = (TextView)findViewById(R.id.txvPhotoIndex);
    }
}
