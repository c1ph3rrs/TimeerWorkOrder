package com.biosmile.timeerworkorder.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.biosmile.timeerworkorder.Clients.ClientsDetailActivity;
import com.biosmile.timeerworkorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserDetailActivity extends AppCompatActivity {

    ImageView userDetailBackIcon, userDetailDeleteIcon;
    TextInputLayout viewUserDetailUsernameTxt, viewUserDetailPasswordTxt, viewUserDetailConfirmPasswordTxt;
    String viewUserDetailUsername, viewUserDetailPassword, viewUserDetailConfirmPassword;
    Button viewUserDetailApplyBtn;

    String getViewUserUsername;
    ProgressDialog loadingDialog;

    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        getViewUserUsername = getIntent().getStringExtra("userName");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        viewUserDetailUsernameTxt = findViewById(R.id.user_detail_username_txt);
        viewUserDetailPasswordTxt = findViewById(R.id.user_detail_password_txt);
        viewUserDetailConfirmPasswordTxt = findViewById(R.id.user_detail_password_re_enter_txt);
        viewUserDetailApplyBtn = findViewById(R.id.user_detail_apply_changes_btn);
        userDetailDeleteIcon = findViewById(R.id.user_delete_icon);
        userDetailBackIcon = findViewById(R.id.user_detail_back_icon);

        userDetailBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.super.onBackPressed();
            }
        });

        userDetailDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setTitle("Are you sure you want to Delete this User.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            deleteUserIcon();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });

        loadingDialog = new ProgressDialog(this);

        viewUserDetailUsernameTxt.getEditText().setText(getViewUserUsername);

        viewUserDetailApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserData();
            }
        });

    }

    private void deleteUserIcon() {

        userRef.child(getViewUserUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UserDetailActivity.super.onBackPressed();
                Toast.makeText(getApplicationContext(), "User Record Deleted", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void validateUserData() {

        viewUserDetailUsername = viewUserDetailUsernameTxt.getEditText().getText().toString().trim();
        viewUserDetailPassword = viewUserDetailPasswordTxt.getEditText().getText().toString().trim();
        viewUserDetailConfirmPassword = viewUserDetailConfirmPasswordTxt.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(viewUserDetailUsername)){
            Toast.makeText(getApplicationContext(), "Please Enter the Username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewUserDetailPassword)){
            Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewUserDetailConfirmPassword)){
            Toast.makeText(getApplicationContext(), "Please Re-Enter the Password", Toast.LENGTH_SHORT).show();
        }else if(!(viewUserDetailPassword.equals(viewUserDetailConfirmPassword))){
            Toast.makeText(getApplicationContext(), "Password Not Match", Toast.LENGTH_SHORT).show();
        }else{
            updateUserData();
        }

    }

    private void updateUserData() {

        loadingDialog.setTitle("Apply Changing...");
        loadingDialog.setMessage("Please wait while we are uploading...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("password", viewUserDetailPassword);

        userRef.child(getViewUserUsername).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                    UserDetailActivity.super.onBackPressed();
                    finish();

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Make Sure you have an active INTERNET. OR Please Try Again ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        UserDetailActivity.super.onBackPressed();
    }
}