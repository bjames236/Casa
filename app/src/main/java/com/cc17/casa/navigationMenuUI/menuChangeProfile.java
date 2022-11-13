package com.cc17.casa.navigationMenuUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cc17.casa.HomeUI;
import com.cc17.casa.Model.Users;
import com.cc17.casa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class menuChangeProfile extends AppCompatActivity {

    FirebaseAuth Auth;
    FirebaseUser User;
    private ImageButton settingsArrowBack;
    private CircleImageView profileImage;
    private EditText address, phoneNumber, fullname;
    private Button updateBTN;
    private Button changeProfilePic;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_change_profile);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_changProfile);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuChangeProfile.this, HomeUI.class);
                startActivity(intent);
            }
        });

        profileImage = (CircleImageView) findViewById(R.id.profileImageEdit);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1, 1).start(menuChangeProfile.this);
            }
        });

        updateBTN = (Button) findViewById(R.id.profileEditSaveBTN);
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checker.equals("clicked")) {
                    updateWithProfilePic();
                } else {
                    updateWithNoProfilePic();
                }
            }
        });

        fullname = (EditText) findViewById(R.id.profileNameEdit);
        address = (EditText) findViewById(R.id.profileAddressEdit);
        phoneNumber = (EditText) findViewById(R.id.profilePhoneNumberEdit);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;

                fullname.setText(users.getFullname());
                phoneNumber.setText(users.getPhoneNumber());
                address.setText(users.getHouseAddress());

                if (users.getProfileImage().equals("default")) {
                    profileImage.setImageResource(R.drawable.logo);
                } else {
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(menuChangeProfile.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateWithProfilePic() {
        String name = fullname.getText().toString();
        String add = address.getText().toString();
        String phone = phoneNumber.getText().toString();


        if (TextUtils.isEmpty(name)) {
            fullname.setError("Enter your name");
            Toast.makeText(menuChangeProfile.this, "Please write your middle name / initial...", Toast.LENGTH_SHORT).show();
        }else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(menuChangeProfile.this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your address");
            Toast.makeText(menuChangeProfile.this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }else {
            updateDataPic(name, add, phone);
        }

    }

    private void updateDataPic(String name, String add, String phone) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(User.getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap Users = new HashMap();
                        Users.put("fullname", name);
                        Users.put("houseAddress", add);
                        Users.put("phoneNumber", phone);
                        Users.put("profileImage", myUrl);
                        ref.child(User.getUid()).updateChildren(Users);

                        progressDialog.dismiss();

                        startActivity(new Intent(menuChangeProfile.this, menuMyProfile.class));
                        Toast.makeText(menuChangeProfile.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(menuChangeProfile.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateWithNoProfilePic() {
        String name = fullname.getText().toString();
        String add = address.getText().toString();
        String phone = phoneNumber.getText().toString();


        if (TextUtils.isEmpty(name)) {
            fullname.setError("Enter your name");
            Toast.makeText(menuChangeProfile.this, "Please write your middle name / initial...", Toast.LENGTH_SHORT).show();
        }else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(menuChangeProfile.this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your address");
            Toast.makeText(menuChangeProfile.this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }else {
            updateData(name, add, phone);
        }
    }

    private void updateData(String name, String add, String phone) {
        HashMap Users = new HashMap();
        Users.put("fullname", name);
        Users.put("houseAddress", add);
        Users.put("phoneNumber", phone);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(User.getUid()).updateChildren(Users).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(menuChangeProfile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(menuChangeProfile.this, menuMyProfile.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(menuChangeProfile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Please Select Image.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(menuChangeProfile.this, menuChangeProfile.class));
            finish();
        }
    }

}