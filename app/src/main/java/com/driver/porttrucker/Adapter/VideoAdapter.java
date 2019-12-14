package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.driver.porttrucker.Model.VideoModel;
import com.driver.porttrucker.R;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {

    ArrayList<VideoModel> allData =  new ArrayList<>();
    Context context;

    public VideoAdapter(Context context, ArrayList<VideoModel> tempData) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_videos, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i));

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        ImageView imvVideoThumb;
        TextView txvVideoTitle;

        private void setId(View item){
            imvVideoThumb = (ImageView)item.findViewById(R.id.imvVideoThumb);
            txvVideoTitle = (TextView)item.findViewById(R.id.txvVideoTitle);
        }

        private void loadData(VideoModel model){
            txvVideoTitle.setText(model.getTitle());
            Picasso.get().load(model.getThumb()).into(imvVideoThumb);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
