package com.biosmile.timeerworkorder.Clients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.biosmile.timeerworkorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewCustomerActivity extends AppCompatActivity {

    Button addNewCustomerBtn;
    TextInputLayout customerNameTxt, companyNameTxt, addressTxt, cityTxt, stateTxt, postalTxt, customerEmailTxt, customerPhoneTxt;
    String customerName, companyName, address, city, state, postal, email, phone;
    ProgressDialog loadingDialog;
    ImageView newCustomerBackIcon;

    String currentDate, currentTime, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        newCustomerBackIcon = findViewById(R.id.new_customer_back_icon);
        addNewCustomerBtn = findViewById(R.id.add_new_customer_btn);
        customerNameTxt = findViewById(R.id.new_contact_name_txt);
        companyNameTxt = findViewById(R.id.new_company_name_txt);
        addressTxt = findViewById(R.id.new_address_txt);
        cityTxt = findViewById(R.id.new_city_txt);
        stateTxt = findViewById(R.id.new_state_txt);
        postalTxt = findViewById(R.id.new_zip_code_txt);
        customerEmailTxt = findViewById(R.id.new_email_txt);
        customerPhoneTxt = findViewById(R.id.new_phone_no_txt);

        loadingDialog = new ProgressDialog(this);

        newCustomerBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCustomerActivity.super.onBackPressed();
            }
        });

        addNewCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {

        customerName = customerNameTxt.getEditText().getText().toString().trim();
        customerName = customerName.substring(0, 1).toUpperCase() + customerName.substring(1).toLowerCase();
        companyName = companyNameTxt.getEditText().getText().toString().trim();
        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1).toLowerCase();
        address = addressTxt.getEditText().getText().toString().trim();
        state = stateTxt.getEditText().getText().toString().trim();
        city = cityTxt.getEditText().getText().toString().trim();
        postal = postalTxt.getEditText().getText().toString().trim();
        email = customerEmailTxt.getEditText().getText().toString().trim();
        phone = customerPhoneTxt.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(customerName)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Customer Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Company Name", Toast.LENGTH_SHORT).show();
        } else {

            if (TextUtils.isEmpty(city)) {
                city = "none";
            }

            if (TextUtils.isEmpty(address)) {
                address = "none";
            }

            if (TextUtils.isEmpty(state)) {
                state = "none";
            }

            if (TextUtils.isEmpty(postal)) {
                postal = "none";
            }

            if (TextUtils.isEmpty(email)) {
                email = "none";
            }

            if (TextUtils.isEmpty(phone)) {
                phone = "none";
            }

            validateAndUploadData();

        }

    }

    private void validateAndUploadData() {

        loadingDialog.setTitle("Customer Adding");
        loadingDialog.setMessage("Please wait while we are checking if customer exist or not");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Customers").child(productId).child(companyName).exists()) {
                    Toast.makeText(getApplicationContext(), "Company with this name Already Exist", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> customerData = new HashMap<>();
                    customerData.put("customerName", customerName);
                    customerData.put("companyName", companyName);
                    customerData.put("address", address);
                    customerData.put("customerEmail", email);
                    customerData.put("customerPhone", phone);
                    customerData.put("city", city);
                    customerData.put("state", state);
                    customerData.put("postal", postal);
                    customerData.put("customerID", productId);

                    rootRef.child("Customers").child(productId).updateChildren(customerData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Customer Added Successfully", Toast.LENGTH_LONG).show();
                            NewCustomerActivity.super.onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        NewCustomerActivity.super.onBackPressed();
    }
}