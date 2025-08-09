package com.example.aabhaa.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aabhaa.R;
import com.example.aabhaa.adapters.FAQAdapter;
import com.example.aabhaa.models.FAQ;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFAQ;
    private FAQAdapter faqAdapter;
    private List<FAQ> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerViewFAQ = findViewById(R.id.recyclerViewFAQ);
        recyclerViewFAQ.setLayoutManager(new LinearLayoutManager(this));

        faqList = new ArrayList<>();
        loadFAQs();

        faqAdapter = new FAQAdapter(faqList);
        recyclerViewFAQ.setAdapter(faqAdapter);
    }

    private void loadFAQs() {
        faqList.add(new FAQ(
                "How do I send issues to the expert?",
                "To send your issue, open the 'Expert Help' section from the main menu. Select the problem type that best matches your situation, describe the issue in detail, and attach any photos if needed. Then tap 'Submit'. Our experts will review your query and respond with guidance."
        ));

        faqList.add(new FAQ(
                "How do I add a new address?",
                "Go to 'My Addresses' from the side menu. Tap on the 'Add Address' button, then fill in all required fields such as title, province, district, latitude, and longitude. You can also add a short description for reference. Finally, tap 'Save' to store your address."
        ));

        faqList.add(new FAQ(
                "How do I add soil data?",
                "Open the 'Soil Data' section and select the address where the soil sample was collected. Enter the NPK values (Nitrogen, Phosphorus, Potassium), pH level, and select the source of the data. Once done, press 'Save' to record the soil data for that location."
        ));

        faqList.add(new FAQ(
                "Where can I get manual soil data?",
                "You can visit your nearest agricultural lab, government agriculture office, or research center to get a professional soil test. They will provide a printed or digital report with NPK values and pH level, which you can then enter into the app."
        ));

        faqList.add(new FAQ(
                "How do I check suitable crops for my land?",
                "Go to 'Suitable Crops' from the main menu. Select the soil data and address you want to analyze. The app will process your soil values, location, and climate data to suggest crops that have the highest chance of thriving in your conditions."
        ));

        faqList.add(new FAQ(
                "How do I get pesticide alerts?",
                "Make sure notifications are enabled in your device settings and in the app settings. Based on your crop data and local pest reports, the app will send timely alerts about recommended pesticide use and safety precautions."
        ));

        faqList.add(new FAQ(
                "How do I edit my profile?",
                "Navigate to the 'Profile' section. Tap the edit icon, update any of your details such as name, phone number, or language preference, and then press 'Save'. Your profile will be updated immediately."
        ));

        faqList.add(new FAQ(
                "Can I use the app offline?",
                "Yes, many features such as stored soil data, saved addresses, and farming tips can be accessed offline. However, features that require live data — such as weather updates, crop suggestions, or expert help — will need an internet connection."
        ));

        faqList.add(new FAQ(
                "How do I change the app language?",
                "Go to 'Settings' and tap on 'Language'. You will see a list of available languages. Select your preferred one, and the app will reload its interface in the chosen language immediately."
        ));

        faqList.add(new FAQ(
                "How do I delete soil data?",
                "Open the 'Soil Data' section and locate the entry you want to delete. Long press on it, then tap the 'Delete' option from the menu. Confirm your choice, and the entry will be removed permanently."
        ));

        faqList.add(new FAQ(
                "Where can I see my weather forecast?",
                "The live weather forecast is shown on the 'Home' screen inside the weather card. You can tap on it to view detailed daily and weekly forecasts for your saved addresses."
        ));

        faqList.add(new FAQ(
                "How do I contact support?",
                "Open 'Help & Support' from the side menu. You can either send us a message directly through the app or use the provided email address. Include as much detail as possible so our team can assist you quickly."
        ));

        faqList.add(new FAQ(
                "Can I share crop suggestions with others?",
                "Yes. When you open a crop suggestion result, you’ll see a share icon. Tap it to share the suggestions through WhatsApp, email, or any other app installed on your device."
        ));

        faqList.add(new FAQ(
                "Does the app support multiple farms?",
                "Absolutely. You can add multiple addresses in 'My Addresses' and store soil data separately for each one. This helps you manage different plots of land independently within the app."
        ));

        faqList.add(new FAQ(
                "How to update the app?",
                "To keep your app up to date, open the Google Play Store, search for the app name, and tap 'Update' if a new version is available. This ensures you get the latest features, improvements, and security updates."
        ));

    }
}

