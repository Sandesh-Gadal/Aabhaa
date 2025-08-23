package com.example.aabhaa.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.aabhaa.models.Crop;

@Database(entities = {Crop.class}, version = 1)
public abstract class CropDatabase extends RoomDatabase {

    private static CropDatabase instance;

    public abstract CropDao cropDao();

    public static synchronized CropDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CropDatabase.class, "crop_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

