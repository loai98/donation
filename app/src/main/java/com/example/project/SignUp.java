package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    DatabaseReference databaseReference;


    EditText firstNameEditText, lastNameEditText,
            password1EditText, password2EditText,
            E_mailEditText, phoneNumberEditText,
            birthYearEditText;

    Button signUpButton;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ////fierbase
        databaseReference = FirebaseDatabase.getInstance().getReference();


        firstNameEditText = findViewById(R.id.sign_up_first_name);
        lastNameEditText = findViewById(R.id.sign_up_Last_name);
        password1EditText = findViewById(R.id.sign_up_Password1);
        password2EditText = findViewById(R.id.sign_up_Password2);
        E_mailEditText = findViewById(R.id.sign_up_EmailAddress);
        phoneNumberEditText = findViewById(R.id.sign_up_phone);
        birthYearEditText = findViewById(R.id.sign_up_editTextDate);
        signUpButton = findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lastNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (E_mailEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter E_mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password1EditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password2EditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password2EditText.getText().toString().equals(password1EditText.getText().toString())) {
                    Toast.makeText(SignUp.this, "Passwords Don't Match ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneNumberEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (birthYearEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter Birth Year", Toast.LENGTH_SHORT).show();
                    return;
                }

                user = new User(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        E_mailEditText.getText().toString().toLowerCase(),
                        phoneNumberEditText.getText().toString(),
                        birthYearEditText.getText().toString(),
                        password2EditText.getText().toString().toLowerCase(),
                        "noImage"
                );


                SignUp();


            }
        });


    }

       void SignUp() {
        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getE_mail()).exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "E_mail already exists", Toast.LENGTH_SHORT).show();
                    // return ;
                } else {

                    databaseReference.child("Users").child(user.getE_mail()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "user added Successfuly", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(SignUp.this , Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}

