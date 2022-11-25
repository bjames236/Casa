package com.cc17.casa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cc17.casa.Model.House;
import com.cc17.casa.Model.Users;
import com.cc17.casa.ViewHolder.houseViewHolder;
import com.cc17.casa.navigationMenuUI.menuMyProfile;
import com.cc17.casa.navigationMenuUI.menuAddListing;
import com.cc17.casa.navigationMenuUI.menuViewListing;
import com.cc17.casa.navigationMenuUI.menuChangeProfile;
import com.cc17.casa.navigationMenuUI.menuChangePassword;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    private RecyclerView myList;

    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);

        myList = (RecyclerView) findViewById(R.id.recycler_menu);
        myList.setLayoutManager(new LinearLayoutManager(HomeUI.this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome to CASA");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);
        TextView profileName = (TextView) headerView.findViewById(R.id.profileIDname);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                profileName.setText(users.getFullname());
                profileName.setAllCaps(true);
                if(users.getProfileImage().equals("default")){
                    profileImageView.setImageResource(R.drawable.logo);
                }else{
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeUI.this,"Error", Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menuProfile) {
            Intent intent = new Intent(HomeUI.this, menuMyProfile.class);
            startActivity(intent);

        }/*if (id == R.id.menuMessage) {
            *//*Intent intent = new Intent(HomeUI.this, menuMessages.class);
            startActivity(intent);*//*

        }*/if (id == R.id.menuAdd) {
            Intent intent = new Intent(HomeUI.this, menuAddListing.class);
            startActivity(intent);

        }if (id == R.id.menuView) {
            Intent intent = new Intent(HomeUI.this, menuViewListing.class);
            startActivity(intent);

        }if (id == R.id.menuSettings) {
            Intent intent = new Intent(HomeUI.this, menuChangeProfile.class);
            startActivity(intent);

        }if (id == R.id.menuSecurity) {
            Intent intent = new Intent(HomeUI.this, menuChangePassword.class);
            startActivity(intent);

        }else if (id == R.id.menuLogout) {
            Intent intent = new Intent(HomeUI.this, LoginUI.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Auth.signOut();
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");
        FirebaseRecyclerOptions<House> options = new FirebaseRecyclerOptions.Builder<House>()
                .setQuery(databaseReference.orderByChild("status").equalTo("AVAILABLE"), House.class).build();
        FirebaseRecyclerAdapter<House, houseViewHolder> adapter =
                new FirebaseRecyclerAdapter<House, houseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull houseViewHolder houseViewHolder, int i, @NonNull House house) {
                        houseViewHolder.houseAddressLayout.setText(house.getHouseAddress());
                        houseViewHolder.monthRentPriceLayout.setText("₱ " + house.getMonthlyRent());
                        houseViewHolder.houseContactLayout.setText(house.getHouseContactNumber());
                        houseViewHolder.housePostedDateLayout.setText(house.getDate());
                        houseViewHolder.listersNameLayout.setText("By: " + house.getHouseOwner());
                        houseViewHolder.statusLayout.setText(house.getStatus());
                        if (house.getStatus().equals("AVAILABLE")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#008450"));
                        }else if (house.getStatus().equals("RESERVED")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#EFB700"));
                        }else if (house.getStatus().equals("RENTED")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#B81D13"));
                        }
                        Glide.with(getApplicationContext()).load(house.getImage()).into(houseViewHolder.imageView);

                        houseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeUI.this, ViewListingDetails.class);
                                intent.putExtra("lid", house.getLid());
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public houseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.houselistinglayout, parent, false);
                        houseViewHolder holder = new houseViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

}