package com.example.aabhaa.controllers;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aabhaa.data.repository.TaskRepository;
import com.example.aabhaa.models.Task;

import java.util.List;

public class TaskController {

    private TaskRepository repository;

    public TaskController(Context context) {
        repository = new TaskRepository(context);
    }

    /** LiveData for all tasks from repository (API + Room) */
    public LiveData<List<Task>> getTasksLiveData() {
        return repository.getTasksLiveData();
    }

    /** LiveData for loading state */
    public LiveData<Boolean> getLoadingLiveData() {
        return repository.getLoadingLiveData();
    }

    /** LiveData for error messages */
    public LiveData<String> getErrorLiveData() {
        return repository.getErrorLiveData();
    }

    /** Fetch all tasks from backend and update local DB */
    public void fetchAllTasks() {
        Log.d("TaskController", "Fetching all tasks from backend");
        repository.fetchAllTasks();
    }

    /** Create a new task */
    public void createTask(Task task, MutableLiveData<Boolean> result) {
        repository.createTask(task, result);
    }

    /** Update a task */
    public void updateTask(Task task, MutableLiveData<Boolean> result) {
        repository.updateTask(task, result);
    }

    /** Delete a task */
    public void deleteTask(Task task, MutableLiveData<Boolean> result) {
        repository.deleteTask(task, result);
    }

    /** Toggle completed status */
    public void toggleTaskComplete(Task task, MutableLiveData<Boolean> result) {
        repository.toggleTaskComplete(task, result);
    }

    /** Refresh tasks after operations */
    public void refreshTasks() {
        Log.d("TaskController", "Refreshing tasks");
        fetchAllTasks();
    }
}