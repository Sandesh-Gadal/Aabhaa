package com.example.aabhaa.data;

import com.example.aabhaa.R;
import com.example.aabhaa.models.Content;

import java.util.ArrayList;
import java.util.List;

public class StaticContentProvider {

    public static List<Content> getAllExploreContents() {
        List<Content> contentList = new ArrayList<>();

        contentList.add(new Content(
                        1,
                        "Smart Farming Techniques",               // title_en
                        "स्मार्ट खेती प्रविधिहरू",               // ✅ title_ne
                        "Discover how smart farming improves...", // description_en
                        String.valueOf(R.drawable.bg_wheat),      // image_url
                        "article",
                        "Technology",
                        "Wheat",
                        "Beginner",
                        "Monsoon",
                        "Terai",
                        "en",
                        true,
                        null,
                        null,
                        1,
                        "2025-07-01T10:00:00",
                        "2025-06-30T08:30:00",
                        "2025-07-01T10:00:00"
                )
        );

        contentList.add(new Content(
                2,
                "Organic Pest Control",
                "नेपाली ज्ञान",
                "Natural pest solutions for small farms.",
                String.valueOf(R.drawable.change_password),
                "tip",
                "Pesticides",
                "Rice",
                "Intermediate",
                "Summer",
                "Hills",
                "en",
                false,
                null,
                null,
                2,
                "2025-06-28T07:45:00",
                "2025-06-28T07:00:00",
                "2025-06-28T07:45:00"
        ));

        contentList.add(new Content(
                3,
                "Effective Soil Management",
                "प्रभावकारी माटो व्यवस्थापन",
                "Learn the best practices for managing soil health.",
                String.valueOf(R.drawable.ic_partly_sunny),
                "article",
                "Soil",
                "Wheat",
                "Beginner",
                "Winter",
                "Terai",
                "en",
                true,
                null,
                null,
                3,
                "2025-06-25T09:30:00",
                "2025-06-24T08:00:00",
                "2025-06-25T09:30:00"
        ));

        contentList.add(new Content(
                4,
                "Weather Forecasting Tips",
                "मौसम पूर्वानुमान टिप्स",
                "How to use weather data to improve crop yield.",
                String.valueOf(R.drawable.reset_password),
                "article",
                "Weather",
                "Maize",
                "Intermediate",
                "Monsoon",
                "Hills",
                "en",
                false,
                null,
                null,
                4,
                "2025-07-02T11:15:00",
                "2025-07-01T10:00:00",
                "2025-07-02T11:15:00"
        ));

        contentList.add(new Content(
                5,
                "Seasonal Crop Reminder",
                "मौसमी बाली सम्झना",
                "Set reminders to care for your crops every season.",
                String.valueOf(R.drawable.bg_login),
                "reminder",
                "Reminder",
                "Rice",
                "Beginner",
                "Monsoon",
                "Terai",
                "en",
                true,
                null,
                null,
                5,
                "2025-06-30T08:00:00",
                "2025-06-29T07:00:00",
                "2025-06-30T08:00:00"
        ));

        contentList.add(new Content(
                6,
                "Advanced Irrigation Techniques",
                "उन्नत सिचाई प्रविधिहरू",
                "Maximize water efficiency with advanced irrigation methods.",
                String.valueOf(R.drawable.cover_img),
                "article",
                "Irrigation",
                "Wheat",
                "Advanced",
                "Winter",
                "Mountains",
                "en",
                false,
                null,
                null,
                6,
                "2025-07-03T13:20:00",
                "2025-07-02T12:00:00",
                "2025-07-03T13:20:00"
        ));


        contentList.add(new Content(
                7,
                "Organic Pest Control",
                  "Nepali gyan",
                "Natural pest solutions for small farms.",
                String.valueOf(R.drawable.profile_img),
                "tip",
                "Pesticides",
                "Rice",
                "Intermediate",
                "Summer",
                "Hills",
                "en",
                false,
                null,
                null,
                2,
                "2025-06-28T07:45:00",
                "2025-06-28T07:00:00",
                "2025-06-28T07:45:00"
        ));

        // Add more items here

        return contentList;
    }

    // You can even create more static getters for other types of content:
    public static List<Content> getTips() {
        // Filter tips only
        List<Content> tips = new ArrayList<>();
        for (Content c : getAllExploreContents()) {
            if (c.content_type.equals("tip")) tips.add(c);
        }
        return tips;
    }
}
