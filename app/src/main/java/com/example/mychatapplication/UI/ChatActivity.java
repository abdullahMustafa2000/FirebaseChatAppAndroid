package com.example.mychatapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.Adapters.AdapterChat;
import com.example.mychatapplication.Models.Chat;
import com.example.mychatapplication.Models.UsersModel;
import com.example.mychatapplication.Notification.ApiService;
import com.example.mychatapplication.Notification.Client;
import com.example.mychatapplication.Notification.Data;
import com.example.mychatapplication.Notification.Response;
import com.example.mychatapplication.Notification.Sender;
import com.example.mychatapplication.Notification.Token;
import com.example.mychatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.mychatapplication.Adapters.AdapterUsers.HIS_IMAGE;
import static com.example.mychatapplication.Adapters.AdapterUsers.HIS_NAME;
import static com.example.mychatapplication.Adapters.AdapterUsers.HIS_UID;
import static com.example.mychatapplication.Notification.FirebaseService.TOKEN_REF;
import static com.example.mychatapplication.UI.RegisterActivity.USERS_REF;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    protected CircleImageView profileIv;
    protected TextView nameTv;
    protected TextView userStatusTv;
    protected Toolbar toolbar;
    protected RecyclerView chatRecyclerview;
    protected EditText messageEt;
    protected ImageButton sendBtn;
    protected LinearLayout chatLayout;

    DatabaseReference mDataRef;
    FirebaseDatabase mDb;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String myUid;
    private String hisUid;

    public static final String CHAT_REF = "Chats";
    public static final String PRIVATE_CHAT = "Private chats";

    List<Chat>chatList;
    AdapterChat adapterChat;
    private String hisName;
    static ChatActivity chatActivity;

    ApiService apiService;
    boolean notify = false;
    private String hisImage;
    private DatabaseReference userRefSeen;
    private ValueEventListener seenListener;

    public static ChatActivity getInstance(){
        return chatActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat);
        initView();
        chatActivity = this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerview.setHasFixedSize(true);
        chatRecyclerview.setLayoutManager(linearLayoutManager);
        chatList = new ArrayList<>();
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(ApiService.class);
        mDb = FirebaseDatabase.getInstance();
        mDataRef = mDb.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            myUid = firebaseUser.getUid();
        }
        Intent intent = getIntent();
        hisUid = intent.getStringExtra(HIS_UID);
        hisName = intent.getStringExtra(HIS_NAME);
        hisImage = intent.getStringExtra(HIS_IMAGE);
        nameTv.setText(hisName);
        try {
            Picasso.get().load(hisImage).placeholder(R.drawable.ic_account_circle_black_24dp).into(profileIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_account_circle_black_24dp).into(profileIv);
        }
        String roomId = setOneToOneChat(myUid, hisUid);
        getMessages(roomId);
        seenMessages(roomId);

        Query query = mDataRef.orderByChild("uid").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userStatusTv.setText(snapshot.child("onlineStatus").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "reopen the application", Toast.LENGTH_SHORT).show();
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


    public void seenMessages(String roomId){
        userRefSeen = FirebaseDatabase.getInstance().getReference(CHAT_REF);
        seenListener = userRefSeen.child(PRIVATE_CHAT).child(roomId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isSeen", true);
                    snapshot.getRef().updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getMessages(String roomId){
            mDataRef.child(CHAT_REF).child(PRIVATE_CHAT).child(roomId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatList.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Chat chat = ds.getValue(Chat.class);
                        chatList.add(chat);
                        adapterChat = new AdapterChat(chatList, hisUid);
                        adapterChat.notifyDataSetChanged();
                        chatRecyclerview.setAdapter(adapterChat);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
    }

    public void sendMessage(final String message, String mUID, final String hUID){
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
                if (notify)
                sendNotification(hUID, users.getName(), message);
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String hUID, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference(TOKEN_REF);
        Query query = allTokens.orderByKey().equalTo(hUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String token = snapshot.getValue(String.class);
                    Token token1 = new Token(token);
                    Data data = new Data(myUid, name+" : "+ message, "New Message", hisUid, R.drawable.ic_basic_profile_image);
                    Sender sender = new Sender(data, token1.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        Toast.makeText(ChatActivity.this, "etb3tet", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nameTv) {

        } else if (view.getId() == R.id.sendBtn) {
            String message = messageEt.getText().toString();
            if (!TextUtils.isEmpty(message)){
                notify = true;
                sendMessage(message, myUid, hisUid);
                messageEt.setText("");
            }
        }
    }

    private void initView() {
        profileIv = (CircleImageView) findViewById(R.id.profileIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        nameTv.setOnClickListener(ChatActivity.this);
        userStatusTv = (TextView) findViewById(R.id.userStatusTv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chatRecyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);
        messageEt = (EditText) findViewById(R.id.messageEt);
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(ChatActivity.this);
        chatLayout = (LinearLayout) findViewById(R.id.chat_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                break;

            case R.id.action_search:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        //checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefSeen.removeEventListener(seenListener);
    }
}
