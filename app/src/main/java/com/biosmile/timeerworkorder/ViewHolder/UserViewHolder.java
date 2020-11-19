package com.biosmile.timeerworkorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biosmile.timeerworkorder.Interface.setOnCLickListener;
import com.biosmile.timeerworkorder.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public setOnCLickListener listener;
    public TextView userEmailCardTxt;
    public ImageView nextIcon;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        userEmailCardTxt = itemView.findViewById(R.id.card_user_email);
        nextIcon = itemView.findViewById(R.id.user_icon_forward_card);
    }

    public void setItemClickListener(setOnCLickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }

}
