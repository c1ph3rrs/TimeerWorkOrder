package com.biosmile.timeerworkorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biosmile.timeerworkorder.Interface.setOnCLickListener;
import com.biosmile.timeerworkorder.R;

public class ClientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public setOnCLickListener listener;
    public TextView companyNameTxt, clientNameTxt;
    public ImageView nextIcon;

    public ClientsViewHolder(@NonNull View itemView) {
        super(itemView);

        companyNameTxt = itemView.findViewById(R.id.card_customer_name_card);
        clientNameTxt = itemView.findViewById(R.id.card_company_name_card);
        nextIcon = itemView.findViewById(R.id.customer_next);
    }

    public void setItemClickListener(setOnCLickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }
}
