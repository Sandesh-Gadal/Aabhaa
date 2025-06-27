package com.example.aabhaa.views;


import android.os.Bundle;
import android.widget.CalendarView;


import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityCalendarBinding;




public class CalendarActivity extends BaseActivity<ActivityCalendarBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(binding.bottomNavigationView, R.id.nav_calendar);

        binding.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;

            }
        });
    }

}