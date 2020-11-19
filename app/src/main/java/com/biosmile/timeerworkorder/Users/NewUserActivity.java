package com.biosmile.timeerworkorder.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.biosmile.timeerworkorder.Dashboard;
import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.Prevalent.Prevalent;
import com.biosmile.timeerworkorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NewUserActivity extends AppCompatActivity {

    Button addNewUserBtn;
    TextInputLayout newUserEmailTxt, newUserPasswordTxt, newUserReEnterPasswordTxt, newUserNameTxt;
    ImageView newUserBackIcon;

    String newUserEmail, newUserPass, newUserReEnterPass;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        addNewUserBtn = findViewById(R.id.add_new_user_btn);
        newUserEmailTxt = findViewById(R.id.new_user_email_txt);
        newUserPasswordTxt = findViewById(R.id.new_user_password_txt);
        newUserReEnterPasswordTxt = findViewById(R.id.new_user_confirm_password_txt);

        loadingDialog = new ProgressDialog(this);
        newUserBackIcon = findViewById(R.id.new_user_back_icon);

        newUserBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUserActivity.super.onBackPressed();
            }
        });

        addNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateNewUserData();
            }
        });
    }

    private void validateNewUserData() {

        newUserEmail = newUserEmailTxt.getEditText().getText().toString().trim();
        newUserPass = newUserPasswordTxt.getEditText().getText().toString().trim();
        newUserReEnterPass = newUserReEnterPasswordTxt.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(newUserEmail)){
            Toast.makeText(getApplicationContext(), "Please Enter the Username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(newUserPass)){
            Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(newUserReEnterPass)){
            Toast.makeText(getApplicationContext(), "Please Re Enter the Password", Toast.LENGTH_SHORT).show();
        }else if(!(newUserPass.equals(newUserReEnterPass))){
            Toast.makeText(getApplicationContext(), "Password Not match", Toast.LENGTH_SHORT).show();
        }else{
            uploadUserData();
        }

    }

    private void uploadUserData() {

        loadingDialog.setTitle("Please wait ");
        loadingDialog.setMessage("User is Creating...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> userData = new HashMap<>();
                userData.put("username", newUserEmail);
                userData.put("password", newUserPass);

                rootRef.child("Users").child(newUserEmail).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismiss();

                        Users userData = new Users(newUserPass, newUserEmail);
//                                            Users userData = new Users()
                        Prevalent.currentOnlineUser = userData;

                        Intent otpIntent = new Intent(getApplicationContext(), Dashboard.class);
                        otpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(otpIntent);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        NewUserActivity.super.onBackPressed();
    }
}