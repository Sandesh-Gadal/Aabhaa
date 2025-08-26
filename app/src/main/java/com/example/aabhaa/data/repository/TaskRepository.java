package com.example.aabhaa.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aabhaa.data.TaskDao;
import com.example.aabhaa.data.TaskDatabase;
import com.example.aabhaa.models.Task;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.TaskApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private TaskApiService taskService;
    private TaskDao taskDao;
    private MutableLiveData<List<Task>> tasksLiveData;
    private MutableLiveData<Boolean> loadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private Context context;

    public TaskRepository(Context context) {
        this.context = context;
        taskService = RetrofitClient.getClient(context).create(TaskApiService.class);
        taskDao = TaskDatabase.getInstance(context).taskDao();
        tasksLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Task>> getTasksLiveData() {
        return tasksLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /** Fetch all tasks from backend and store locally */
    public void fetchAllTasks() {
        Log.d("TaskRepository", "Starting fetchAllTasks");

        loadingLiveData.postValue(true);

        taskService.getTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                Log.d("TaskRepository", "API Response received");

                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();
                    Log.d("TaskRepository", "API returned " + tasks.size() + " tasks");

                    // Update LiveData immediately
                    tasksLiveData.setValue(tasks);

                    // Save to Room database in background
                    new Thread(() -> {
                        try {
                            taskDao.deleteAll();
                            taskDao.insertAll(tasks);
                            Log.d("TaskRepository", "Tasks saved to local database");

                            // Post the updated data again to ensure UI consistency
                            tasksLiveData.postValue(tasks);
                        } catch (Exception e) {
                            Log.e("TaskRepository", "Error saving to local database", e);
                        }
                    }).start();
                } else {
                    Log.e("TaskRepository", "API call unsuccessful: " + response.code());
                    errorLiveData.setValue("Failed to load tasks from server");
                    loadTasksFromLocal();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskRepository", "API call failed", t);
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
                loadTasksFromLocal();
            }
        });
    }

    private void loadTasksFromLocal() {
        Log.d("TaskRepository", "Loading tasks from local database");
        new Thread(() -> {
            try {
                List<Task> localTasks = taskDao.getAllTasks();
                Log.d("TaskRepository", "Loaded " + localTasks.size() + " tasks from local database");
                tasksLiveData.postValue(localTasks);
            } catch (Exception e) {
                Log.e("TaskRepository", "Error loading from local database", e);
                tasksLiveData.postValue(null);
            }
        }).start();
    }

    /** Create a new task */
    public void createTask(Task task, MutableLiveData<Boolean> result) {
        loadingLiveData.setValue(true);

        taskService.createTask(task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        taskDao.insert(response.body());
                        // Refresh the tasks list
                        fetchAllTasks();
                    }).start();
                    result.setValue(true);
                } else {
                    errorLiveData.setValue("Failed to create task");
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    /** Update a task */
    public void updateTask(Task task, MutableLiveData<Boolean> result) {
        loadingLiveData.setValue(true);

        taskService.updateTask(task.getId(), task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        taskDao.update(response.body());
                        // Refresh the tasks list
                        fetchAllTasks();
                    }).start();
                    result.setValue(true);
                } else {
                    errorLiveData.setValue("Failed to update task");
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    /** Delete a task */
    public void deleteTask(Task task, MutableLiveData<Boolean> result) {
        loadingLiveData.setValue(true);

        taskService.deleteTask(task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        taskDao.delete(task);
                        // Refresh the tasks list
                        fetchAllTasks();
                    }).start();
                    result.setValue(true);
                } else {
                    errorLiveData.setValue("Failed to delete task");
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    /** Toggle task completed */
    public void toggleTaskComplete(Task task, MutableLiveData<Boolean> result) {
        loadingLiveData.setValue(true);

        taskService.toggleComplete(task.getId()).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        taskDao.update(response.body());
                        // Refresh the tasks list
                        fetchAllTasks();
                    }).start();
                    result.setValue(true);
                } else {
                    errorLiveData.setValue("Failed to toggle complete");
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    /** Get tasks by date (offline) */
    public List<Task> getTasksByDate(String date) {
        return taskDao.getTasksByDate(date);
    }

    /** Get pending tasks for logged-in user (offline) */
    public List<Task> getPendingTasks(int userId) {
        return taskDao.getPendingTasks(userId);
    }
}