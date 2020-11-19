package com.biosmile.timeerworkorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.biosmile.timeerworkorder.Login.LoginActivity;
import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private static int timer = 5000;

    Animation top_animation, middle_animation, bottom_animation;
    View first, second, third, fourth, fifth, six;
    TextView logo, tagLine;

    String passwordKey, usernameKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        top_animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middle_animation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottom_animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.four_line);
        fifth = findViewById(R.id.five_line);
        six = findViewById(R.id.six_line);

        logo = findViewById(R.id.logo);
        tagLine = findViewById(R.id.tag_line);

        first.setAnimation(top_animation);
        second.setAnimation(top_animation);
        third.setAnimation(top_animation);
        fourth.setAnimation(top_animation);
        fifth.setAnimation(top_animation);
        six.setAnimation(top_animation);

        logo.setAnimation(middle_animation);
        tagLine.setAnimation(bottom_animation);

        Paper.init(this);
        usernameKey = Paper.book().read(Prevalent.userUserNameKey);
        passwordKey = Paper.book().read(Prevalent.userUserPasswordKey);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));
//                finish();

                if (usernameKey != "" && passwordKey != "") {
                    if (!TextUtils.isEmpty(usernameKey) && !TextUtils.isEmpty(passwordKey)) {
                        allowAccess(usernameKey, passwordKey);
                    } else {
                        Intent loginIntent = new Intent(getApplicationContext(), RetailerMainPage.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(loginIntent);
                    }
                } else {
                    Intent loginIntent = new Intent(getApplicationContext(), RetailerMainPage.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(loginIntent);
                }
            }
        }, timer);

    }

    private void allowAccess(final String usernameKey, final String passwordKey) {

        if(!isConnected(this)){
            showCustomDialog();
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(usernameKey).exists()) {

                    String passwordFromDB = dataSnapshot.child("Users").child(usernameKey).child("password").getValue(String.class);

                    if (passwordFromDB.equals(passwordKey)) {

                        String dbPassword = dataSnapshot.child("Users").child(usernameKey).child("password").getValue(String.class);
                        String dbPhone = dataSnapshot.child("Users").child(usernameKey).child("phoneNo").getValue(String.class);
                        String dbUserName = dataSnapshot.child("Users").child(usernameKey).child("username").getValue(String.class);

                        Users userData = new Users(dbPassword, dbUserName);
                        Prevalent.currentOnlineUser = userData;

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);

                    } else {
                        startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));
                    }

                } else {
                    startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isConnected(SplashScreen splashScreen) {
        ConnectivityManager connectivityManager = (ConnectivityManager) splashScreen.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }


    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}