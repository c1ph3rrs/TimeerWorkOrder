package com.biosmile.timeerworkorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.biosmile.timeerworkorder.Clients.NewCustomerActivity;
import com.biosmile.timeerworkorder.Clients.ViewClients;
import com.biosmile.timeerworkorder.Users.NewUserActivity;
import com.biosmile.timeerworkorder.Users.ViewUsersActivity;

import io.paperdb.Paper;

public class Dashboard extends AppCompatActivity {

    ImageView newCustomerBtn, addWorkDetail, logoutBtn, viewCustomers, newUserBtn, viewUserBtn, workDetailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        newCustomerBtn = findViewById(R.id.new_customer_btn);
        addWorkDetail = findViewById(R.id.add_work_detail);
        logoutBtn = findViewById(R.id.seller_logout_icon);
        viewCustomers = findViewById(R.id.view_customers_btn);
        newUserBtn = findViewById(R.id.add_new_user);
        viewUserBtn = findViewById(R.id.view_users_btn);
        workDetailBtn = findViewById(R.id.view_work_detail);

        newCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewCustomerActivity.class));
            }
        });

        addWorkDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomersList.class));
            }
        });

        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewUserActivity.class));
            }
        });

        viewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewUsersActivity.class));
            }
        });

        viewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewClients.class));
            }
        });

        workDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchCustomerActivity.class));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Are you sure you want to Logout.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Paper.book().destroy();
                            Intent logoutIntent = new Intent(getApplicationContext(), SplashScreen.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(logoutIntent);
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

    }
}