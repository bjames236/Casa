package com.cc17.casa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ForgotPasswordUI extends AppCompatActivity {

    private ImageButton arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_ui);

        //arrow back
        arrowBack = (ImageButton) findViewById(R.id.arrowback_forgot);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForgotPasswordUI.this, LoginUI.class);
                startActivity(intent);
            }
        });
    }
}