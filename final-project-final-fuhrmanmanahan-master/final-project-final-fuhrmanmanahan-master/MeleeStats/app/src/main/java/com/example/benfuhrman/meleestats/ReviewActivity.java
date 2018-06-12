// Mark Manahan
// src = https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements OnItemSelectedListener {

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

        setContentView(R.layout.activity_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner1 = (Spinner) findViewById(R.id.user_char_spinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.opponent_char_spinner);

        spinner1.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        List<String> charNames = new ArrayList<String>();
        charNames.add("Select a Character");
        charNames.add("Bowser");
        charNames.add("C. Falcon");
        charNames.add("DK");
        charNames.add("Dr. Mario");
        charNames.add("Falco");
        charNames.add("Fox");
        charNames.add("Ganondorf");
        charNames.add("Ice Climbers");
        charNames.add("Jigglypuff");
        charNames.add("Kirby");
        charNames.add("Link");
        charNames.add("Luigi");
        charNames.add("Mario");
        charNames.add("Marth");
        charNames.add("Mewtwo");
        charNames.add("Mr. Game & Watch");
        charNames.add("Ness");
        charNames.add("Peach");
        charNames.add("Pichu");
        charNames.add("Pikachu");
        charNames.add("Roy");
        charNames.add("Samus");
        charNames.add("Sheik");
        charNames.add("Yoshi");
        charNames.add("Young Link");
        charNames.add("Zelda");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, charNames);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        spinner3.setAdapter(dataAdapter);

    }

    public void nextSearch(View view){

        // Intent intent = new Intent(this, SecondReviewActivity.class);
        // startActivity(intent);

        Spinner spinner1 = (Spinner) findViewById(R.id.user_char_spinner);
        EditText editTextOpp = (EditText) findViewById(R.id.opponent_editText);
        Spinner spinner3 = (Spinner) findViewById(R.id.opponent_char_spinner);
        EditText editTextLoc = (EditText) findViewById(R.id.location_editText);

        int spinner1position = spinner1.getSelectedItemPosition() -1;
        int spinner3position = spinner3.getSelectedItemPosition() -1;

        String yourChar = "" + spinner1position;
        String oppName = editTextOpp.getText().toString();
        String oppChar = "" + spinner3position;
        String locName = editTextLoc.getText().toString();


        Intent intent = new Intent(this, SecondReviewActivity.class);
        intent.putExtra("yourChar", yourChar);
        intent.putExtra("oppName", oppName);
        intent.putExtra("oppChar", oppChar);
        intent.putExtra("locName", locName);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
