package com.cc17.casa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterUI extends AppCompatActivity {

    private TextView loginInstead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);

        loginInstead = (TextView) findViewById(R.id.rgt_logininstead);
        loginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterUI.this, LoginUI.class);
                startActivity(intent);
            }
        });

    }
}