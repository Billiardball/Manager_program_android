package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.porttrucker.Model.NewsModel;

import java.util.ArrayList;

public class IndexRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    ArrayList<NewsModel> newsModels = new ArrayList<>();

    public IndexRVAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder{

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder{

        public ViewHolder4(@NonNull View itemView) {
            super(itemView);
        }
    }

}
