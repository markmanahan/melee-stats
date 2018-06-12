// Mark Manahan

/*

References:

- how to retrieve data from an intent (not for a result): https://stackoverflow.com/questions/4233873/how-do-i-get-extra-data-from-intent-on-android

 */

package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class SecondReviewActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    EditText beforeDate, afterDate;
    CheckBox isTourney;

    private int bYear, bMonth, bDay;
    private int aYear, aMonth, aDay;

    String your_char, opponent, opponent_char, location_name;

    boolean is_tournament;

    // before date, after date
    // boolean tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // open shared preferences file in private mode
        SharedPreferences shared_preferences = this.getSharedPreferences("SettingsPrefsFile", Context.MODE_PRIVATE);

        // pull theme information from shared preferences and update theme
        if (shared_preferences.contains("color_scheme")) {

            String color_scheme = shared_preferences.getString("color_scheme", null);

            if (color_scheme == "light") setTheme(R.style.LightAppTheme);
            else setTheme(R.style.DarkAppTheme);

        }

        setContentView(R.layout.activity_second_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Source for popup calendar:
        // https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        beforeDate =(EditText)findViewById(R.id.before_date);
        afterDate =(EditText)findViewById(R.id.after_date);
        isTourney = (CheckBox) findViewById(R.id.tournament_only);

        beforeDate.setOnClickListener(this);
        afterDate.setOnClickListener(this);


        // retrieve the intent that started this activity
        intent = getIntent();

        // extract the data from the intent
        your_char = intent.getStringExtra("yourChar");
        opponent = intent.getStringExtra("oppName");
        opponent_char = intent.getStringExtra("oppChar");
        location_name = intent.getStringExtra("locName");

    }

    public void search(View view){

        // make a new intent to send to the search results activity
        Intent new_intent = new Intent(this, SearchResultsActivity.class);

        // attach the search criteria to the intent to send over
        new_intent.putExtra("yourChar", your_char);
        new_intent.putExtra("oppName", opponent);
        new_intent.putExtra("oppChar", opponent_char);
        new_intent.putExtra("locName", location_name);

        String beforeDateText = beforeDate.getText().toString();
        String afterDateText = afterDate.getText().toString();

        new_intent.putExtra("beforeDate", beforeDateText);
        new_intent.putExtra("afterDate", afterDateText);

        String isATournament = "-1";
        if(isTourney.isChecked()){
            isATournament = "1";
        }

        new_intent.putExtra("isTournament", isATournament);

        // send the intent to the search results activity
        startActivity(new_intent);

    }

    @Override
    public void onClick(View view) {

        if (view == beforeDate) {
            final Calendar c = Calendar.getInstance();
            bYear = c.get(Calendar.YEAR);
            bMonth = c.get(Calendar.MONTH);
            bDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            beforeDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                        }
                    }, bYear, bMonth, bDay);
            datePickerDialog.show();
        }
        if (view == afterDate) {
            final Calendar d = Calendar.getInstance();
            aYear = d.get(Calendar.YEAR);
            aMonth = d.get(Calendar.MONTH);
            aDay = d.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            afterDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                        }
                    }, aYear, aMonth, aDay);
            datePickerDialog.show();
        }

    }
}