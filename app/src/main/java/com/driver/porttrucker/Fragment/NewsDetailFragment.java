package com.driver.porttrucker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.driver.porttrucker.Adapter.RecentNewsAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.CustomView.NonScrollListView;
import com.driver.porttrucker.Model.NewsModel;
import com.driver.porttrucker.R;

import java.util.ArrayList;

public class NewsDetailFragment extends BaseFragment {

    View fragment;

    RecentNewsAdapter adapter;

    ImageView imvNewsPhoto;
    TextView txvNewsTitle, txvNewsBody;
    NonScrollListView lstRecentNews;

    public NewsDetailFragment() {}

    public static NewsDetailFragment newInstance(){

        NewsDetailFragment newInstance = new NewsDetailFragment();

        return  newInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_news_detail, parent, false);

        imvNewsPhoto =  (ImageView)fragment.findViewById(R.id.imvNewsPhoto);
        txvNewsTitle = (TextView)fragment.findViewById(R.id.txvNewsTitle);
        txvNewsBody =  (TextView)fragment.findViewById(R.id.txvNewsBody);

        lstRecentNews = (NonScrollListView)fragment.findViewById(R.id.lstRecentNews);

        loadData(Common.allNews, Common.currentNewsIndex);
        return fragment;
    }

    public void loadData(ArrayList<NewsModel> allData, int position){
        txvNewsTitle.setText(allData.get(position).getTitle());
        txvNewsBody.setText(allData.get(position).getBody());

        String date = (allData.get(position).getCreatedDate().split(" "))[0];
        String year = (date.split("-"))[0];
        String month = (date.split("-"))[1];
        Picasso.get().load("https://porttrucker.app/images/news" + "/" + year + "/" + month + "/" + allData.get(position).getPhoto()).into(imvNewsPhoto);

        ArrayList<NewsModel> recentNews = new ArrayList<>();
        for (int i = 0; i < allData.size(); i ++){
            if (i != position){
                recentNews.add(allData.get(i));
            }
        }

        adapter = new RecentNewsAdapter(getContext(), recentNews);
        lstRecentNews.setAdapter(adapter);
    }
}
