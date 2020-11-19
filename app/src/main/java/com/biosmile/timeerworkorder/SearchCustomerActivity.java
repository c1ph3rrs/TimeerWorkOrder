package com.biosmile.timeerworkorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchCustomerActivity extends AppCompatActivity {

    Spinner customerNamesSpinner;
    DatabaseReference customerSpinnerRef;
    Button searchCustomerBtn;
    ImageView searchCustomerBackIcon;
    ArrayList<String> customerList = new ArrayList<>();
    String customerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        customerSpinnerRef = FirebaseDatabase.getInstance().getReference().child("Customers");

        searchCustomerBtn = findViewById(R.id.search_customer_btn);
        customerNamesSpinner = findViewById(R.id.choose_customer_spinner);
        searchCustomerBackIcon = findViewById(R.id.search_customer_back_icon);

        searchCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent searchCustomerIntent = new Intent(getApplicationContext(), SearchCustomerWorkActivity.class);
                searchCustomerIntent.putExtra("customerName", customerNamesSpinner.getSelectedItem().toString());
                startActivity(searchCustomerIntent);
            }
        });

        searchCustomerBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCustomerActivity.super.onBackPressed();
            }
        });

        showSpinnerData();
    }

    private void showSpinnerData() {

        customerSpinnerRef.orderByChild("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    customerList.add(items.child("companyName").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, customerList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerNamesSpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        SearchCustomerActivity.super.onBackPressed();
    }
}