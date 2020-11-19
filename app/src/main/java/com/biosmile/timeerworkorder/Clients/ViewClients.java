package com.biosmile.timeerworkorder.Clients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biosmile.timeerworkorder.Model.Clients;
import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.R;
import com.biosmile.timeerworkorder.ViewHolder.ClientsViewHolder;
import com.biosmile.timeerworkorder.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewClients extends AppCompatActivity {

    RecyclerView clientsRecycler;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference clientsRef;
    FirebaseRecyclerAdapter<Clients, ClientsViewHolder> adapter;

    ImageView viewCLientsBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clients);

        clientsRef = FirebaseDatabase.getInstance().getReference().child("Customers");

        clientsRecycler = findViewById(R.id.view_clients_recycler);
        viewCLientsBackIcon = findViewById(R.id.view_clients_back_icon);

        viewCLientsBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewClients.super.onBackPressed();
            }
        });

        startCustomerRecycler();

    }

    private void startCustomerRecycler() {

        clientsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        clientsRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Clients> options =
                new FirebaseRecyclerOptions.Builder<Clients>().setQuery(clientsRef.orderByChild("companyName"), Clients.class).build();

        adapter = new FirebaseRecyclerAdapter<Clients, ClientsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ClientsViewHolder clientsViewHolder, int i, @NonNull Clients clients) {
                clientsViewHolder.companyNameTxt.setText(clients.getCompanyName());
                clientsViewHolder.clientNameTxt.setText(clients.getCustomerPhone());
//                Picasso.get().load(R.drawable.forward_icon).into(clientsViewHolder.nextIcon);

                clientsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clientIntent = new Intent(getApplicationContext(), ClientsDetailActivity.class);
                        clientIntent.putExtra("contactName", clients.getCustomerName());
                        clientIntent.putExtra("companyName", clients.getCompanyName());
                        clientIntent.putExtra("address", clients.getAddress());
                        clientIntent.putExtra("email", clients.getCustomerEmail());
                        clientIntent.putExtra("phone", clients.getCustomerPhone());
                        clientIntent.putExtra("city", clients.getCity());
                        clientIntent.putExtra("state", clients.getState());
                        clientIntent.putExtra("postal", clients.getPostal());
                        clientIntent.putExtra("customerId", clients.getCustomerID());
                        startActivity(clientIntent);
                    }
                });
            }

            @NonNull
            @Override
            public ClientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_card_list, parent, false);
                ClientsViewHolder holder = new ClientsViewHolder(view);
                return holder;
            }
        };

        clientsRecycler.setAdapter(adapter);
        adapter.startListening();

    }
}