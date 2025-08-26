package com.example.aabhaa.views;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.config.CloudinaryConfig;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MyApplication extends Application {

    private static LanguageChangeListener languageChangeListener;

    public interface LanguageChangeListener {
        void onLanguageChanged(String newLang);
    }

    public static void setLanguageChangeListener(LanguageChangeListener listener) {
        languageChangeListener = listener;
    }

    public static void notifyLanguageChanged(String newLang) {
        if (languageChangeListener != null) {
            languageChangeListener.onLanguageChanged(newLang);
        }
    }

    public static void switchLanguage(Context context, String lang) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("lang", lang).apply();

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        // Notify listeners
        notifyLanguageChanged(lang);

    }
    @Override
    protected void attachBaseContext(Context base) {
        Context updatedContext = updateLocale(base);
        super.attachBaseContext(updatedContext);
        Log.d("LANG", "attachBaseContext called in " + getClass().getSimpleName() +
                " with locale: " + updatedContext.getResources().getConfiguration().locale);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefManager.init(this);
        CloudinaryConfig.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannel();
    }

    public static Context updateLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");

        Log.d("LANG", "Saved language from prefs: " + lang);

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration(); // create a fresh config
        config.setLocale(locale);

        Context newContext = context.createConfigurationContext(config);

        Log.d("LANG", "Locale set in context: " + newContext.getResources().getConfiguration().locale);

        return newContext;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Reminder";
            String description = "Channel for task reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("task_reminder_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}