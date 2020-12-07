package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.ListItem.Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Toast;
import java.util.ArrayList;

public class MyDonation extends AppCompatActivity {
    TextView textView ;

    @Override
    protected void onResume() {
        super.onResume();
        if (myDonations.size()==0)
            textView.setText("No Donations");
        else
            textView.setText("");
        adapter.notifyDataSetChanged();


    }

    Adapter adapter;
    RecyclerView recyclerView;
   public static ArrayList<Donation> myDonations;

    ArrayList<String>keys;
    RecylclerViewClickListener listener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation);


        keys=new ArrayList<>();

        textView=findViewById(R.id.myDonations_text);
        textView.setText("");

        listener=new RecylclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
               // Toast.makeText(MyDonation.this, keys.get(position), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(MyDonation.this , DonationDetails.class);
                intent.putExtra("Position" ,position );
                intent.putExtra("Key" , keys.get(position));
                startActivity(intent);

            }
        };
        ////Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");


        myDonations = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Adapter(myDonations,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LoadItem();

        recyclerView.setAdapter(adapter);

       /* if (myDonations.size()==0)
            textView.setText("No Donations");
        else
            textView.setText("");*/

    }



    private void LoadItem() {
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

               for (DataSnapshot s : dataSnapshot.child(User.e_mail).getChildren()) {
                   Donation donation = s.getValue(Donation.class);
                   myDonations.add(donation);
                 keys.add(s.getRef().getKey());

                   if (myDonations.size()==0)
                       textView.setText("No Donations");
                   else
                       textView.setText("");
                }
               progressBar.setVisibility(View.INVISIBLE);

               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
