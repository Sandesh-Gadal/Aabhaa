package com.example.aabhaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.TaskController;
import com.example.aabhaa.models.Task;
import com.example.aabhaa.views.AddTaskActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;
    private TaskController taskController;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.taskController = new TaskController(context);
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // Bind basic task info
        holder.taskTitle.setText(task.getTitle() != null ? task.getTitle() : "No Title");
        holder.taskTime.setText(task.getTime() != null ? task.getTime() : "No Time");
        holder.taskDescription.setText(task.getDescription() != null ? task.getDescription() : "No Description");
        holder.taskCategory.setText(task.getCategory() != null ? task.getCategory() : "General");

        // Format and display date
        if (task.getDate() != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = inputFormat.parse(task.getDate());
                holder.taskDate.setText(outputFormat.format(date));
            } catch (Exception e) {
                holder.taskDate.setText(task.getDate());
            }
        } else {
            holder.taskDate.setText("No Date");
        }

        // Set checkbox state and listener
        holder.checkBoxComplete.setOnCheckedChangeListener(null); // Clear previous listener
        holder.checkBoxComplete.setChecked(task.isCompleted());

        holder.checkBoxComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Show loading state
            buttonView.setEnabled(false);

            // Update task object immediately for UI responsiveness
            task.setCompleted(isChecked);

            MutableLiveData<Boolean> result = new MutableLiveData<>();
            taskController.toggleTaskComplete(task, result);

            // Use single-time observer to avoid memory leaks
            result.observeForever(new androidx.lifecycle.Observer<Boolean>() {
                @Override
                public void onChanged(Boolean success) {
                    result.removeObserver(this); // Remove observer after first call
                    buttonView.setEnabled(true);

                    if (success) {
                        Toast.makeText(context,
                                isChecked ? "Task marked as completed" : "Task marked as incomplete",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Revert checkbox state if operation failed
                        task.setCompleted(!isChecked);
                        holder.checkBoxComplete.setChecked(!isChecked);
                        Toast.makeText(context, "Failed to update task status", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        // Set button listeners - Handle directly in adapter
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(context, "Edit: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Add your edit logic here
            // Example: Open edit dialog or activity
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("task_to_edit", task); // pass full task object
            v.getContext().startActivity(intent);
        });

        holder.btnView.setOnClickListener(v -> {
            Toast.makeText(context, "View: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Add your view logic here
            // Example: Show task details dialog
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Show confirmation and delete
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete \"" + task.getTitle() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        deleteTask(task, position);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void deleteTask(Task task, int position) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        taskController.deleteTask(task, result);

        // Use single-time observer to avoid memory leaks
        result.observeForever(new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                result.removeObserver(this); // Remove observer after first call

                if (success) {
                    tasks.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, tasks.size());
                    Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskTime, taskDescription, taskCategory, taskDate;
        CheckBox checkBoxComplete;
        ImageButton btnEdit, btnDelete, btnView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskCategory = itemView.findViewById(R.id.taskCategory);
            taskDate = itemView.findViewById(R.id.taskDate);
            checkBoxComplete = itemView.findViewById(R.id.checkBoxComplete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}