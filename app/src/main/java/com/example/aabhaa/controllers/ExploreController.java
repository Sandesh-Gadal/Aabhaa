package com.example.aabhaa.controllers;

import com.example.aabhaa.R;
import com.example.aabhaa.models.Content;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExploreController {

    // Return static list of contents (dummy data)
    // Return static list of contents (dummy data for testing)
    public List<Content> getDummyContents() {
        List<Content> contentList = new ArrayList<>();

        // Add some dummy content for testing
        contentList.add(new Content(
                1,
                "Mental Health Awareness",
                "मानसिक स्वास्थ्य चेतना",
                "Understanding the importance of mental health in daily life",
                "दैनिक जीवनमा मानसिक स्वास्थ्यको महत्व बुझ्ने",
                String.valueOf(R.drawable.bg_wheat),
                "mental-health",
                true,
                "",
                "",
                1,
                "2024-01-15T10:30:00",
                "2024-01-15T10:30:00",
                "2024-01-15T10:30:00"
        ));

        contentList.add(new Content(
                2,
                "Meditation Techniques",
                "ध्यान प्रविधिहरू",
                "Learn various meditation techniques for stress relief",
                "तनाव कम गर्न विभिन्न ध्यान प्रविधिहरू सिक्नुहोस्",
                String.valueOf(R.drawable.bg_wheat),
                "meditation",
                true,
                "https://example.com/meditation-video.mp4",
                "",
                1,
                "2024-01-14T15:20:00",
                "2024-01-14T15:20:00",
                "2024-01-14T15:20:00"
        ));

        contentList.add(new Content(
                3,
                "Breathing Exercises",
                "सास फेर्ने अभ्यासहरू",
                "Simple breathing exercises for anxiety management",
                "चिन्ता व्यवस्थापनका लागि सरल सास फेर्ने अभ्यासहरू",
                String.valueOf(R.drawable.bg_wheat),
                "breathing",
                true,
                "",
                "https://example.com/breathing-audio.mp3",
                1,
                "2024-01-13T09:45:00",
                "2024-01-13T09:45:00",
                "2024-01-13T09:45:00"
        ));

        return contentList;
    }

    // Utility to format time ago (optional: or call DateTimeUtils)
    public String formatTimeAgo(String publishedAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date publishedDate = sdf.parse(publishedAt);
            Date now = new Date();

            long diffInMillis = now.getTime() - publishedDate.getTime();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            if (seconds < 60)
                return seconds + " seconds ago";
            else if (minutes < 60)
                return minutes + " minutes ago";
            else if (hours < 24)
                return hours + " hours ago";
            else
                return days + " days ago";

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
