package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Donate extends AppCompatActivity {

    public Adress adress;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Button addImageBtn, selectLocationBtn,
            placeBtn;
    Donation donation;

    EditText titleEditText, descriptionEditText;
    Uri imageuri;
    ImageView imageViewBtn;
    boolean isLocationSelected;
    int IMAGE_REQUEST_CODE = 101, LOCATION_REQUEST_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        isLocationSelected = false;
        adress = new Adress();
        donation = new Donation();

        titleEditText = findViewById(R.id.donate_title);
        descriptionEditText = findViewById(R.id.donate_description);
        placeBtn = findViewById(R.id.donate_place);
        imageViewBtn = findViewById(R.id.donate_imageView);
        addImageBtn = findViewById(R.id.donate_addImage);
        selectLocationBtn = findViewById(R.id.donate_selectLocation);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Donate.this, SelectLocation.class);
                startActivityForResult(intent, LOCATION_REQUEST_CODE);

            }
        });


        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!titleEditText.getText().toString().isEmpty() &&
                        !descriptionEditText.getText().toString().isEmpty()
                         && isLocationSelected)
                {
                    donation.setTitle(titleEditText.getText().toString());
                    donation.setDescription(descriptionEditText.getText().toString());
                    if (imageuri!=null)
                       uploadImage();
                    else
                    {
                        donation.setImage("noImage");
                        placeDoantion();
                    }
                }
                else
                    Toast.makeText(Donate.this, "Please Fill Full Information", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void uploadImage() {

        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.show();

        String imageName = UUID.randomUUID().toString();
        final StorageReference imageFolder = storageReference.child("images/" + imageName);
        imageFolder.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDialog.dismiss();
                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                donation.setImage(uri.toString());
                                placeDoantion();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();
                Toast.makeText(Donate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                  /*  double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploading...  " + (int) progress + "%");*/
                mDialog.setMessage("Please wait...");
            }
        });


    }


    private void placeDoantion() {

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance().format(calendar.getTime());
        donation.setDate(date);


        donation.setDonator(new User(User.firstName , User.lastName ,User.e_mail ,User.phonenumber,User.birthYear,User.image));



        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please Wait");
        mDialog.show();
        databaseReference.child("Donations").child(User.e_mail).push().setValue(donation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful())
                {
                    Toast.makeText(Donate.this, "Donation Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(Donate.this , MyDonation.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Donate.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && data.getData() != null) {
            imageuri = data.getData();
            imageViewBtn.setImageURI(imageuri);

        }

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {

            isLocationSelected = true;
            adress.setLatitude(data.getStringExtra("latitude"));
            adress.setLongitude(data.getStringExtra("longitude"));
            selectLocationBtn.setText("Location Selected");
            donation.setAdress(adress);


            //  selectLocationBtn.setBackgroundColor(R.color.locationBtnColor);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);
    }


}
