package com.example.aabhaa.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aabhaa.models.Task;

@Database(entities = {Task.class}, version =3, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "tasks_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
