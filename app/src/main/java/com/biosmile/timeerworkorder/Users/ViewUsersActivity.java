package com.biosmile.timeerworkorder.Users;

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

import com.biosmile.timeerworkorder.Model.Users;
import com.biosmile.timeerworkorder.R;
import com.biosmile.timeerworkorder.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ViewUsersActivity extends AppCompatActivity {

    RecyclerView userRecyclerView;
    LinearLayoutManager layoutManager;

    DatabaseReference userRef;
    FirebaseRecyclerAdapter<Users, UserViewHolder> adapter;
    ImageView viewUserBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userRecyclerView = findViewById(R.id.view_clients_recycler);
        viewUserBackIcon = findViewById(R.id.view_user_back_icon);

        viewUserBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUsersActivity.super.onBackPressed();
            }
        });

        startUserRecycler();

    }


    private void startUserRecycler() {

        userRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>().setQuery(userRef.orderByChild("username"), Users.class).build();


        adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull Users user) {
                userViewHolder.userEmailCardTxt.setText(user.getUsername());

                userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userIntent = new Intent(getApplicationContext(), UserDetailActivity.class);
                            userIntent.putExtra("userName", user.getUsername());
                        startActivity(userIntent);
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_list, parent, false);
                UserViewHolder holder = new UserViewHolder(view);
                return holder;
            }
        };


        userRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
