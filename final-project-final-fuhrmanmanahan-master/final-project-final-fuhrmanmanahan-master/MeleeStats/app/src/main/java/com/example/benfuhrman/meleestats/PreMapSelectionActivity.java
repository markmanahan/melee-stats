// Mark Manahan

// activity used to store the date of a match before selecting maps and after choosing
// char, opponent, and location information

package com.example.benfuhrman.meleestats;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class PreMapSelectionActivity extends AppCompatActivity {

    DatePicker date_picker;
    Button button;

    Intent intent;

    String your_char, opponent, opponent_char, x_coord, y_coord;
    String month, day, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_map_selection);

        date_picker = (DatePicker) findViewById(R.id.match_datepicker);
        button = (Button) findViewById(R.id.map_selection_button);

        // retrieve the intent that started this activity
        intent = getIntent();

        // extract the character information from the intent of previous activity
        your_char = intent.getStringExtra("yourChar");
        opponent = intent.getStringExtra("oppName");
        opponent_char = intent.getStringExtra("oppChar");
        x_coord = intent.getStringExtra("xLoc");
        y_coord = intent.getStringExtra("yLoc");

    }

    public void mapSelection(View view) {

        // extract each component of the date from the DatePicker object
        month = Integer.toString(date_picker.getMonth() + 1);
        day = Integer.toString(date_picker.getDayOfMonth());
        year = Integer.toString(date_picker.getYear());

        // create an intent to send to the map selection activity
        Intent map_intent = new Intent(this, SecondAddGameActivity.class);

        // attach the char and date data to the intent to send over to map selection
        map_intent.putExtra("yourChar", your_char);
        map_intent.putExtra("oppName", opponent);
        map_intent.putExtra("oppChar", opponent_char);
        map_intent.putExtra("xLoc", x_coord);
        map_intent.putExtra("yLoc", y_coord);
        map_intent.putExtra("month", month);
        map_intent.putExtra("day", day);
        map_intent.putExtra("year", year);

        // start map selection activity
        startActivity(map_intent);

    }

}
