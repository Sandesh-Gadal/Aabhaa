package com.example.aabhaa.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.aabhaa.R;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.models.Task;
import com.example.aabhaa.data.repository.TaskRepository;
import com.example.aabhaa.workers.TaskReminderWorker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText etTaskTitle, etDescription, etDate, etTime;
    private AutoCompleteTextView actvCategory, dropdownRepeat;
    private MaterialButton btnSave;
    private TextView tvHeaderText;

    private TaskRepository taskRepository;
    private Calendar selectedDateTime;

    // Editing flag
    private Task editingTask = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);



        // Initialize
        etTaskTitle = findViewById(R.id.etTasktitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        actvCategory = findViewById(R.id.actvCategory);
        dropdownRepeat = findViewById(R.id.actvRepeat);
        btnSave = findViewById(R.id.btnRequestHelp);
        tvHeaderText = findViewById(R.id.headerTitle);
        selectedDateTime = Calendar.getInstance();
        taskRepository = new TaskRepository(getApplicationContext());

        // Cancel button
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        setupCategoryDropdown();
        setupDatePicker();
        setupTimePicker();

        // Check if editing
        if (getIntent().hasExtra("task_to_edit")) {
            editingTask = (Task) getIntent().getSerializableExtra("task_to_edit");
            populateFields(editingTask);
            tvHeaderText.setText("Update Task");
            btnSave.setText("Update Task");
        } else {
            tvHeaderText.setText("Add Task");
            btnSave.setText("Add Task");
        }

        btnSave.setOnClickListener(v -> saveTask());
    }


    private void setupCategoryDropdown() {
        String[] categories = {"Fertilization","Irrigation","Pesticides","Harvesting","Farming",
                "Maintenance","Weeding","Soil Preparation","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(adapter);

        String[] repeatOptions = {"None", "Daily", "Weekly", "Monthly"};
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, repeatOptions);
        dropdownRepeat.setAdapter(repeatAdapter);
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            int year = selectedDateTime.get(Calendar.YEAR);
            int month = selectedDateTime.get(Calendar.MONTH);
            int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDateTime.set(y, m, d);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                etDate.setText(sdf.format(selectedDateTime.getTime()));
            }, year, month, day).show();
        });
    }

    private void setupTimePicker() {
        etTime.setOnClickListener(v -> {
            int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
            int minute = selectedDateTime.get(Calendar.MINUTE);

            new TimePickerDialog(this, (view, h, m) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, h);
                selectedDateTime.set(Calendar.MINUTE, m);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                etTime.setText(sdf.format(selectedDateTime.getTime()));
            }, hour, minute, true).show();
        });
    }

    private void populateFields(Task task) {
        etTaskTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());
        actvCategory.setText(task.getCategory(), false);
        etDate.setText(task.getDate());
        etTime.setText(task.getTime());
        dropdownRepeat.setText(task.getRepeatInterval(), false);
    }

    private void saveTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String repeat = dropdownRepeat.getText().toString().trim();

        if(title.isEmpty() || category.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(editingTask != null) {
            editingTask.setTitle(title);
            editingTask.setDescription(description);
            editingTask.setCategory(category);
            editingTask.setDate(date);
            editingTask.setTime(time);
            editingTask.setRepeatInterval(repeat);

            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if(Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                    scheduleReminder(editingTask); // reschedule reminder
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
                }
            });
            taskRepository.updateTask(editingTask, result);

        } else {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setCategory(category);
            task.setDate(date);
            task.setTime(time);
            task.setRepeatInterval(repeat);
            task.setUserId(SharedPrefManager.getInstance().getUserId());

            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if(Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    scheduleReminder(task);
                    WorkManager.getInstance(this).getWorkInfosByTagLiveData("task_reminder")
                            .observe(this, workInfos -> {
                                if (workInfos != null && !workInfos.isEmpty()) {
                                    Log.d("WorkStatus", "Work state: " + workInfos.get(0).getState());
                                    Toast.makeText(this, "Work state: " + workInfos.get(0).getState(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(this, "Reminder Scheduled: " + workInfos.get(0).getState(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    finish();
                } else {
                    Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            });
            taskRepository.createTask(task, result);
        }
    }

    private void scheduleReminder(@NonNull Task task) {
        String date = task.getDate();
        String time = task.getTime();

        if (date == null || date.isEmpty() || time == null || time.isEmpty()) {
            Log.e("AddTaskActivity", "scheduleReminder: date or time is null/empty → skipping reminder");
            return;
        }

        // Calculate initial delay
        Calendar taskTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            taskTime.setTime(sdf.parse(date + " " + time));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        long delay = taskTime.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) delay = 0;

        // Determine interval
        long intervalMinutes;
        switch (task.getRepeatInterval() != null ? task.getRepeatInterval() : "") {
            case "Daily": intervalMinutes = 24 * 60; break;
            case "Weekly": intervalMinutes = 7 * 24 * 60; break;
            case "Monthly": intervalMinutes = 30 * 24 * 60; break;
            default: intervalMinutes = 0; break;
        }

        Data data = new Data.Builder()
                .putString("task_title", task.getTitle() != null ? task.getTitle() : "")
                .putString("task_description", task.getDescription() != null ? task.getDescription() : "")
                .build();

        if (intervalMinutes > 0) {
            // ✅ Repeating task → PeriodicWorkRequest
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                    TaskReminderWorker.class,
                    intervalMinutes, TimeUnit.MINUTES
            )
                    .setInputData(data)
                    .addTag("task_reminder")
                    .build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "task_" + task.getId(),
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
            );
        } else {
            // ✅ One-time task → OneTimeWorkRequest
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(TaskReminderWorker.class)
                    .setInputData(data)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .addTag("task_reminder")
                    .build();

            WorkManager.getInstance(this).enqueue(workRequest);
        }

        Log.d("AddTaskActivity", "Reminder scheduled for task: " + task.getTitle());
    }


}
