package com.example.aabhaa.services;

import com.example.aabhaa.models.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface TaskApiService {

    // Get all tasks
    @GET("tasks")
    Call<List<Task>> getTasks();

    // Get a single task
    @GET("tasks/{id}")
    Call<Task> getTask(@Path("id") int id);

    // Create a task
    @POST("tasks")
    Call<Task> createTask(@Body Task task);

    // Update a task
    @PUT("tasks/{id}")
    Call<Task> updateTask(@Path("id") int id, @Body Task task);

    // Delete a task
    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);

    // Toggle completed
    @PATCH("tasks/{id}/toggle-complete")
    Call<Task> toggleComplete(@Path("id") int id);
}
