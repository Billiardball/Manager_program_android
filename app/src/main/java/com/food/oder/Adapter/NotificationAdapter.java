package com.food.oder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.oder.Common.Constant;
import com.food.oder.Model.NotificationModel;
import com.food.oder.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;

    ArrayList<NotificationModel> allData = new ArrayList<>();

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<NotificationModel> allData){
        this.allData =  allData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_notification, null);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = allData.get(position);
        holder.txvTitle.setText( model.title);
        holder.txvBody.setText(model.message);
        holder.txvDate.setText(model.createdAt);
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txvTitle, txvBody, txvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txvTitle = (TextView)itemView.findViewById(R.id.txvTitle);
            txvBody = (TextView)itemView.findViewById(R.id.txvBody);
            txvDate = (TextView)itemView.findViewById(R.id.txvDate);
        }
    }
}
