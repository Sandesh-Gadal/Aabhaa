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
    public List<Content> getExploreContents() {
        List<Content> contentList = new ArrayList<>();

        contentList.add(new Content(
                1,
                "Smart Farming Techniques",
                "स्मार्ट खेती प्रविधिहरू",
                "Discover how smart farming improves smallholder yields.",
                String.valueOf(R.drawable.bg_wheat),
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
        ));

        contentList.add(new Content(
                2,
                "Organic Pest Control",
                "जैविक कीट नियन्त्रण",
                "Learn natural ways to handle pest issues.",
                String.valueOf(R.drawable.bg_wheat),
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
