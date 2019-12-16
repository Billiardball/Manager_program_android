package com.food.oder.Adapter;

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
import com.food.oder.Model.UserCommentModel;
import com.food.oder.R;

import java.util.ArrayList;

public class UserCommentAdapter  extends RecyclerView.Adapter<UserCommentAdapter.ViewHolder>{

    Context context;

    ArrayList<UserCommentModel> allData = new ArrayList<>();

    public UserCommentAdapter(Context context) {
        this.context = context;
    }

    public void loadData(ArrayList<UserCommentModel> allData){
        this.allData = allData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserCommentModel model = allData.get(position);
        Picasso.get().load(Constant.USER_PHOTO_BASE_URL + model.userPhoto).into(holder.imvPhoto);
        holder.txvComment.setText(model.comment);
        holder.txvUserName.setText(model.userName);
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imvPhoto;
        TextView txvUserName, txvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imvPhoto = (RoundedImageView)itemView.findViewById(R.id.imvPhoto);
            txvUserName = (TextView)itemView.findViewById(R.id.txvUserName);
            txvComment = (TextView)itemView.findViewById(R.id.txvComment);
        }
    }
}
