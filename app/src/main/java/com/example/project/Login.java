package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    EditText e_mailEditText, passwordEditText;
    Button loginBtn;
    TextView SignUpText;
    CheckBox checkRemember ;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ///Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Paper.init(this);
        checkRemember=findViewById(R.id.login_chekRemember);
        e_mailEditText = findViewById(R.id.Login_userName);
        passwordEditText = findViewById(R.id.Login_password);
        loginBtn = findViewById(R.id.Login_btn_profile);
        SignUpText = findViewById(R.id.Login_sign_up);

        SignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
                //      finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (e_mailEditText.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Login(e_mailEditText.getText().toString().toLowerCase(), passwordEditText.getText().toString().toLowerCase());

            }
        });


    }

    public void Login(final String Email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.child(Email).exists()) {
                    User user = dataSnapshot.child(Email).getValue(User.class);
                    if (user.getPassword().equals(password)) {
                        // Toast.makeText(Login.this, "succesful/ "+User.firstName, Toast.LENGTH_SHORT).show();
                        if (checkRemember.isChecked())
                        {
                            Paper.book().write(Common.USER_KEY,Email);
                            Paper.book().write(Common.PASSWORD_KEY, password);

                        }
                        startActivity(new Intent(Login.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Email doesn't exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
