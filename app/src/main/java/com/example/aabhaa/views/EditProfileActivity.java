package com.example.aabhaa.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        AutoCompleteTextView autoCompleteFarmerType = findViewById(R.id.autoCompleteFarmerType);

        String[] farmerTypes = new String[] {"Small Scale", "Commercial", "Organic", "Subsistence"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                farmerTypes
        );

        autoCompleteFarmerType.setAdapter(adapter);

        AutoCompleteTextView acvEducation = findViewById(R.id.acvEducation);

        String[] educationLevels = new String[] {
                "No Schooling",
                "Primary",
                "High School Graduate",
                "Undergraduate",
                "Graduate",
                "Master's",
                "PhD"
        };

        ArrayAdapter<String> educationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                educationLevels
        );

        acvEducation.setAdapter(educationAdapter);


        TextInputEditText etDatePicker = findViewById(R.id.etdatePicker);

        etDatePicker.setFocusable(false); // Prevent manual typing
        etDatePicker.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditProfileActivity.this,
                    (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDatePicker.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


    }

}
