package com.example.project.ListItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.R;
import com.example.project.RecylclerViewClickListener;
import com.squareup.picasso.Picasso;

import static com.example.project.ListItem.Adapter.listener;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    ImageView imageView;
    TextView titleTextView ,dateTextView;



    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        imageView  =itemView.findViewById(R.id.list_item_image);
        titleTextView  =itemView.findViewById(R.id.list_item_title);
        dateTextView  =itemView.findViewById(R.id.list_item_date);
    }


    @Override
    public void onClick(View v) {

        listener.onClick(v , getAdapterPosition());
    }
}
