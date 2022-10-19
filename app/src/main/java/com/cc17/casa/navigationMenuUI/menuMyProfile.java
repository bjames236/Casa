package com.cc17.casa.navigationMenuUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.cc17.casa.HomeUI;
import com.cc17.casa.R;

public class menuMyProfile extends AppCompatActivity {

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


    }
}