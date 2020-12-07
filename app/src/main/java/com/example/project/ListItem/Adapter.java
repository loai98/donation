package com.example.project.ListItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Donation;
import com.example.project.Home;
import com.example.project.R;
import com.example.project.RecylclerViewClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {


    public static RecylclerViewClickListener listener;
    ArrayList<Donation>donations ;
    public Adapter(){}

    public Adapter(ArrayList<Donation> donations , RecylclerViewClickListener listener) {
        this.donations = donations;
        this.listener =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.titleTextView.setText(donations.get(position).getTitle());
        holder.dateTextView.setText(donations.get(position).getDate());
        //Picasso.with().load(donations.get(position).getImage()).into(holder.imageView);
        Picasso.get().load(donations.get(position).getImage()).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return donations.size();
    }



}
