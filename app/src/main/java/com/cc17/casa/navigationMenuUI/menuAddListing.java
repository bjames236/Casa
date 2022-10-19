package com.cc17.casa.navigationMenuUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cc17.casa.HomeUI;
import com.cc17.casa.R;

public class menuAddListing extends AppCompatActivity {

    RecyclerView houseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add_listing);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_addListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuAddListing.this, HomeUI.class);
                startActivity(intent);
            }
        });


    }
}