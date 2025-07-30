package com.example.aabhaa.data;

import com.example.aabhaa.R;
import com.example.aabhaa.models.ChatMessage;
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

    public static List<ChatMessage> chatMessages = new ArrayList<>();

    static {
        chatMessages.add(new ChatMessage(0, 1, "Hello! How can I help you today?", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(1, 0, "Can you tell me which crop suits my soil?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(2, 1, "Sure! Could you tell me your soil pH level and type?", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(3, 0, "My soil is loamy with a pH of around 6.8.", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(4, 1, "Great! Tomatoes, carrots, and lettuce grow well in loamy soil with that pH.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(5, 0, "That's good to know. What about rice?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(6, 1, "Rice grows best in clayey soil with good water retention. Is your field well-irrigated?", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(7, 0, "Yes, we have canal irrigation available year-round.", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(8, 1, "Perfect. Then you can consider growing rice as well, especially during the monsoon.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(9, 0, "Thanks! Should I add any fertilizer before planting?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(10, 1, "Yes, use organic compost or NPK fertilizer based on soil testing. Avoid over-fertilizing.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(11, 0, "How frequently should I water the tomato plants?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(12, 1, "Tomatoes need deep watering every 2–3 days, more often during hot weather.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(13, 0, "What’s the ideal temperature range for growing carrots?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(14, 1, "Carrots prefer cooler climates, around 15–21°C for optimal growth.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(15, 0, "Which pests should I watch out for when planting leafy greens?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(16, 1, "Watch out for aphids, cutworms, and snails. Neem oil spray helps a lot.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(17, 0, "Do I need crop rotation in a small garden?", "text", false, "", ChatMessage.SenderType.USER));

        chatMessages.add(new ChatMessage(18, 1, "Yes, even in small gardens, rotating crops prevents soil depletion and pest buildup.", "text", false, "", ChatMessage.SenderType.EXPERT));
        chatMessages.add(new ChatMessage(19, 0, "Thank you! This was very helpful.", "text", false, "", ChatMessage.SenderType.USER));
    }


    public static void addMessage(ChatMessage message) {
        chatMessages.add(message);
    }

}
