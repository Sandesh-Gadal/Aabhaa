package com.example.aabhaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "contents")
public class Content implements Parcelable {

    @PrimaryKey
    public int id;
    public String title_en;
    public String title_ne; // Changed from title_np to title_ne to match DB
    public String description_en;
    public String description_ne; // Changed from description_np to description_ne to match DB
    public String image_url;
    public String category;
    public boolean is_verified;
    public String video_url;
    public String audio_url;
    public int user_id;
    public String published_at;
    public String created_at;
    public String updated_at;

    public Content() {}

    public Content(int id, String title_en, String title_ne, String description_en,
                   String description_ne, String image_url, String category,
                   boolean is_verified, String video_url, String audio_url,
                   int user_id, String published_at, String created_at, String updated_at) {
        this.id = id;
        this.title_en = title_en;
        this.title_ne = title_ne;
        this.description_en = description_en;
        this.description_ne = description_ne;
        this.image_url = image_url;
        this.category = category;
        this.is_verified = is_verified;
        this.video_url = video_url;
        this.audio_url = audio_url;
        this.user_id = user_id;
        this.published_at = published_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Helper method to determine content type
    public String getContentType() {
        if (video_url != null && !video_url.isEmpty()) {
            return "video";
        } else if (audio_url != null && !audio_url.isEmpty()) {
            return "audio";
        } else {
            return "article";
        }
    }

    // Parcelable implementation
    protected Content(Parcel in) {
        id = in.readInt();
        title_en = in.readString();
        title_ne = in.readString();
        description_en = in.readString();
        description_ne = in.readString();
        image_url = in.readString();
        category = in.readString();
        is_verified = in.readByte() != 0;
        video_url = in.readString();
        audio_url = in.readString();
        user_id = in.readInt();
        published_at = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title_en);
        dest.writeString(title_ne);
        dest.writeString(description_en);
        dest.writeString(description_ne);
        dest.writeString(image_url);
        dest.writeString(category);
        dest.writeByte((byte) (is_verified ? 1 : 0));
        dest.writeString(video_url);
        dest.writeString(audio_url);
        dest.writeInt(user_id);
        dest.writeString(published_at);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}