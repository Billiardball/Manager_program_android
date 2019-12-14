package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.driver.porttrucker.Activity.ShowImageInFullScreenActivity;
import com.driver.porttrucker.Model.GalleryModel;
import com.driver.porttrucker.R;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    final String PHOTO_BASE_URL = "https://porttrucker.app/images/gallery/";

    ArrayList<GalleryModel> allData =  new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();
    Context context;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> tempData) {
        this.context = context;
        this.allData = tempData;
        notifyDataSetChanged();

        for (int i = 0; i < tempData.size(); i++){
            imageUrls.add(PHOTO_BASE_URL + tempData.get(i).getPhoto());
        }
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public Object getItem(int i) {
        return allData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomHolder holder;

        if (view == null){
            holder = new CustomHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_gallery, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i));

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        ImageView imvPhoto;
        TextView txvPhotoName;

        private void setId(View item){
            imvPhoto = (ImageView)item.findViewById(R.id.imvPhoto); imvPhoto.setOnClickListener(this);
            txvPhotoName = (TextView)item.findViewById(R.id.txvPhotoName);
        }

        private void loadData(GalleryModel model){
            Picasso.get().load(PHOTO_BASE_URL + model.getPhoto()).into(imvPhoto);
            txvPhotoName.setText(model.getFoot());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imvPhoto:
                    Intent intent = new Intent(context, ShowImageInFullScreenActivity.class);
                    intent.putExtra("imageUrls", imageUrls);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
