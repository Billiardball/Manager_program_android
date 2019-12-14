package com.driver.porttrucker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Model.MessageModel;
import com.driver.porttrucker.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    RecyclerView recyclerView;

    public ChatAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    ArrayList<MessageModel> allData = new ArrayList<>();

    public void setChatHistory(ArrayList<MessageModel> chatHistory){
        this.allData.clear();
        this.allData.addAll(chatHistory);
        notifyDataSetChanged();
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), getItemCount());
    }

    public void addItem(MessageModel item){
        allData.add(item);
        notifyDataSetChanged();
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat, null);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel model = allData.get(position);
        if (model.userId == Common.userModel.id){
            holder.lytFriendMsg.setVisibility(View.GONE);

            holder.lytMyMsg.setVisibility(View.VISIBLE);
            holder.txvMyDate.setText(model.date);
            holder.txvMyMsg.setText(model.message);
        }else {
            holder.lytMyMsg.setVisibility(View.GONE);

            holder.lytFriendMsg.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.photoUrl).into(holder.imvFriendPhoto);
            holder.txvFriendNameDate.setText(model.userName + ", " + model.date);
            holder.txvFriendMsg.setText(model.message);
        }
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout lytFriendMsg, lytMyMsg;
        RoundedImageView imvFriendPhoto;
        TextView txvFriendNameDate, txvFriendMsg, txvMyDate, txvMyMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytFriendMsg = (LinearLayout)itemView.findViewById(R.id.lytFriendMsg);
            imvFriendPhoto = (RoundedImageView)itemView.findViewById(R.id.imvFriendPhoto);
            txvFriendNameDate = (TextView)itemView.findViewById(R.id.txvFriendNameDate);
            txvFriendMsg = (TextView)itemView.findViewById(R.id.txvFriendMsg);

            lytMyMsg = (LinearLayout)itemView.findViewById(R.id.lytMyMsg);
            txvMyDate = (TextView)itemView.findViewById(R.id.txvMyDate);
            txvMyMsg = (TextView)itemView.findViewById(R.id.txvMyMsg);
        }
    }
}
