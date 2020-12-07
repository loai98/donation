package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DonationDetails extends AppCompatActivity {

    TextView titleTextView, descriptionTextView, dateTextView;
    ImageView imageView;
    String key ;
    int position ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        titleTextView = findViewById(R.id.donationDetails_title);
        descriptionTextView = findViewById(R.id.donationDetails_description);
        dateTextView = findViewById(R.id.donationDetails_date);
        imageView = findViewById(R.id.donationDetails_image);


         key = getIntent().getStringExtra("Key");
         position = getIntent().getIntExtra("Position", -1);

        if (key != null && position != -1) {
            LoadItemInfornaion();
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadItemInfornaion() {
        titleTextView.setText(MyDonation.myDonations.get(position).getTitle());
        descriptionTextView.setText(MyDonation.myDonations.get(position).getDescription());
        dateTextView.setText(MyDonation.myDonations.get(position).getDate());
        Picasso.get().load(MyDonation.myDonations.get(position).getImage()).into(imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.donation_details_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.remove) {

            //remove item
            RemoveItem();

        } else if (item.getItemId() == R.id.showLocation) {
            Intent intent  =new Intent(DonationDetails.this , Donation_location.class);
            intent.putExtra("Position" , position);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    private void RemoveItem() {
        DatabaseReference databaseReference  =FirebaseDatabase.getInstance().getReference("Donations");

      databaseReference.child(User.e_mail).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful())
              {
                  Toast.makeText(DonationDetails.this, "Removed", Toast.LENGTH_SHORT).show();
                  MyDonation.myDonations.remove(position);
                  finish();
              }
          }
      });
    }
}
