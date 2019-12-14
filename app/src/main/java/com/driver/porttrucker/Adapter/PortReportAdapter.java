package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qhutch.elevationimageview.ElevationImageView;
import com.squareup.picasso.Picasso;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Model.PortReportModel;
import com.driver.porttrucker.R;

import java.util.ArrayList;

public class PortReportAdapter extends RecyclerView.Adapter<PortReportAdapter.CustomViewHolder> {

    Context context;

    ArrayList<PortReportModel> allData = new ArrayList<>();

    public PortReportAdapter(Context context, ArrayList<PortReportModel> allData) {
        this.context = context;
        this.allData = allData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_port_report, parent, false);
        CustomViewHolder holder = new CustomViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        PortReportModel model = allData.get(position);

        String imgRootRul = "https://porttrucker.app/images/news/" + model.created_at.split("-")[0] + "/" + model.created_at.split("-")[1] + "/";
        if (model.photo1.length() > 0 && model.photo2.length() > 0 && model.photo3.length() > 0 && model.photo4.length() > 0 ){
            holder.rytContainer1.setVisibility(View.GONE);
            holder.lytContainer2.setVisibility(View.VISIBLE);
            Picasso.get().load(imgRootRul + model.photo1).into(holder.imvPicture1);
            Picasso.get().load(imgRootRul + model.photo2).into(holder.imvPicture2);
            Picasso.get().load(imgRootRul + model.photo3).into(holder.imvPicture3);
            Picasso.get().load(imgRootRul + model.photo4).into(holder.imvPicture4);
        }else {
            holder.rytContainer1.setVisibility(View.VISIBLE);
            holder.lytContainer2.setVisibility(View.GONE);
            Picasso.get().load(imgRootRul + model.photo1).into(holder.imvPicture);
        }

        holder.txvTitle.setText(model.title);
        holder.txvCntOfViews.setText(model.views);
        holder.txvCntOfComments.setText(model.comments);
        holder.txvBody.setText(model.body);
        holder.imvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).showPortReportDetailFragment(model.id, allData);
            }
        });

        holder.lytContainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).showPortReportDetailFragment(model.id, allData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView imvPicture, imvPicture1, imvPicture2, imvPicture3, imvPicture4;
        TextView txvTitle, txvCntOfViews, txvCntOfComments, txvBody;
        RelativeLayout rytContainer1;
        LinearLayout lytContainer2;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            rytContainer1 = (RelativeLayout)itemView.findViewById(R.id.rytContainer1);
            lytContainer2 = (LinearLayout) itemView.findViewById(R.id.lytContainer2);

            imvPicture = (ImageView) itemView.findViewById(R.id.imvPicture);
            imvPicture1 = (ImageView)itemView.findViewById(R.id.imvPicture1);
            imvPicture2 = (ImageView)itemView.findViewById(R.id.imvPicture2);
            imvPicture3 = (ImageView)itemView.findViewById(R.id.imvPicture3);
            imvPicture4 = (ImageView)itemView.findViewById(R.id.imvPicture4);
            txvTitle = (TextView)itemView.findViewById(R.id.txvTitle);
            txvCntOfViews = (TextView)itemView.findViewById(R.id.txvCntOfViews);
            txvCntOfComments = (TextView)itemView.findViewById(R.id.txvCntOfComments);
            txvBody = (TextView)itemView.findViewById(R.id.txvBody);
        }
    }
}
