package com.example.aabhaa.models;

import java.io.Serializable;

public class Content implements Serializable {
    public int id;
    public String title_en;
    public String title_ne;
    public String description_en; // You can later parse JSON if needed
    public String description_ne;
    public String image_url;
    public String content_type;
    public String category;
    public String crop_type;
    public String difficulty;
    public String season;
    public String region;

    public String language;
    public boolean is_verified;
    public String video_url;
    public String audio_url;
    public Integer user_id; // nullable
    public String published_at;
    public String created_at;
    public String updated_at;

    // Optional: You can add convenience methods here later



    // Constructor
    public Content(int id, String title_en, String title_ne,String description_en, String image_url, String content_type,
                   String category, String crop_type, String difficulty, String season,
                   String region, String language, boolean is_verified, String video_url,
                   String audio_url, Integer user_id, String published_at, String created_at, String updated_at) {
        this.id = id;
        this.title_en = title_en;
        this.title_ne = title_ne;
        this.description_en = description_en;
        this.image_url = image_url;
        this.content_type = content_type;
        this.category = category;
        this.crop_type = crop_type;
        this.difficulty = difficulty;
        this.season = season;
        this.region = region;
        this.language = language;
        this.is_verified = is_verified;
        this.video_url = video_url;
        this.audio_url = audio_url;
        this.user_id = user_id;
        this.published_at = published_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
