package com.food.oder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Common.Common;
import com.food.oder.Fragment.NewsDetailFragment;
import com.food.oder.Model.NewsModel;
import com.food.oder.R;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    ArrayList<NewsModel> allData =  new ArrayList<>();
    Context context;

    public NewsAdapter(Context context, ArrayList<NewsModel> tempData) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i), i);

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        TextView txvNewsTitle, txvNewsBody;
        ImageView imvNewsPhoto;
        Button btnReadMore;

        NewsModel model;
        int position;

        private void setId(View item){
            txvNewsTitle = (TextView)item.findViewById(R.id.txvNewsTitle);
            txvNewsBody = (TextView)item.findViewById(R.id.txvNewsBody);
            imvNewsPhoto = (ImageView)item.findViewById(R.id.imvNewsPhoto); imvNewsPhoto.setOnClickListener(this);
            btnReadMore = (Button)item.findViewById(R.id.btnReadMore); btnReadMore.setOnClickListener(this);

        }

        private void loadData(NewsModel model, int position){
            this.model = model;
            this.position =  position;

            txvNewsTitle.setText(model.getTitle());
            txvNewsBody.setText(model.getBody());

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
                case R.id.btnReadMore:
                    Common.currentNewsIndex = position;
                    ((MainActivity)context).openFragment(NewsDetailFragment.newInstance());
                    break;
            }
        }
    }
}
