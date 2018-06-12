package com.example.benfuhrman.meleestats;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondAddGameActivity extends AppCompatActivity {

    CheckBox checkbox;
    int whichStage = 99;
    ToggleButton winStatusButton;

    String yourChar, oppName, oppChar, xLoc, yLoc, date, locName, picture_id;

    DatabaseHelper databaseHelper;

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

        setContentView(R.layout.activity_second_add_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get info from previous AddGame screen
        Intent data = getIntent();
        yourChar = data.getStringExtra("yourChar");
        oppName = data.getStringExtra("oppName");
        oppChar = data.getStringExtra("oppChar");
        xLoc = data.getStringExtra("xLoc");
        yLoc = data.getStringExtra("yLoc");
        locName = data.getStringExtra("locName");
        picture_id = data.getStringExtra("picture_id");

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date todays_date = new Date();
        date = dateFormat.format(todays_date);

        // Connects to database if exists, else creates new one
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    public void addBorder(View view) {

        switch (view.getId()) {
            case R.id.btn_battlefield:
                whichStage = 0;
                break;
            case R.id.btn_dreamland:
                whichStage = 1;
                break;
            case R.id.btn_yoshis_story:
                whichStage = 2;
                break;
            case R.id.btn_final_destination:
                whichStage = 3;
                break;
            case R.id.btn_pokemon_stadium:
                whichStage = 4;
                break;
            case R.id.btn_fountain_of_dreams:
                whichStage = 5;
                break;
        }

        ImageButton btn_battlefield = (ImageButton) findViewById(R.id.btn_battlefield);
        ImageButton btn_dreamland = (ImageButton) findViewById(R.id.btn_dreamland);
        ImageButton btn_yoshis_story = (ImageButton) findViewById(R.id.btn_yoshis_story);
        ImageButton btn_final_destination = (ImageButton) findViewById(R.id.btn_final_destination);
        ImageButton btn_pokemon_stadium = (ImageButton) findViewById(R.id.btn_pokemon_stadium);
        ImageButton btn_fountain_of_dreams = (ImageButton) findViewById(R.id.btn_fountain_of_dreams);

        ImageButton[] buttons = {btn_battlefield, btn_dreamland, btn_yoshis_story, btn_final_destination, btn_pokemon_stadium, btn_fountain_of_dreams};
        for (int i = 0; i < 6; i++) {
            if (i == whichStage) {
                buttons[i].setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.colorPrimary));
            } else {
                buttons[i].setBackgroundTintList(null);
            }
        }
    }

    public void addMatch(View view) {
        // Get match info
        int outcome;

        winStatusButton = (ToggleButton) findViewById(R.id.toggleWinStatus);
        Boolean didWin = winStatusButton.isChecked();
        if (didWin) {
            outcome = 1;
        } else {
            outcome = 0;
        }

        if (whichStage != 99) {
            // Convert intent extras to appropriate types
            int finalYourChar = Integer.parseInt(yourChar) - 1;
            String finalOppName = oppName;
            int finalOppChar = Integer.parseInt(oppChar) - 1;

            CheckBox tournament_checkbox = (CheckBox) findViewById(R.id.friendly);
            int isTournament = 1;
            if(tournament_checkbox.isChecked()){
                isTournament = 0;
            }

            double finalXLoc = Double.parseDouble(xLoc);
            double finalYLoc = Double.parseDouble(yLoc);

            Boolean didInsertCorrectly = databaseHelper.insertEntry(finalYourChar, finalOppName, finalOppChar, finalXLoc, finalYLoc, whichStage, outcome, date, locName, isTournament, picture_id);

            if (didInsertCorrectly) {
                Toast.makeText(SecondAddGameActivity.this, "Match added", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SecondAddGameActivity.this, "Match NOT added", Toast.LENGTH_LONG).show();
            }

            Intent intent;
            checkbox = (CheckBox) findViewById(R.id.multiple);
            if (checkbox.isChecked()) {
                // Do nothing, stay on same screen
            } else {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(SecondAddGameActivity.this, "You must select a stage", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("whichStage", whichStage);

        // etc.

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Context context = getApplicationContext();


        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        whichStage = savedInstanceState.getInt("whichStage");
        ImageButton btn_battlefield = (ImageButton) findViewById(R.id.btn_battlefield);
        ImageButton btn_dreamland = (ImageButton) findViewById(R.id.btn_dreamland);
        ImageButton btn_yoshis_story = (ImageButton) findViewById(R.id.btn_yoshis_story);
        ImageButton btn_final_destination = (ImageButton) findViewById(R.id.btn_final_destination);
        ImageButton btn_pokemon_stadium = (ImageButton) findViewById(R.id.btn_pokemon_stadium);
        ImageButton btn_fountain_of_dreams = (ImageButton) findViewById(R.id.btn_fountain_of_dreams);

        ImageButton[] buttons = {btn_battlefield, btn_dreamland, btn_yoshis_story, btn_final_destination, btn_pokemon_stadium, btn_fountain_of_dreams};
        buttons[whichStage].setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));

    }
}
