package com.food.oder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.food.oder.Activity.ShowCameraPhotoActivity;
import com.food.oder.Model.CameraDetailModel;
import com.food.oder.R;

import java.util.ArrayList;

public class CameraDetailaAdapter extends BaseAdapter {

    ArrayList<CameraDetailModel> allData =  new ArrayList<>();
    Context context;

    public CameraDetailaAdapter(Context context, ArrayList<CameraDetailModel> tempData) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_camera_detail, viewGroup, false);
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

        CameraDetailModel model;

        private void setId(View item){
            imvPhoto = (ImageView)item.findViewById(R.id.imvPhoto); imvPhoto.setOnClickListener(this);
            txvPhotoName = (TextView)item.findViewById(R.id.txvPhotoName);
        }

        private void loadData(CameraDetailModel model){
            this.model = model;

            Picasso.get().load(model.photoUrl).into(imvPhoto);
            txvPhotoName.setText(model.name);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imvPhoto:
                    Intent intent = new Intent(context, ShowCameraPhotoActivity.class);
                    intent.putExtra("cameraPhoto", model.photoUrl);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
