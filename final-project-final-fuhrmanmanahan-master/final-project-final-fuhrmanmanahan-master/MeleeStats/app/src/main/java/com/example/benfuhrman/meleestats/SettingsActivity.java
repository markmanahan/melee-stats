// Mark Manahan
// Implements Shared Preferences


/*

References:

- https://github.com/UVA-CS4720-S18/core-skills-android-final-fuhrmanmanahan/blob/master/app/src/main/java/cs4720/cs/virginia/edu/coreskills/SharedPreferencesActivity.java
- how to pull text from a spinner: https://stackoverflow.com/questions/10331854/how-to-get-spinner-selected-item-value-to-string
- how to check if a entry for a given key exists in shared preferences: https://stackoverflow.com/questions/22821270/how-to-check-if-sharedpreferences-exists-or-not?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
- styles and themes: https://developer.android.com/guide/topics/ui/look-and-feel/themes.html
- implementing user choice of theme: https://stackoverflow.com/questions/8811594/implementing-user-choice-of-theme
- dynamically changing a theme: https://stackoverflow.com/questions/2482848/how-to-change-current-theme-at-runtime-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
- shared preferences editor clear(): https://developer.android.com/reference/android/content/SharedPreferences.Editor.html

 */

package com.example.benfuhrman.meleestats;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity implements OnItemSelectedListener{

    public static final String PREFS_NAME = "SettingsPrefsFile";

    SharedPreferences shared_preferences;
    SharedPreferences.Editor editor;

    Spinner spinner1, spinner2, spinner3;
    ToggleButton toggle;

    String saved_character_one, saved_character_two, saved_character_three, color_scheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // initialize shared preferences object associated with PREFS file in private mode
        shared_preferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // pull color scheme from shared prefs file and update theme
        if (shared_preferences.contains("color_scheme")) {

            color_scheme = shared_preferences.getString("color_scheme", null);

            if (color_scheme == "light") setTheme(R.style.LightAppTheme);
            else setTheme(R.style.DarkAppTheme);

        }

        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);

        toggle = (ToggleButton) findViewById(R.id.toggleButton);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
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
        spinner2.setAdapter(dataAdapter);
        spinner3.setAdapter(dataAdapter);

        // pull from shared preferences file, update fields as necessary
        if (shared_preferences.contains("character_one")) {

            saved_character_one = shared_preferences.getString("character_one", null);
            spinner1.setSelection(charNames.indexOf(saved_character_one));

        }

        if (shared_preferences.contains("character_two")) {

            saved_character_two = shared_preferences.getString("character_two", null);
            spinner2.setSelection(charNames.indexOf(saved_character_two));

        }

        if (shared_preferences.contains("character_three")) {

            saved_character_three = shared_preferences.getString("character_three", null);
            spinner3.setSelection(charNames.indexOf(saved_character_three));

        }

        if (shared_preferences.contains("color_scheme")) {

            color_scheme = shared_preferences.getString("color_scheme", null);

            if (color_scheme == "light") toggle.setChecked(true);
            else toggle.setChecked(false);

        }

    }

    public void saveSettings(View view){

        // pull color scheme
        if (toggle.isChecked()) color_scheme = "light";
        else color_scheme = "dark";

        // pull data from spinners and store in shared prefs file
        editor = shared_preferences.edit();
        editor.clear();
        editor.putString("character_one", spinner1.getSelectedItem().toString());
        editor.putString("character_two", spinner2.getSelectedItem().toString());
        editor.putString("character_three", spinner3.getSelectedItem().toString());
        editor.putString("color_scheme", color_scheme);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
