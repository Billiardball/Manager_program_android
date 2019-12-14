package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Fragment.NewsDetailFragment;
import com.driver.porttrucker.Model.NewsModel;
import com.driver.porttrucker.R;

import java.util.ArrayList;

public class RecentNewsAdapter extends BaseAdapter {

    ArrayList<NewsModel> allData =  new ArrayList<>();
    Context context;

    public RecentNewsAdapter(Context context, ArrayList<NewsModel> tempData) {
        this.context = context;
        this.allData = tempData;
        Common.allNews = allData;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_recent_news, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i), i);

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        TextView txvNewsTitle;
        ImageView imvNewsPhoto;

        NewsModel model;
        int position;

        private void setId(View item){
            txvNewsTitle = (TextView)item.findViewById(R.id.txvNewsTitle);
            imvNewsPhoto = (ImageView)item.findViewById(R.id.imvNewsPhoto); imvNewsPhoto.setOnClickListener(this);
        }

        private void loadData(NewsModel model, int position){
            this.model = model;
            this.position =  position;

            txvNewsTitle.setText(model.getTitle());

            String date = (model.getCreatedDate().split(" "))[0];
            String year = (date.split("-"))[0];
            String month = (date.split("-"))[1];
            Picasso.get().load("https://porttrucker.app/images/news" + "/" + year + "/" + month + "/" + model.getPhoto()).into(imvNewsPhoto);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imvNewsPhoto:
                    Common.currentNewsIndex = position;
                    ((MainActivity)context).openFragment(NewsDetailFragment.newInstance());
                    break;
            }
        }
    }
}
