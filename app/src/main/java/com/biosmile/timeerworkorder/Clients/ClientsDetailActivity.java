package com.biosmile.timeerworkorder.Clients;

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

import com.biosmile.timeerworkorder.R;
import com.biosmile.timeerworkorder.Users.UserDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ClientsDetailActivity extends AppCompatActivity {

    TextInputLayout viewCompanyNameTxt, viewContactNameTxt, viewEmailTxt, viewPhoneNoTxt, viewAddressTxt, viewCityTxt, viewStateTxt, viewPostalTxt;
    Button viewApplyChangesBtn;
    ImageView viewDeleteBtn, viewClientBackIcon;

    DatabaseReference clientRef;

    String viewCompanyName, viewContact, viewEmail, viewPhone, viewAddress, viewCity, viewState, viewPostal;
    ProgressDialog loadingDialog;

    String getCompanyName, getContactName, getEmail, getPhone, getAddress, getCity, getState, getPostal, getClientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_detail);

        getCompanyName = getIntent().getStringExtra("companyName");
        getContactName = getIntent().getStringExtra("contactName");
        getAddress = getIntent().getStringExtra("address");
        getEmail = getIntent().getStringExtra("email");
        getPhone = getIntent().getStringExtra("phone");
        getCity = getIntent().getStringExtra("city");
        getState = getIntent().getStringExtra("state");
        getPostal = getIntent().getStringExtra("postal");
        getClientId = getIntent().getStringExtra("customerId");

        clientRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(getClientId);

        viewCompanyNameTxt = findViewById(R.id.view_customer_name_txt);
        viewContactNameTxt = findViewById(R.id.view_contact_name_txt);
        viewEmailTxt = findViewById(R.id.view_email_txt);
        viewPhoneNoTxt = findViewById(R.id.view_phone_no_txt);
        viewAddressTxt = findViewById(R.id.view_address_txt);
        viewCityTxt = findViewById(R.id.view_city_txt);
        viewStateTxt = findViewById(R.id.view_state_txt);
        viewPostalTxt = findViewById(R.id.view_zip_code_txt);
        viewDeleteBtn = findViewById(R.id.view_client_delete_btn);
        viewClientBackIcon = findViewById(R.id.clients_detail_back_icon);
        
        viewCompanyNameTxt.getEditText().setText(getCompanyName);
        viewContactNameTxt.getEditText().setText(getContactName);
        viewAddressTxt.getEditText().setText(getAddress);
        viewEmailTxt.getEditText().setText(getEmail);
        viewPhoneNoTxt.getEditText().setText(getPhone);
        viewCityTxt.getEditText().setText(getCity);
        viewStateTxt.getEditText().setText(getState);
        viewPostalTxt.getEditText().setText(getPostal);
        
        viewDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientsDetailActivity.this);
                builder.setTitle("Are you sure you want to Delete this User.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            clientDeleteData();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });

        viewClientBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientsDetailActivity.super.onBackPressed();
            }
        });

        loadingDialog = new ProgressDialog(this);

        viewApplyChangesBtn = findViewById(R.id.view_new_customer_btn);

        viewApplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateClientData();
            }
        });

    }

    private void clientDeleteData() {

        clientRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ClientsDetailActivity.super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Client Record Deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateClientData() {

        viewCompanyName = viewCompanyNameTxt.getEditText().getText().toString().trim();
        viewContact = viewContactNameTxt.getEditText().getText().toString().trim();
        viewEmail = viewEmailTxt.getEditText().getText().toString().trim();
        viewPhone = viewPhoneNoTxt.getEditText().getText().toString().trim();
        viewAddress = viewAddressTxt.getEditText().getText().toString().trim();
        viewCity = viewCityTxt.getEditText().getText().toString().trim();
        viewState = viewStateTxt.getEditText().getText().toString().trim();
        viewPostal = viewPostalTxt.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(viewCompanyName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Company Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewContact)){
            Toast.makeText(getApplicationContext(), "Please Enter the Contact Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewEmail)){
            Toast.makeText(getApplicationContext(), "Please Enter the Email Address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewPhone)){
            Toast.makeText(getApplicationContext(), "Please Enter the Phone No", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewAddress)){
            Toast.makeText(getApplicationContext(), "Please Enter the Address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewCity)){
            Toast.makeText(getApplicationContext(), "Please Enter the City Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewState)){
            Toast.makeText(getApplicationContext(), "Please Enter the State Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(viewPostal)){
            Toast.makeText(getApplicationContext(), "Please Enter the Postal Code", Toast.LENGTH_SHORT).show();
        }else{
            updateClientData();
        }

    }

    private void updateClientData() {

        loadingDialog.setTitle("Apply Changing");
        loadingDialog.setMessage("Please wait while we upload");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("customerName", viewContact);
        clientMap.put("companyName", viewCompanyName);
        clientMap.put("address", viewAddress);
        clientMap.put("customerEmail", viewEmail);
        clientMap.put("customerPhone", viewPhone);
        clientMap.put("city", viewCity);
        clientMap.put("state", viewState);
        clientMap.put("postal", viewPostal);
        clientMap.put("customerID", getClientId);
        clientRef.updateChildren(clientMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                    ClientsDetailActivity.super.onBackPressed();
                    finish();
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Make Sure you have an active INTERNET. OR Please Try Again ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}