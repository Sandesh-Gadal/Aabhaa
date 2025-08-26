package com.example.aabhaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "crops")
public class Crop implements Parcelable {

    @PrimaryKey
    private int id;
    private String name;
    private String image_url;
    private String description_en;
    private String description_ne;
    private String category;
    private String season;

    private double temp_min_c;
    private double temp_max_c;

    private Integer humidity_min_pct;
    private Integer humidity_max_pct;

    private double ph_min;
    private double ph_max;

    private Integer rainfall_min_mm;
    private Integer rainfall_max_mm;

    private Integer n_min_kg;
    private Integer n_max_kg;
    private Integer p_min_kg;
    private Integer p_max_kg;
    private Integer k_min_kg;
    private Integer k_max_kg;

    private Integer duration_days_min;
    private Integer duration_days_max;

    private String steps;

    private String created_at;
    private String updated_at;

    // --- Empty constructor ---
    public Crop() {}

    // --- Full constructor ---
    public Crop(int id, String name, String image_url,
                String description_en, String description_ne,
                String category, String season,
                double temp_min_c, double temp_max_c,
                Integer humidity_min_pct, Integer humidity_max_pct,
                double ph_min, double ph_max,
                Integer rainfall_min_mm, Integer rainfall_max_mm,
                Integer n_min_kg, Integer n_max_kg,
                Integer p_min_kg, Integer p_max_kg,
                Integer k_min_kg, Integer k_max_kg,
                Integer duration_days_min, Integer duration_days_max,
                String steps, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.description_en = description_en;
        this.description_ne = description_ne;
        this.category = category;
        this.season = season;
        this.temp_min_c = temp_min_c;
        this.temp_max_c = temp_max_c;
        this.humidity_min_pct = humidity_min_pct;
        this.humidity_max_pct = humidity_max_pct;
        this.ph_min = ph_min;
        this.ph_max = ph_max;
        this.rainfall_min_mm = rainfall_min_mm;
        this.rainfall_max_mm = rainfall_max_mm;
        this.n_min_kg = n_min_kg;
        this.n_max_kg = n_max_kg;
        this.p_min_kg = p_min_kg;
        this.p_max_kg = p_max_kg;
        this.k_min_kg = k_min_kg;
        this.k_max_kg = k_max_kg;
        this.duration_days_min = duration_days_min;
        this.duration_days_max = duration_days_max;
        this.steps = steps;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // --- Parcelable Implementation ---
    protected Crop(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image_url = in.readString();
        description_en = in.readString();
        description_ne = in.readString();
        category = in.readString();
        season = in.readString();
        temp_min_c = in.readDouble();
        temp_max_c = in.readDouble();
        if (in.readByte() == 0) {
            humidity_min_pct = null;
        } else {
            humidity_min_pct = in.readInt();
        }
        if (in.readByte() == 0) {
            humidity_max_pct = null;
        } else {
            humidity_max_pct = in.readInt();
        }
        ph_min = in.readDouble();
        ph_max = in.readDouble();
        if (in.readByte() == 0) {
            rainfall_min_mm = null;
        } else {
            rainfall_min_mm = in.readInt();
        }
        if (in.readByte() == 0) {
            rainfall_max_mm = null;
        } else {
            rainfall_max_mm = in.readInt();
        }
        if (in.readByte() == 0) {
            n_min_kg = null;
        } else {
            n_min_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            n_max_kg = null;
        } else {
            n_max_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            p_min_kg = null;
        } else {
            p_min_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            p_max_kg = null;
        } else {
            p_max_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            k_min_kg = null;
        } else {
            k_min_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            k_max_kg = null;
        } else {
            k_max_kg = in.readInt();
        }
        if (in.readByte() == 0) {
            duration_days_min = null;
        } else {
            duration_days_min = in.readInt();
        }
        if (in.readByte() == 0) {
            duration_days_max = null;
        } else {
            duration_days_max = in.readInt();
        }
        steps = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Crop> CREATOR = new Creator<Crop>() {
        @Override
        public Crop createFromParcel(Parcel in) {
            return new Crop(in);
        }

        @Override
        public Crop[] newArray(int size) {
            return new Crop[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image_url);
        dest.writeString(description_en);
        dest.writeString(description_ne);
        dest.writeString(category);
        dest.writeString(season);
        dest.writeDouble(temp_min_c);
        dest.writeDouble(temp_max_c);

        if (humidity_min_pct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(humidity_min_pct);
        }
        if (humidity_max_pct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(humidity_max_pct);
        }

        dest.writeDouble(ph_min);
        dest.writeDouble(ph_max);

        if (rainfall_min_mm == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rainfall_min_mm);
        }
        if (rainfall_max_mm == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rainfall_max_mm);
        }

        if (n_min_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(n_min_kg);
        }
        if (n_max_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(n_max_kg);
        }
        if (p_min_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(p_min_kg);
        }
        if (p_max_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(p_max_kg);
        }
        if (k_min_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(k_min_kg);
        }
        if (k_max_kg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(k_max_kg);
        }
        if (duration_days_min == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(duration_days_min);
        }
        if (duration_days_max == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(duration_days_max);
        }

        dest.writeString(steps);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImage_url() { return image_url; }
    public String getDescription_en() { return description_en; }
    public String getDescription_ne() { return description_ne; }
    public String getCategory() { return category; }
    public String getSeason() { return season; }
    public double getTemp_min_c() { return temp_min_c; }
    public double getTemp_max_c() { return temp_max_c; }
    public Integer getHumidity_min_pct() { return humidity_min_pct; }
    public Integer getHumidity_max_pct() { return humidity_max_pct; }
    public double getPh_min() { return ph_min; }
    public double getPh_max() { return ph_max; }
    public Integer getRainfall_min_mm() { return rainfall_min_mm; }
    public Integer getRainfall_max_mm() { return rainfall_max_mm; }
    public Integer getN_min_kg() { return n_min_kg; }
    public Integer getN_max_kg() { return n_max_kg; }
    public Integer getP_min_kg() { return p_min_kg; }
    public Integer getP_max_kg() { return p_max_kg; }
    public Integer getK_min_kg() { return k_min_kg; }
    public Integer getK_max_kg() { return k_max_kg; }
    public Integer getDuration_days_min() { return duration_days_min; }
    public Integer getDuration_days_max() { return duration_days_max; }
    public String getSteps() { return steps; }
    public String getCreated_at() { return created_at; }
    public String getUpdated_at() { return updated_at; }
}
