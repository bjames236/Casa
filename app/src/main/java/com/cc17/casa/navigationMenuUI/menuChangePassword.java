package com.cc17.casa.navigationMenuUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.cc17.casa.HomeUI;
import com.cc17.casa.R;

public class menuChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_change_password);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_ChangePassword);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuChangePassword.this, HomeUI.class);
                startActivity(intent);
            }
        });

    }
}