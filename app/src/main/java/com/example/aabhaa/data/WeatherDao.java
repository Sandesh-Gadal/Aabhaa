//package com.example.aabhaa.data.local;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.example.aabhaa.models.Weather;
//
//import java.util.List;
//
//@Dao
//public interface WeatherDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertWeather(List<Weather> weatherList);
//
//    @Query("SELECT * FROM weathers ORDER BY date DESC")
//    LiveData<List<Weather>> getAllWeather();
//
//    @Query("SELECT * FROM weathers ORDER BY date DESC LIMIT 1")
//    LiveData<Weather> getLatestWeather();
//
//    @Query("DELETE FROM weathers")
//    void clearWeather();
//}
