package com.cc17.casa.navigationMenuUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cc17.casa.HomeUI;
import com.cc17.casa.Model.Users;
import com.cc17.casa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class menuMyProfile extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private TextView name, phone, address, email;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_my_profile);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_profile);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuMyProfile.this, HomeUI.class);
                startActivity(intent);
            }
        });

        name =  (TextView) findViewById(R.id.profileName);
        phone =  (TextView) findViewById(R.id.profilePhoneNumber);
        address =  (TextView) findViewById(R.id.profileAddress);
        email =  (TextView) findViewById(R.id.profileEmailAddress);

        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                name.setText(users.getFullname());
                phone.setText(users.getPhoneNumber());
                address.setText(users.getHouseAddress());
                address.setAllCaps(true);
                email.setText(User.getEmail());

                if(users.getProfileImage().equals("default")){
                    profileImage.setImageResource(R.drawable.logo);
                }else{
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(menuMyProfile.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}