package com.example.aabhaa.views;

import android.content.Context;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;

public class SendIssueActivity  extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendissue);
    }
}
