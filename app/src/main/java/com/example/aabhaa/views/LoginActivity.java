package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;

public class LoginActivity extends AppCompatActivity {


     private TextView signUpLink , forgotPassword;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       signUpLink = findViewById(R.id.signUpLink);
       signUpLink.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
               startActivity(intent);
           }
       });

        forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , ForgotpasswordActivity.class);
                startActivity(intent);
            }
        });

    }






}
