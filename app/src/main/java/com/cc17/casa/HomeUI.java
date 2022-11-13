package com.cc17.casa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cc17.casa.Model.Users;
import com.cc17.casa.navigationMenuUI.menuMyProfile;
import com.cc17.casa.navigationMenuUI.menuMessages;
import com.cc17.casa.navigationMenuUI.menuAddListing;
import com.cc17.casa.navigationMenuUI.menuViewListing;
import com.cc17.casa.navigationMenuUI.menuChangeProfile;
import com.cc17.casa.navigationMenuUI.menuChangePassword;
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

public class HomeUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);

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

        }if (id == R.id.menuMessage) {
            Intent intent = new Intent(HomeUI.this, menuMessages.class);
            startActivity(intent);

        }if (id == R.id.menuAdd) {
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
            Intent intent = new Intent(HomeUI.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Auth.signOut();
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}