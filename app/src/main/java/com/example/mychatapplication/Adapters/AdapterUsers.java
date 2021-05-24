package com.example.mychatapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.Models.UsersModel;
import com.example.mychatapplication.R;
import com.example.mychatapplication.UI.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<UsersModel> userList;
    public static final String  HIS_UID = " his_uid";
    public static final String  HIS_IMAGE = "his_image";
    public static final String  HIS_NAME = "his_name";

    public AdapterUsers(Context context, List<UsersModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
        .inflate(R.layout.chat_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisUID = userList.get(position).getUid();
        final String userImage = userList.get(position).getImage();
        final String userName = userList.get(position).getName();
        final String userEmail = userList.get(position).getEmail();

        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);

        try {
            Picasso.get()
                    .load(userImage)
                    .placeholder(R.drawable.ic_face_black_24dp)
                    .into(holder.mAvatarIv);
        }catch (Exception e){
            Picasso.get()
                    .load(R.drawable.ic_face_black_24dp)
                    .placeholder(R.drawable.ic_face_black_24dp)
                    .into(holder.mAvatarIv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(HIS_UID, hisUID);
                intent.putExtra(HIS_IMAGE, userImage);
                intent.putExtra(HIS_NAME, userName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mNameTv, mEmailTv;
        ImageView mAvatarIv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTV);
            mEmailTv = itemView.findViewById(R.id.emailTV);
        }
    }
}
