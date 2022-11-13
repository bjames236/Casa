package com.cc17.casa.navigationMenuUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cc17.casa.HomeUI;
import com.cc17.casa.Model.House;
import com.cc17.casa.R;
import com.cc17.casa.ViewHolder.houseViewHolder;
import com.cc17.casa.ViewListingDetails;
import com.cc17.casa.ViewYourListingDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class menuViewListing extends AppCompatActivity {

    private ImageButton ArrowBack;
    private TextView soldItems;
    private RecyclerView myList;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private String productID = "";
    private String theuserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view_listing);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuViewListing.this, HomeUI.class);
                startActivity(intent);
            }
        });

        myList = (RecyclerView) findViewById(R.id.myListingsRecyclerView);
        myList.setLayoutManager(new LinearLayoutManager(menuViewListing.this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");

        FirebaseRecyclerOptions<House> options = new FirebaseRecyclerOptions.Builder<House>()
                .setQuery(databaseReference.orderByChild("userId").equalTo(User.getUid()), House.class).build();

        FirebaseRecyclerAdapter<House, houseViewHolder> adapter =
                new FirebaseRecyclerAdapter<House, houseViewHolder>(options) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull houseViewHolder houseViewHolder, int i, @NonNull House house) {
                        houseViewHolder.houseAddressLayout.setText(house.getHouseAddress());
                        houseViewHolder.monthRentPriceLayout.setText("₱ " + house.getMonthlyRent());
                        houseViewHolder.houseContactLayout.setText(house.getHouseContactNumber());
                        houseViewHolder.housePostedDateLayout.setText(house.getDate());
                        houseViewHolder.listersNameLayout.setText(house.getHouseOwner());
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
                                String [] sortOptions = {"View", "Delete", "Mark: Reserved", "Mark: Rented", "Mark: Available"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(menuViewListing.this);
                                builder.setTitle(house.getHouseAddress());
                                builder.setIcon(R.drawable.logo);
                                builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0){
                                            dialog.dismiss();
                                            Intent intent = new Intent(menuViewListing.this, ViewYourListingDetails.class);
                                            intent.putExtra("userId", house.getUserId());
                                            intent.putExtra("hid", house.getLid());
                                            startActivity(intent);
                                        }else if (which==1){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(menuViewListing.this);
                                            builder.setTitle("")
                                                    .setMessage("Are you sure you want to delete " + house.getHouseAddress() + " ?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings").child(house.getLid());
                                                            databaseReference.removeValue();
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }else if (which==2){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(menuViewListing.this);
                                            builder.setTitle("")
                                                    .setMessage("Do you want to mark this listing as RESERVED?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            HashMap houseMap = new HashMap();
                                                            houseMap.put("status", "RESERVED");
                                                            Auth = FirebaseAuth.getInstance();
                                                            User = Auth.getCurrentUser();

                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings");
                                                            databaseReference.child(house.getLid()).updateChildren(houseMap);

                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }else if (which==3){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(menuViewListing.this);
                                            builder.setTitle("")
                                                    .setMessage("Do you want to mark this listing as RENTED?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            HashMap houseMap = new HashMap();
                                                            houseMap.put("status", "RENTED");
                                                            Auth = FirebaseAuth.getInstance();
                                                            User = Auth.getCurrentUser();

                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings");
                                                            databaseReference.child(house.getLid()).updateChildren(houseMap);

                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }else if (which==4){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(menuViewListing.this);
                                            builder.setTitle("")
                                                    .setMessage("Do you want to mark this listing as AVAILABLE again?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Calendar calendar = Calendar.getInstance();

                                                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                            String saveCurrentDate = currentDate.format(calendar.getTime());

                                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                                            String saveCurrentTime = currentTime.format(calendar.getTime());

                                                            HashMap houseMap = new HashMap();
                                                            houseMap.put("status", "AVAILABLE");
                                                            houseMap.put("date", saveCurrentDate);
                                                            houseMap.put("time", saveCurrentTime);
                                                            Auth = FirebaseAuth.getInstance();
                                                            User = Auth.getCurrentUser();

                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings");
                                                            databaseReference.child(house.getLid()).updateChildren(houseMap);

                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }
                                    }
                                });
                                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
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
        myList.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myList.setLayoutManager(linearLayoutManager);

    }

}