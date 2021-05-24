package com.example.mychatapplication.Adapters;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mychatapplication.Models.Chat;
import com.example.mychatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    List<Chat> mChat;
    String hisId;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    public AdapterChat(List<Chat> mChat, String hisId) {
        this.mChat = mChat;
        this.hisId = hisId;
    }
    boolean sender;


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_chat_right, parent, false);
            return new MyHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.messageTv.setText(chat.getMessage());
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        String timestamp = chat.getTimestamp();
        try {
            calendar.setTimeInMillis(Long.parseLong(timestamp));
        }catch (Exception e){
        }

        String timeDate = DateFormat.format("dd hh:mm aa", calendar).toString();

        holder.timeTv.setText(timeDate);
        if (position == mChat.size()-1){
            if (mChat.get(position).isSeen()){
                holder.isSeenTv.setText("seen");
            }else {
                holder.isSeenTv.setText("delivered");
            }
        }else {
            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils
                .equals(mChat.get(position).getSender(),
                FirebaseAuth.getInstance().getCurrentUser().getUid())
        ){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
        }
    }
}
