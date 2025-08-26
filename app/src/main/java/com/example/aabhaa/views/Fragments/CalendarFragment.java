package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.adapters.TaskAdapter;
import com.example.aabhaa.controllers.TaskController;
import com.example.aabhaa.models.Task;
import com.example.aabhaa.views.AddTaskActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private TaskController taskController;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddTask;
    private CalendarView calendarView;
    private TextView tvTaskDate;
    private ChipGroup chipGroupCategories;

    private Calendar selectedDate = Calendar.getInstance();
    private List<Task> allTasks = new ArrayList<>();
    private Map<String, Integer> dateTaskCountMap = new HashMap<>(); // backend date -> task count

    private final String[] categoriesBackend = {"Fertilization","Irrigation","Pesticides","Harvesting","Farming",
            "Maintenance","Weeding","Soil Preparation","Other"};

    private final String[] categoriesNepali = {"मलजनन", "सिंचाई", "किटनाशक", "फसल काट्ने", "खेती",
            "रखरखाव", "गहिराई", "माटो तयारी", "अन्य"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        taskController = new TaskController(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new TaskAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fabAddTask = view.findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> startActivity(new Intent(getContext(), AddTaskActivity.class)));

        calendarView = view.findViewById(R.id.calendar);
        tvTaskDate = view.findViewById(R.id.tvTaskDate);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);

        setupChips();
        updateTaskDateLabel(selectedDate);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate.set(year, month, dayOfMonth);
            filterTasks();
        });

        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> filterTasks());

        setupObservers();

        return view;
    }

    private void setupObservers() {
        taskController.getTasksLiveData().observe(getViewLifecycleOwner(), tasks -> {
            allTasks.clear();
            dateTaskCountMap.clear();

            if (tasks != null) {
                allTasks.addAll(tasks);

                // Build map of backend date -> task count
                for (Task task : allTasks) {
                    if (task.getDate() == null) continue;
                    int count = dateTaskCountMap.getOrDefault(task.getDate(), 0);
                    dateTaskCountMap.put(task.getDate(), count + 1);
                }

                Log.d("CalendarFragment", "Date -> task count map: " + dateTaskCountMap);
            }

            filterTasks();
        });
    }

    private void setupChips() {
        chipGroupCategories.removeAllViews();

        Locale currentLocale = getResources().getConfiguration().locale;
        boolean isNepali = currentLocale.getLanguage().equals("ne");

        // Add "All" chip first
        Chip allChip = new Chip(getContext());
        allChip.setText(isNepali ? "सबै" : "All");
        allChip.setTag("All"); // backend value
        allChip.setCheckable(true);
        allChip.setChecked(true); // default selection
        chipGroupCategories.addView(allChip);

        // Add category chips
        for (int i = 0; i < categoriesBackend.length; i++) {
            Chip chip = new Chip(getContext());
            chip.setText(isNepali ? categoriesNepali[i] : categoriesBackend[i]);
            chip.setTag(categoriesBackend[i]); // always English for backend
            chip.setCheckable(true);
            chipGroupCategories.addView(chip);
        }

        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> filterTasks());
    }


    private void updateTaskDateLabel(Calendar date) {
        // Backend date (always English for comparison)
        SimpleDateFormat sdfBackend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String selectedDateStr = sdfBackend.format(date.getTime());

        int taskCount = dateTaskCountMap.getOrDefault(selectedDateStr, 0);

        // Display date in current locale
        String formattedDisplay = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date.getTime());

        tvTaskDate.setText(getString(R.string.tasks_for_date, formattedDisplay, taskCount));
    }

    private void filterTasks() {
        List<Task> filtered = new ArrayList<>();

        Chip selectedChip = chipGroupCategories.findViewById(chipGroupCategories.getCheckedChipId());
        String selectedChipBackend = selectedChip != null ? (String) selectedChip.getTag() : null;

        // Backend date
        SimpleDateFormat sdfBackend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String selectedDateStr = sdfBackend.format(selectedDate.getTime());

        for (Task task : allTasks) {
            if (task.getDate() == null) continue;
            if (!task.getDate().equals(selectedDateStr)) continue;
            if (selectedChipBackend != null && !selectedChipBackend.equals("All") &&
                    !task.getCategory().equalsIgnoreCase(selectedChipBackend)) continue;

            filtered.add(task);
        }

        adapter.updateTasks(filtered);
        updateTaskDateLabel(selectedDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        taskController.fetchAllTasks();
    }
}
