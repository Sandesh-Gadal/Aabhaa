package com.example.aabhaa.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Serializable;
import java.lang.reflect.Type;

import lombok.Data;

@Data
@Entity(tableName = "tasks")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String title;
    private String description;
    private String category;
    private String date; // yyyy-MM-dd
    private String time; // HH:mm:ss
    private String reminder;
    private String repeat_interval;

    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("completed")
    private boolean completed; // This will handle both boolean and number from JSON

    public Task() {
        // Default constructor required by Room
    }

    public Task(int userId, String title, String description, String category,
                String date, String time, String reminder, String repeat_interval, boolean completed) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.time = time;
        this.reminder = reminder;
        this.repeat_interval = repeat_interval;
        this.completed = completed;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getReminder() { return reminder; }
    public void setReminder(String reminder) { this.reminder = reminder; }

    public String getRepeatInterval() { return repeat_interval; }
    public void setRepeatInterval(String repeatInterval) { this.repeat_interval = repeat_interval; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Custom TypeAdapter to handle boolean/number conversion
    public static class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                JsonPrimitive primitive = json.getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    return primitive.getAsBoolean();
                } else if (primitive.isNumber()) {
                    // Convert number to boolean: 0 = false, anything else = true
                    return primitive.getAsInt() != 0;
                } else if (primitive.isString()) {
                    String value = primitive.getAsString().toLowerCase();
                    return "true".equals(value) || "1".equals(value);
                }
            }
            return false; // Default to false if unable to parse
        }

        @Override
        public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }
}