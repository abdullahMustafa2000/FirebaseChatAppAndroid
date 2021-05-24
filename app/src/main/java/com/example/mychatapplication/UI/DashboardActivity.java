package com.example.mychatapplication.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mychatapplication.Fragments.HomeFragment;
import com.example.mychatapplication.Fragments.ProfileFragment;
import com.example.mychatapplication.Fragments.UsersFragment;
import com.example.mychatapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.example.mychatapplication.Notification.FirebaseService.TOKEN_REF;
import static com.example.mychatapplication.UI.RegisterActivity.USERS_REF;

public class DashboardActivity extends AppCompatActivity {

    protected FrameLayout frameLayout;
    protected BottomNavigationView navigation;
    FirebaseAuth firebaseAuth;
    public ActionBar actionBar;
    private String mUID;
    public static final String  CURRENT_UID = "current_id";
    public static final String  UID_SHARED_PREF = "uid_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_dashboard);
        initView();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            mUID = user.getUid();
            SharedPreferences sharedPreferences = getSharedPreferences(UID_SHARED_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CURRENT_UID, mUID);
            editor.apply();
        }
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener);
        setFragment(new HomeFragment(), "Home");
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    public void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TOKEN_REF);
        reference.child(mUID).setValue(token);
    }

    public void checkOnlineStatus(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(USERS_REF).child(mUID);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        reference.updateChildren(hashMap);
    }

    public void setFragment(Fragment fragment, String title){

        switch (title){
            case "Home":
                try {
                    actionBar.setTitle("Home");
                }catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case "Users":
                actionBar.setTitle("Users");
                break;
            case "Profile":
                actionBar.setTitle("Profile");
                break;
            case "Alarms":
                actionBar.setTitle("Alarms");
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "");
        fragmentTransaction.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    setFragment(new HomeFragment(), "Home");
                    return true;
                case R.id.nav_profile:
                    setFragment(new ProfileFragment(), "Profile");
                    return true;
                case R.id.nav_users:
                    setFragment(new UsersFragment(), "Users");
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
            }
        }
    };




    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(System.currentTimeMillis());
        String time = DateFormat.format("dd hh:mm aa", calendar).toString();
        checkOnlineStatus("Last seen "+time);
    }

    @Override
    protected void onResume() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            mUID = user.getUid();
            SharedPreferences sharedPreferences = getSharedPreferences(UID_SHARED_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CURRENT_UID, mUID);
            editor.apply();
        }
        checkOnlineStatus("online");
        super.onResume();
    }

    private void initView() {
        frameLayout = findViewById(R.id.frame_layout);
        navigation = findViewById(R.id.navigation);

    }
}
