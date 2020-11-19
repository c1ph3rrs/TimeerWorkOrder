package com.biosmile.timeerworkorder.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.biosmile.timeerworkorder.Dashboard;
import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.Prevalent.Prevalent;
import com.biosmile.timeerworkorder.R;
import com.biosmile.timeerworkorder.RetailerMainPage;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout usernameTxt, passwordTxt;
    Button loginBtn, signUpBtn;
    ImageView loginCloseBtn;
    CheckBox rememberMe;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        usernameTxt = findViewById(R.id.username_txt);
        passwordTxt = findViewById(R.id.password_txt);
        loginCloseBtn = findViewById(R.id.login_close_btn);
        rememberMe = findViewById(R.id.login_user_remember_me);
        loadingBar = new ProgressDialog(this);

        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnection();
            }
        });

        loginCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.super.onBackPressed();
                finish();
            }
        });
    }

    private void checkInternetConnection() {
        if (!isConnected(this)) {
            showCustomDialog();
        } else {
            checkUserAndLogin();
        }
    }

    private boolean isConnected(LoginActivity loginRetailer) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginRetailer.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkUserAndLogin() {

        String username = usernameTxt.getEditText().getText().toString().trim();
        String password = passwordTxt.getEditText().getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Username Field is Empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Password Field is Empty", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Login...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccessToAccount(username, password);
        }
    }

    private void allowAccessToAccount(final String username, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(username).exists()) {

                    String passwordFromDB = dataSnapshot.child("Users").child(username).child("password").getValue(String.class);

                    if (passwordFromDB.equals(password)) {

                        if (rememberMe.isChecked()) {
                            Paper.book().write(Prevalent.userUserNameKey, username);
                            Paper.book().write(Prevalent.userUserPasswordKey, password);
                        }

                        loadingBar.dismiss();

                        String dbPassword = dataSnapshot.child("Users").child(username).child("password").getValue(String.class);
                        String dbPhone = dataSnapshot.child("Users").child(username).child("phoneNo").getValue(String.class);
                        String dbUserName = dataSnapshot.child("Users").child(username).child("username").getValue(String.class);

                        Users userData = new Users(dbPassword, dbUserName);
                        Prevalent.currentOnlineUser = userData;

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Password does not Match", Toast.LENGTH_LONG).show();
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Account with this " + username + " username do not exists.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void validateData() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }
}