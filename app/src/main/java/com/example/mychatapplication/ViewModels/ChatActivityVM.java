package com.example.mychatapplication.ViewModels;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.Models.UsersModel;
import com.example.mychatapplication.Notification.ApiService;
import com.example.mychatapplication.Notification.Data;
import com.example.mychatapplication.Notification.Response;
import com.example.mychatapplication.Notification.Sender;
import com.example.mychatapplication.Notification.Token;
import com.example.mychatapplication.R;
import com.example.mychatapplication.UI.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.mychatapplication.Notification.FirebaseService.TOKEN_REF;
import static com.example.mychatapplication.UI.ChatActivity.CHAT_REF;
import static com.example.mychatapplication.UI.ChatActivity.PRIVATE_CHAT;
import static com.example.mychatapplication.UI.RegisterActivity.USERS_REF;

public class ChatActivityVM extends ViewModel {

    /*

    private DatabaseReference mDataRef;

    public void sendMessage(final String message, final String mUID, final String hUID, boolean notify, final ApiService apiService){
        mDataRef = FirebaseDatabase.getInstance().getReference();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", mUID);
        hashMap.put("receiver", hUID);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);
        mDataRef.child(CHAT_REF).child(PRIVATE_CHAT).child(setOneToOneChat(mUID, hUID)).push().setValue(hashMap);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(USERS_REF).child(mUID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersModel users = dataSnapshot.getValue(UsersModel.class);
                if (notify) {
                    sendNotification(hUID, users.getName(), message, mUID, apiService);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String setOneToOneChat(String uid1, String uid2) {
        if (uid1.compareTo(uid2) > 0) {
            return uid1 + uid2;
        } else {
            return uid2 + uid1;
        }
    }

    private void sendNotification(final String hUID, final String name, final String message, final String myUid, final ApiService apiService) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference(TOKEN_REF).child(hUID);
        allTokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, name+" : " +message, "New Message", hUID,
                            R.drawable.icon);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

     */
}
