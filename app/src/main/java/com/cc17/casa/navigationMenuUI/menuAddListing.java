package com.cc17.casa.navigationMenuUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cc17.casa.HomeUI;
import com.cc17.casa.Model.Users;
import com.cc17.casa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class menuAddListing extends AppCompatActivity {

    private ImageView addHousePicture;
    private FloatingActionButton addBTN;
    private TextView addImage;
    private EditText monthlyRentTXT, houseAddressTXT, houseDescriptionTXT, listersNameTXT, listersPhoneNumberTXT;
    private String monthlyRent, houseAddress, houseDescription, listersName, listersPhoneNumber, saveCurrentDate, saveCurrentTime;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String RandomKey, downloadImageUrl;
    private StorageReference houseImagesRef;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingBar;

    private FirebaseUser User;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add_listing);

        loadingBar = new ProgressDialog(this);
        houseImagesRef = FirebaseStorage.getInstance().getReference().child("House Images");

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_addListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuAddListing.this, HomeUI.class);
                startActivity(intent);
            }
        });

        addBTN = (FloatingActionButton) findViewById(R.id.addBoarding);
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        addHousePicture = (ImageView) findViewById(R.id.houseImages);
        addImage = (TextView) findViewById(R.id.AddListingImageBTN);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        monthlyRentTXT = (EditText) findViewById(R.id.AddRentEditText);
        houseAddressTXT = (EditText) findViewById(R.id.AddAddressEditText);
        houseDescriptionTXT = (EditText) findViewById(R.id.AddDescriptionEditText);
        listersNameTXT = (EditText) findViewById(R.id.AddNameInfoEditText);
        listersPhoneNumberTXT = (EditText) findViewById(R.id.AddPhoneInfoEditText);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                listersNameTXT.setText(users.getFullname());
                listersPhoneNumberTXT.setText(users.getPhoneNumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(menuAddListing.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void add() {
        monthlyRent = monthlyRentTXT.getText().toString();
        houseAddress = houseAddressTXT.getText().toString();
        houseDescription = houseDescriptionTXT.getText().toString();
        listersName = listersNameTXT.getText().toString();
        listersPhoneNumber = listersPhoneNumberTXT.getText().toString();
        if (ImageUri == null) {
            Toast.makeText(this, "Product image is needed...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(monthlyRent)) {
            monthlyRentTXT.setError("Required!");
        }else if (TextUtils.isEmpty(houseAddress)) {
            houseAddressTXT.setError("Required!");
        } else if (TextUtils.isEmpty(houseDescription)) {
            houseDescriptionTXT.setError("Required!");
        }else {
            storeToDatabase();
        }
    }

    private void storeToDatabase() {
        loadingBar.setTitle("Adding Please Wait");
        loadingBar.setMessage("Loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        RandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = houseImagesRef.child(ImageUri.getLastPathSegment() + RandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(menuAddListing.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            SaveInfoToDatabase();
                        }

                    }
                });
            }
        });

    }

    private void SaveInfoToDatabase() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "AVAILABLE");
        hashMap.put("lid", RandomKey);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("time", saveCurrentTime);
        hashMap.put("image", downloadImageUrl);
        hashMap.put("monthlyRent", monthlyRent);
        hashMap.put("houseAddress", houseAddress);
        hashMap.put("houseDetails", houseDescription);
        hashMap.put("houseLocation", houseAddress);
        hashMap.put("houseContactNumber", listersPhoneNumber);
        hashMap.put("houseOwner", listersName);
        hashMap.put("userId", User.getUid());
        hashMap.put("search", houseAddress.toLowerCase());

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");
        databaseReference.child(RandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(menuAddListing.this, menuViewListing.class);
                    startActivity(intent);
                    finish();
                    loadingBar.dismiss();
                    Toast.makeText(menuAddListing.this, "Listing has been added!", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(menuAddListing.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            addHousePicture.setImageURI(ImageUri);
        }
    }
}