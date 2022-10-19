package com.cc17.casa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginUI extends AppCompatActivity {

    private TextView createAccount, forgotPassword;
    private EditText emailEditText, passwordEditText;
    private Button login;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;

    FirebaseAuth Auth;
    FirebaseUser User ,SellerAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        createAccount = (TextView) findViewById(R.id.createAccountBTN);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUI.this, RegisterUI.class);
                startActivity(intent);
            }
        });

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUI.this, ForgotPasswordUI.class);
                startActivity(intent);
            }
        });

        //Initialize firebase
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        SellerAcc = Auth.getCurrentUser();
        //Progress Dialog
        loadBar = new ProgressDialog(this);


        //EditText
        emailEditText = (EditText) findViewById(R.id.input_username);
        passwordEditText = (EditText) findViewById(R.id.input_password);

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if (!email.matches(emailPattern)) {
            emailEditText.setError("Enter correct E-mail!");
        } else if (pass.isEmpty()) {
            passwordEditText.setError("Enter correct Password!");
        }else{
            loadBar.setTitle("Logging In...");
            loadBar.setMessage("Please wait, we are currently checking your credentials.");
            loadBar.setCanceledOnTouchOutside(false);
            loadBar.show();

            Auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        User = FirebaseAuth.getInstance().getCurrentUser();
                        if(User.isEmailVerified()){
                            loadBar.dismiss();
                            Intent intent = new Intent(LoginUI.this, HomeUI.class);
                            startActivity(intent);
                            finish();
                        }else{
                            User.sendEmailVerification();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginUI.this);
                            builder.setTitle("E-mail Verification")
                                    .setMessage("Check your email / spam to verify your account before Logging in.")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadBar.dismiss();
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                    }else {
                        loadBar.dismiss();
                        Toast.makeText(LoginUI.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}