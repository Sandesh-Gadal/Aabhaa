package com.example.aabhaa.models;

public class User {
    private String full_name;
    private String phone;
    private String gender;
    private String date_of_birth;
    private String farmer_type;
    private int experience_years;
    private String education_level;
    private String preferred_language;
    private String profile_image_url;

    public User(String full_name, String phone, String gender, String date_of_birth,
                String farmer_type, int experience_years, String education_level,
                String preferred_language, String profile_image_url) {
        this.full_name = full_name;
        this.phone = phone;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.farmer_type = farmer_type;
        this.experience_years = experience_years;
        this.education_level = education_level;
        this.preferred_language = preferred_language;
        this.profile_image_url = profile_image_url;
    }

    public String getFullName() {
        return full_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public String getFarmerType() {
        return farmer_type;
    }

    public int getExperienceYears() {
        return experience_years;
    }

    public String getEducationLevel() {
        return education_level;
    }

    public String getPreferredLanguage() {
        return preferred_language;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }
}
