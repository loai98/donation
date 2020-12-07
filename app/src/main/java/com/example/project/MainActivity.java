package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    User user;
    DatabaseReference databaseReference ;
    ImageView refresh ;
    ProgressBar progressBar;
    String Email ,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar  =findViewById(R.id.progressBar);
        refresh=findViewById(R.id.refresh);

        Paper.init(this);
        user=new User();
        databaseReference = FirebaseDatabase.getInstance().getReference();
         Email= Paper.book().read(Common.USER_KEY);
         pass=Paper.book().read(Common.PASSWORD_KEY);

       CheckConnection();

       refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               refresh.setVisibility(View.INVISIBLE);
               progressBar.setVisibility(View.VISIBLE);
               CheckConnection();
           }
       });



    }

    private void CheckConnection() {
        if(Common.isConnectedToInternet(this)){
            if(Email!=null && pass != null)
            {
                if(!Email.isEmpty() && !pass.isEmpty())
                {
                    login(Email);
                }else{
                    startActivity(new Intent(MainActivity.this , Login.class));
                    finish();
                }
            }else{
                startActivity(new Intent(MainActivity.this , Login.class));
                finish();
            }
        }else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            refresh.setVisibility(View.VISIBLE);
        }
    }

    private void login(final String email) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.child("Users").child(email).getValue(User.class);
                startActivity(new Intent(MainActivity.this , Home.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
