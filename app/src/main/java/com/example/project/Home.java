package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity {

    ImageView profileImageDisplay;
    Button donateBtn;
    Button addImage, removeImage, viewImage;
    Uri imageUri;
    StorageReference storageReference;
    TextView fullNmae, phoneNumber;

    DatabaseReference databaseReference;
    CircleImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        profileImage = findViewById(R.id.profile_image);
        fullNmae = findViewById(R.id.profile_name);
        phoneNumber = findViewById(R.id.profile_phone);
        donateBtn = findViewById(R.id.profile_To_donate);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowImageOptions();

            }


        });


        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Donate.class);
                startActivity(intent);
            }
        });


        SetUserInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.myDonations)
        {
            //Toast.makeText(this, "code late", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Home.this , MyDonation.class));

        }



        else if (item.getItemId() == R.id.editProfile)
            Toast.makeText(this, "late", Toast.LENGTH_SHORT).show();
        else if (item.getItemId() == R.id.logOut) {
            // Toast.makeText(this, "log out", Toast.LENGTH_SHORT).show();
            Intent logInIntent = new Intent(Home.this, Login.class);
            Paper.book().destroy();
            logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logInIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetUserInformation() {
        fullNmae.setText(User.firstName + " " + User.lastName);
        phoneNumber.setText(User.phonenumber);

        if (!User.image.equals("noImage")) {
            Picasso.get().load(User.image).into(profileImage);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadImage();


        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

    }


    private void uploadImage() {
        if (imageUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    databaseReference.child(User.e_mail).child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                User.image = uri.toString();
                                                Toast.makeText(Home.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(Home.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(Home.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploading...  " + (int) progress + "%");
                }
            });
        }

    }

    public void showImage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View displayImage = inflater.inflate(R.layout.display_image, null);

        profileImageDisplay = displayImage.findViewById(R.id.profileImage);
        Picasso.get().load(User.image).into(profileImageDisplay);
        alertDialog.setView(displayImage);
        alertDialog.show();
    }

    private void ShowImageOptions() {


        final AlertDialog.Builder alertDialod = new AlertDialog.Builder(Home.this);
        alertDialod.setTitle("Choose Option :");
        LayoutInflater inflater = Home.this.getLayoutInflater();
        View optionsLayout = inflater.inflate(R.layout.image_options, null);

        addImage = optionsLayout.findViewById(R.id.image_options_addImage);
        removeImage = optionsLayout.findViewById(R.id.image_options_removeImage);
        viewImage = optionsLayout.findViewById(R.id.image_options_showImage);

        if (User.image.equals("noImage")) {
            viewImage.setEnabled(false);
            removeImage.setEnabled(false);
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(User.e_mail).child("image").setValue("noImage").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Home.this, "Image Removed", Toast.LENGTH_SHORT).show();
                        User.image = "noImage";
                        profileImage.setImageResource(R.drawable.profile);
                    }
                });


            }
        });

        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        alertDialod.setView(optionsLayout);
        alertDialod.show();

    }
}
