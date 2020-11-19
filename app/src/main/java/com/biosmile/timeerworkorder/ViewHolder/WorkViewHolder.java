package com.biosmile.timeerworkorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biosmile.timeerworkorder.Interface.setOnCLickListener;
import com.biosmile.timeerworkorder.R;

public class WorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public setOnCLickListener listener;
    public TextView workDetailTxt, timeWorkTxt;
    public ImageView nextIcon;

    public WorkViewHolder(@NonNull View itemView) {
        super(itemView);

        timeWorkTxt = itemView.findViewById(R.id.card_time_txt);
        workDetailTxt = itemView.findViewById(R.id.card_work_detail_txt);

    }

    public void setItemClickListener(setOnCLickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }
}
