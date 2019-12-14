package com.driver.porttrucker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.driver.porttrucker.R;

public class ShowCameraPhotoActivity extends AppCompatActivity {

    ImageView imvPhoto;

    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_camera_photo);

        imageUrl = getIntent().getStringExtra("cameraPhoto");
        loadLayout();
    }

    private void loadLayout() {
        imvPhoto = (ImageView)findViewById(R.id.imvPhoto);
        Picasso.get().load(imageUrl).into(imvPhoto);
    }
}
