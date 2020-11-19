package com.biosmile.timeerworkorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.Model.Work;
import com.biosmile.timeerworkorder.Users.UserDetailActivity;
import com.biosmile.timeerworkorder.ViewHolder.UserViewHolder;
import com.biosmile.timeerworkorder.ViewHolder.WorkViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchCustomerWorkActivity extends AppCompatActivity {

    RecyclerView workDetailRecycler;
    LinearLayoutManager layoutManager;

    DatabaseReference workRef;
    FirebaseRecyclerAdapter<Work, WorkViewHolder> adapter;
    ImageView workBackIcon;

    TextView workEmployeeNameTxt;
    String customerNameTxt = "", othersDetailTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer_work);

        customerNameTxt = getIntent().getStringExtra("customerName");

        workRef = FirebaseDatabase.getInstance().getReference().child("Work").child(customerNameTxt);


        workBackIcon = findViewById(R.id.search_customer_back_btn);
        workEmployeeNameTxt = findViewById(R.id.search_customer_name_txt);
        workDetailRecycler = findViewById(R.id.search_customer_recycler);

        workEmployeeNameTxt.setText(customerNameTxt);

        workBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCustomerWorkActivity.super.onBackPressed();
            }
        });

        startWorkRecycler();

    }

    private void startWorkRecycler() {

        workDetailRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        workDetailRecycler.setLayoutManager(layoutManager);

        //.child(customerNameTxt).orderByChild("customerWorkDateDetail")

//        Query firebaseSearchQuery = workRef.orderByChild("customerName").startAt(customerNameTxt).endAt(customerNameTxt + "\uf8ff");
//        Query firebaseSearchQuery = workRef.orderByChild("customerWorkDateDetail");
        FirebaseRecyclerOptions<Work> options =
                new FirebaseRecyclerOptions.Builder<Work>().setQuery(workRef, Work.class).build();

        adapter = new FirebaseRecyclerAdapter<Work, WorkViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WorkViewHolder workViewHolder, int i, @NonNull Work work) {

                othersDetailTxt = work.getCustomerJobDetail();
                workViewHolder.timeWorkTxt.setText(work.getCustomerWorkDateDetail());
                workViewHolder.workDetailTxt.setText(othersDetailTxt);

                workViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent workIntent = new Intent(getApplicationContext(), WorkDetailActivity.class);
                        workIntent.putExtra("workHardware", work.getCustomerHardwareDetail());
                        workIntent.putExtra("workJobDetail", work.getCustomerJobDetail());
                        workIntent.putExtra("workName", work.getCustomerName());
                        workIntent.putExtra("workTravelDetail", work.getCustomerTravelDetail());
                        workIntent.putExtra("workDateDetail", work.getCustomerWorkDateDetail());
                        workIntent.putExtra("workTimeDetail", work.getCustomerWorkTimeDetail());
                        startActivity(workIntent);
                    }
                });

            }

            @NonNull
            @Override
            public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_work_card_view, parent, false);
                WorkViewHolder holder = new WorkViewHolder(view);
                return holder;
            }
        };

        workDetailRecycler.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        SearchCustomerWorkActivity.super.onBackPressed();
    }
}