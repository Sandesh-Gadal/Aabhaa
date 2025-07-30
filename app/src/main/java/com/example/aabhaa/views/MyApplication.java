package com.example.aabhaa.views;

import android.app.Application;

import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.config.CloudinaryConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefManager.init(this);  // initialize only once here
        CloudinaryConfig.init(this);
    }
}
