//package com.example.aabhaa.data;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.example.aabhaa.models.Weather;
//
//@Database(entities = {Weather.class}, version = 1, exportSchema = false)
//public abstract class WeatherDatabase extends RoomDatabase {
//
//    private static volatile WeatherDatabase INSTANCE;
//
//    public abstract com.example.aabhaa.data.local.WeatherDao weatherDao();
//
//    public static WeatherDatabase getInstance(Context context) {
//        if (INSTANCE == null) {
//            synchronized (WeatherDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(
//                                    context.getApplicationContext(),
//                                    WeatherDatabase.class,
//                                    "weather_db"
//                            )
//                            .fallbackToDestructiveMigration()
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}
