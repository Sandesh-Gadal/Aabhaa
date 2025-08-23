package com.example.aabhaa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.aabhaa.models.Crop;
import java.util.List;

@Dao
public interface CropDao {
    @Insert
    void insertAll(List<Crop> crops);

    @Query("SELECT * FROM crops")
    List<Crop> getAllCrops();

    @Query("DELETE FROM crops")
    void deleteAll();
}
