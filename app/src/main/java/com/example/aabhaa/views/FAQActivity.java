package com.example.aabhaa.views;

import android.content.Context;
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerViewFAQ = findViewById(R.id.recyclerViewFAQ);
        recyclerViewFAQ.setLayoutManager(new LinearLayoutManager(this));

        faqList = new ArrayList<>();
        loadFAQsBasedOnLocale();

        faqAdapter = new FAQAdapter(faqList);
        recyclerViewFAQ.setAdapter(faqAdapter);
    }

    private void loadFAQsBasedOnLocale() {
        String lang = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getString("lang", "en"); // default to English


        if (lang.equals("ne")) {
            loadFAQsNe();
        } else {
            loadFAQsEn();
        }
    }

    private void loadFAQsNe() {
        faqList.add(new FAQ(
                "म विशेषज्ञलाई समस्या कसरी पठाउने?",
                "आफ्नो समस्या पठाउनको लागि, मुख्य मेनुबाट 'Expert Help' सेक्सन खोल्नुहोस्। आफ्नो स्थितिसँग मिल्ने समस्या प्रकार चयन गर्नुहोस्, समस्या विस्तारमा वर्णन गर्नुहोस्, र आवश्यक भए फोटोहरू संलग्न गर्नुहोस्। त्यसपछि 'Submit' थिच्नुहोस्। हाम्रो विशेषज्ञहरूले तपाईंको प्रश्न समीक्षा गरेर मार्गदर्शन दिनेछन्।"
        ));

        faqList.add(new FAQ(
                "म नयाँ ठेगाना कसरी थप्ने?",
                "'My Addresses' मा जानुहोस्। 'Add Address' बटन थिच्नुहोस्, त्यसपछि सबै आवश्यक फिल्डहरू जस्तै शीर्षक, प्रदेश, जिल्ला, अक्षांश, र देशान्तर भर्नुहोस्। तपाईं सन्दर्भको लागि छोटो विवरण पनि थप्न सक्नुहुन्छ। अन्ततः 'Save' थिच्नुहोस्।"
        ));

        faqList.add(new FAQ(
                "म माटो डेटा कसरी थप्ने?",
                "'Soil Data' सेक्सन खोल्नुहोस् र त्यो ठेगाना चयन गर्नुहोस् जहाँ माटो नमूना संकलन गरिएको थियो। NPK मानहरू (नाइट्रोजन, फोस्फोरस, पोटासियम), pH स्तर, र डेटा स्रोत चयन गर्नुहोस्। समाप्त भएपछि 'Save' थिचेर माटो डेटा रेकर्ड गर्नुहोस्।"
        ));

        faqList.add(new FAQ(
                "म्यानुअल माटो डेटा कहाँ पाउन सकिन्छ?",
                "तपाईं नजिकको कृषि प्रयोगशाला, सरकारी कृषि कार्यालय, वा अनुसन्धान केन्द्रमा जान सक्नुहुन्छ। तिनीहरूले NPK मान र pH स्तर सहितको माटो परीक्षण रिपोर्ट प्रदान गर्नेछन्, जुन तपाईंले एपमा प्रविष्ट गर्न सक्नुहुन्छ।"
        ));

        faqList.add(new FAQ(
                "मेरो भूमिको लागि उपयुक्त बाली कसरी जाँच्ने?",
                "मुख्य मेनुबाट 'Suitable Crops' खोल्नुहोस्। जाँच गर्न चाहेको माटो डेटा र ठेगाना चयन गर्नुहोस्। एपले तपाईंको माटो मान, स्थान, र मौसम डेटा प्रोसेस गरेर सबैभन्दा राम्रो बढ्ने संभावना भएका बालीहरू सुझाव दिनेछ।"
        ));

        faqList.add(new FAQ(
                "म कसरी कीटनाशक सूचना पाउने?",
                "तपाईंको उपकरण सेटिङ्स र एप सेटिङ्समा सूचनाहरू सक्षम छन् कि छैन जाँच गर्नुहोस्। तपाईंको बाली डेटा र स्थानीय कीट रिपोर्टको आधारमा, एपले समयमै सिफारिस गरिएका कीटनाशक प्रयोग र सुरक्षा उपायहरूको सूचना पठाउनेछ।"
        ));

        faqList.add(new FAQ(
                "म आफ्नो प्रोफाइल कसरी सम्पादन गर्ने?",
                "'Profile' सेक्सनमा जानुहोस्। सम्पादन आइकन थिच्नुहोस्, नाम, फोन नम्बर, वा भाषा प्राथमिकता जस्ता विवरण अपडेट गर्नुहोस्, र त्यसपछि 'Save' थिच्नुहोस्। तपाईंको प्रोफाइल तुरुन्त अपडेट हुनेछ।"
        ));

        faqList.add(new FAQ(
                "के म एप अफलाइन प्रयोग गर्न सक्छु?",
                "हो, धेरै सुविधाहरू जस्तै स्टोर गरिएको माटो डेटा, सुरक्षित ठेगाना, र खेती सुझावहरू अफलाइन पहुँच गर्न सकिन्छ। तर, प्रत्यक्ष डेटा आवश्यक पर्ने सुविधाहरू — जस्तै मौसम अपडेट, बाली सुझाव, वा विशेषज्ञ सहयोग — इन्टरनेट कनेक्शन आवश्यक पर्दछ।"
        ));

        faqList.add(new FAQ(
                "म एपको भाषा कसरी परिवर्तन गर्ने?",
                "'Settings' मा जानुहोस् र 'Language' थिच्नुहोस्। उपलब्ध भाषाहरूको सूची देखिनेछ। तपाईंको प्राथमिक भाषा चयन गर्नुहोस्, र एप तुरुन्तै चयन गरिएको भाषामा पुनः लोड हुनेछ।"
        ));

        faqList.add(new FAQ(
                "म माटो डेटा कसरी मेटाउने?",
                "'Soil Data' सेक्सन खोल्नुहोस् र मेटाउन चाहेको एंट्री चयन गर्नुहोस्। लामो प्रेस गर्नुहोस्, त्यसपछि मेनुबाट 'Delete' विकल्प थिच्नुहोस्। आफ्नो छनोट पुष्टि गर्नुहोस्, र एंट्री स्थायी रूपमा हटाइनेछ।"
        ));

        faqList.add(new FAQ(
                "म मौसम पूर्वानुमान कहाँ देख्न सक्छु?",
                "लाइभ मौसम पूर्वानुमान 'Home' स्क्रीनमा मौसम कार्ड भित्र देखाइन्छ। तपाईं यसमा थिचेर तपाईंको सुरक्षित ठेगानाहरूको दैनिक र साप्ताहिक पूर्वानुमान विस्तृत रूपमा हेर्न सक्नुहुन्छ।"
        ));

        faqList.add(new FAQ(
                "म सपोर्टसँग कसरी सम्पर्क गर्ने?",
                "'Help & Support' साइड मेनुबाट खोल्नुहोस्। तपाईंले एपबाट सिधै सन्देश पठाउन सक्नुहुन्छ वा प्रदान गरिएको इमेल ठेगाना प्रयोग गर्न सक्नुहुन्छ। सकेसम्म धेरै विवरण समावेश गर्नुहोस् ताकि हाम्रो टीमले छिटो सहयोग गर्न सकोस्।"
        ));

        faqList.add(new FAQ(
                "के म बाली सुझाव अरूसँग साझा गर्न सक्छु?",
                "हो। जब तपाईं बाली सुझाव परिणाम खोल्नुहुन्छ, तपाईंले साझा आइकन देख्नुहुनेछ। यसलाई थिचेर WhatsApp, इमेल, वा तपाईंको उपकरणमा इन्स्टल गरिएको अन्य एपमार्फत सुझावहरू साझा गर्न सक्नुहुन्छ।"
        ));

        faqList.add(new FAQ(
                "के एपले बहु फार्म समर्थन गर्दछ?",
                "अवश्य। तपाईं 'My Addresses' मा धेरै ठेगानाहरू थप्न सक्नुहुन्छ र हरेकका लागि माटो डेटा अलग्गै सुरक्षित गर्न सक्नुहुन्छ। यसले तपाईंलाई फरक फरक भूमिहरू स्वतन्त्र रूपमा व्यवस्थापन गर्न मद्दत गर्छ।"
        ));

        faqList.add(new FAQ(
                "एप कसरी अपडेट गर्ने?",
                "तपाईंको एप अद्यावधिक राख्न, Google Play Store खोल्नुहोस्, एपको नाम खोज्नुहोस्, र नयाँ संस्करण उपलब्ध भए 'Update' थिच्नुहोस्। यसले तपाईंलाई नयाँ सुविधाहरू, सुधारहरू, र सुरक्षा अपडेटहरू सुनिश्चित गर्दछ।"
        ));
    }

    private void loadFAQsEn() {
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

