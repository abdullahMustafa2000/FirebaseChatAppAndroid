package com.example.mychatapplication.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mychatapplication.R;
import com.example.mychatapplication.ViewModels.UserViewModel;
import com.example.mychatapplication.pojo.Tables.UserDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mychatapplication.Notification.FirebaseService.DEVICE_TOKEN;
import static com.example.mychatapplication.UI.DashboardActivity.UID_SHARED_PREF;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView emailEt;
    protected TextInputLayout emailTIL;
    protected TextView passwordEt;
    protected TextInputLayout passwordTIL;
    protected Button registerBtn;
    protected TextView haveAccountTV;
    protected EditText nameEt;
    protected EditText phoneEt;
    protected CircleImageView profileImage;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    public static final String USERS_REF = "Users";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;
    private String storagePath = "Users_Profile_imgs/";
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_register);
        initView();


        storageReference = getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS_REF);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering You...");

        mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerBtn) {

            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            String phone = phoneEt.getText().toString();
            String name = nameEt.getText().toString();

            if (name.isEmpty() || name.length() < 2){
                emailEt.setError("Required");
                emailEt.setFocusable(true);
            }else if(phone.length() < 11){
                phoneEt.setError("less than 11 number");
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEt.setError("Invalid Email");
            } else if (password.length() < 6) {
                passwordEt.setError("password length at least 6 chars");
            } else {
                registerUser(email, password);
            }
        } else if (view.getId() == R.id.have_accountTV) {
            startActivity(new Intent(this, LoginActivity.class));

        }
    }


    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = nameEt.getText().toString();
                            String phone = phoneEt.getText().toString();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", phone);
                            hashMap.put("image", "");
                            hashMap.put("onlineStatus", "online");
                            SharedPreferences preferences = getSharedPreferences(UID_SHARED_PREF, MODE_PRIVATE);
                            hashMap.put("Token", preferences.getString(DEVICE_TOKEN, ""));
                            UserDB userDB = new UserDB();
                            userDB.setName(name);
                            userDB.setPhone(phone);
                            userDB.setEmail(email);
                            userDB.setUid(uid);
                            userDB.token = preferences.getString(DEVICE_TOKEN, "");
                            userViewModel.insertUser(userDB);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference(USERS_REF);
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Welcome...\n" + userDB.getName(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterActivity.this, PickRegisterPhotoActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        emailEt = (TextView) findViewById(R.id.emailEt);
        emailTIL = (TextInputLayout) findViewById(R.id.emailTIL);
        passwordEt = (TextView) findViewById(R.id.passwordEt);
        passwordTIL = (TextInputLayout) findViewById(R.id.passwordTIL);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(RegisterActivity.this);
        haveAccountTV = (TextView) findViewById(R.id.have_accountTV);
        haveAccountTV.setOnClickListener(RegisterActivity.this);
        nameEt = (EditText) findViewById(R.id.nameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
    }

}
