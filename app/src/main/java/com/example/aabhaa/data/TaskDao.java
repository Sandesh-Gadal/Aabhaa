package com.example.aabhaa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.aabhaa.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(Task task);

    @Insert
    void insertAll(List<Task> tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Query("SELECT * FROM tasks ORDER BY date, time")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task getTaskById(int id);

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY time")
    List<Task> getTasksByDate(String date);

    @Query("SELECT * FROM tasks WHERE userId = :userId AND completed = 0 ORDER BY date ASC, time ASC")
    List<Task> getPendingTasks(int userId);
}
