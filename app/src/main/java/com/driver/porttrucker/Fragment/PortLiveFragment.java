package com.driver.porttrucker.Fragment;

import android.annotation.SuppressLint;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Adapter.ChatAdapter;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Model.MessageModel;
import com.driver.porttrucker.R;
import com.driver.porttrucker.Utils.LogUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PortLiveFragment extends BaseFragment {

    View fragment;

    RecyclerView rvChatHistory;
    Button btnSend;
    EditText edtMsg;

    PortLiveFragment instance;

    DatabaseReference dbMsgRef;
    ChatAdapter chatAdapter;

    String roomId = "publicRoomId", msg;
    ArrayList<MessageModel> messages = new ArrayList<>();

    public PortLiveFragment() {
        dbMsgRef = FirebaseDatabase.getInstance().getReference().child("messages").child(roomId);
    }

    public static PortLiveFragment newInstance(){
        PortLiveFragment newInstance = new PortLiveFragment();
        return  newInstance;
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_port_live, parent, false);

        ((MainActivity)getActivity()).changeVisibleStateOfLinkButtons(View.GONE);

        btnSend = (Button)fragment.findViewById(R.id.btnSend); btnSend.setOnClickListener(this);
        edtMsg = (EditText)fragment.findViewById(R.id.edtMsg);

        rvChatHistory = (RecyclerView)fragment.findViewById(R.id.rvChatHistory);
        rvChatHistory.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chatAdapter = new ChatAdapter(getContext(), rvChatHistory);
        rvChatHistory.setAdapter(chatAdapter);

        getMessageHistory();

        return fragment;
    }

    private void getMessageHistory() {
        messages.clear();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot message : dataSnapshot.getChildren()){
                    MessageModel msg = message.getValue(MessageModel.class);
                    messages.add(msg);
                }
                chatAdapter.setChatHistory(messages);
                dbMsgRef.removeEventListener(this);

                addMessageListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbMsgRef.addValueEventListener(listener);
    }

    private void addMessageListener() {
        dbMsgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel msg = dataSnapshot.getValue(MessageModel.class);
                chatAdapter.addItem(msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private String getCurrentDateTime(){
        return new SimpleDateFormat("MM-dd HH:mm aaa").format(new Date());
    }
    private void sendMsg() {
        msg = edtMsg.getText().toString().trim();
        String msgId = String.valueOf(System.currentTimeMillis());
        MessageModel model = new MessageModel(
                Common.userModel.id,
                Constant.USER_PHOTO_BASE_URL + Common.userModel.profile,
                Common.userModel.firstName + " " + Common.userModel.lastName,
                msg,
                getCurrentDateTime()
        );

        dbMsgRef.child(msgId).setValue(model);
        edtMsg.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).changeVisibleStateOfLinkButtons(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                sendMsg();
                break;
        }
    }
}
