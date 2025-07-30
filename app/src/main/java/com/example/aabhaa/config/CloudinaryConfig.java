package com.example.aabhaa.config;

import android.content.Context;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.example.aabhaa.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

    public static void init(Context context) {
        Map<String, String> config = new HashMap<>();


        config.put("cloud_name", "dxslhrq1t");
        config.put("api_key", "619979357538945");
        config.put("api_secret","-4O-178_Xx9tMMf7LGEQRLi93wI" );
        Log.d("CloudinaryConfig", "cloud:inside the config p[ackage " + BuildConfig.CLOUD_NAME + ", key: " + BuildConfig.API_KEY);
        MediaManager.init(context, config);
    }


}
