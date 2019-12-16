package com.food.oder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Model.CameraModel;
import com.food.oder.R;

import java.util.ArrayList;

public class CameraAdapter extends BaseAdapter {

    ArrayList<CameraModel> allData =  new ArrayList<>();
    Context context;

    public CameraAdapter(Context context, ArrayList<CameraModel> tempData) {
        this.context = context;
        this.allData = tempData;
        notifyDataSetChanged();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_camera, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i), i);

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        ImageView imvPhoto;
        TextView txvPhotoName;

        CameraModel model;
        int index;
        private void setId(View item){
            imvPhoto = (ImageView)item.findViewById(R.id.imvPhoto); imvPhoto.setOnClickListener(this);
            txvPhotoName = (TextView)item.findViewById(R.id.txvPhotoName);
        }

        private void loadData(CameraModel model, int i){

            this.model = model;
            this.index = i;

            if (model.getPhoto().contains("http")){
                Picasso.get().load(model.getPhoto()).into(imvPhoto);
            }else {
                final String PHOTO_BASE_URL = "https://porttrucker.app/images/ports/";
                Picasso.get().load(PHOTO_BASE_URL + model.getPhoto()).into(imvPhoto);
            }

            txvPhotoName.setText(model.getName());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imvPhoto:
//                    Common.portId = model.getId();
                    ((MainActivity)context).showCameraDetailFragment(model.getId());
                    break;
            }
        }
    }
}
