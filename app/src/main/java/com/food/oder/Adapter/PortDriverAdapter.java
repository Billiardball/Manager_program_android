package com.food.oder.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.food.oder.Common.Constant;
import com.food.oder.Model.DriverModel;
import com.food.oder.R;

import java.util.ArrayList;

public class PortDriverAdapter extends RecyclerView.Adapter<PortDriverAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<DriverModel> allData = new ArrayList<DriverModel>();

    public PortDriverAdapter(Context context, ArrayList<DriverModel> allData) {
        this.context = context;
        this.allData = allData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_port_driver, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        DriverModel driverModel = allData.get(position);

        if (!driverModel.driverPhoto.equals("null")){
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + driverModel.driverPhoto).into(holder.imvDriverPhoto);
        }

        holder.txvDriverName.setText(driverModel.driverName);
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imvDriverPhoto;
        TextView txvDriverName;

        @SuppressLint("WrongViewCast" )
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            imvDriverPhoto = (RoundedImageView)itemView.findViewById(R.id.imvDriverPhoto);
            txvDriverName = (TextView)itemView.findViewById(R.id.txvDriverName);
        }
    }
}
