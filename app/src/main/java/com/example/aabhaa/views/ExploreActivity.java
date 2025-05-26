package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;

public class ExploreActivity extends AppCompatActivity {

    private Button btnSignIn;
    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);  // Replace with your actual layout file name

        btnSignIn = findViewById(R.id.btnSignIn);
        createAccount = findViewById(R.id.createAccount);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginActivity
                Intent intent = new Intent(ExploreActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity
                Intent intent = new Intent(ExploreActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}
