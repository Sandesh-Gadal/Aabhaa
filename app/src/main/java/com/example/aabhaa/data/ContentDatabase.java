package com.example.aabhaa.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aabhaa.models.Content;

@Database(entities = {Content.class}, version = 1, exportSchema = false)
public abstract class ContentDatabase extends RoomDatabase {

    private static ContentDatabase instance;

    public abstract ContentDao contentDao();

    public static synchronized ContentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ContentDatabase.class, "content_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
