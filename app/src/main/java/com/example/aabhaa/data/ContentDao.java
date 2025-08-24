package com.example.aabhaa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.aabhaa.models.Content;

import java.util.List;

@Dao
public interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Content> contents);

    @Query("SELECT * FROM contents")
    List<Content> getAllContents();

    @Query("DELETE FROM contents")
    void deleteAll();
}
