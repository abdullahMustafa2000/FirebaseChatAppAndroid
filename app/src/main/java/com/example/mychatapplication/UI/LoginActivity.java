package com.example.mychatapplication.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mychatapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.mychatapplication.Notification.FirebaseService.DEVICE_TOKEN;
import static com.example.mychatapplication.UI.DashboardActivity.UID_SHARED_PREF;
import static com.example.mychatapplication.UI.RegisterActivity.USERS_REF;

public class LoginActivity extends Activity implements View.OnClickListener {

    protected EditText emailEt;
    protected TextInputLayout emailTIL;
    protected EditText passwordEt;
    protected TextInputLayout passwordTIL;
    protected Button loginBtn;
    protected TextView notHaveAccountTV;

    FirebaseAuth mFAuth;
    ProgressDialog progressDialog;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);

        mFAuth = FirebaseAuth.getInstance();
        user = mFAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        initView();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginBtn) {

            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEt.setError("Invalid Email");
                } else if (password.length() < 6) {
                    passwordEt.setError("less than 6 characters!!");
                } else {
                    loginUser(email, password);
                }
            }else {
                Toast.makeText(this, "Empty!!", Toast.LENGTH_SHORT).show();
            }


        } else if (view.getId() == R.id.not_have_accountTV) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        mFAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mFAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid email or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        emailEt = (EditText) findViewById(R.id.emailEt);
        emailTIL = (TextInputLayout) findViewById(R.id.emailTIL);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
        passwordTIL = (TextInputLayout) findViewById(R.id.passwordTIL);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(LoginActivity.this);
        notHaveAccountTV = (TextView) findViewById(R.id.not_have_accountTV);
        notHaveAccountTV.setOnClickListener(LoginActivity.this);
    }

}
