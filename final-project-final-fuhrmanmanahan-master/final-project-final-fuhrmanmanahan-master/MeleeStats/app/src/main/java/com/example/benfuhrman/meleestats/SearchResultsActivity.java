package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    ArrayList<MatchItem> matches;
    RecyclerView rvMatches;
    TextView titleField;

    Intent intent;
    DatabaseHelper databaseHelper;

    String your_char = "", opp_name = "", opp_char = "", loc_name = "", before_date = "", after_date = "", is_tournament = "";


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

        setContentView(R.layout.activity_search_results);

        // Lookup the recyclerview in activity layout
        rvMatches = (RecyclerView) findViewById(R.id.rvMatches);
        titleField = (TextView) findViewById(R.id.winPercentage);

        intent = getIntent();

        // extract the data from the intent
        your_char = intent.getStringExtra("yourChar");
        opp_name =  intent.getStringExtra("oppName");
        opp_char =  intent.getStringExtra("oppChar");
        loc_name =  intent.getStringExtra("locName");

        before_date =  intent.getStringExtra("beforeDate");
        after_date =  intent.getStringExtra("afterDate");
        is_tournament =  intent.getStringExtra("isTournament");

        databaseHelper = DatabaseHelper.getInstance(this);
        Cursor res = databaseHelper.searchEntries(your_char, opp_name, opp_char, loc_name, is_tournament);

        // Old way to initialize matches
        // matches = MatchItem.createMatchItemsList(1);

        // New way to initialize matches
        matches = new ArrayList<MatchItem>();
        MatchItem match;
        while(res.moveToNext()){
            int mId;
            int mYourChar;
            String mOppName;
            int mOppChar;
            double mXLoc;
            double mYLoc;
            int mStage;
            int mDidWin;

            mId = Integer.parseInt(res.getString(0));
            mYourChar = Integer.parseInt(res.getString(1));
            mOppName = res.getString(2);
            mOppChar = Integer.parseInt(res.getString(3));
            mXLoc = Double.parseDouble(res.getString(4));
            mYLoc = Double.parseDouble(res.getString(5));
            mStage = Integer.parseInt(res.getString(6));
            mDidWin = Integer.parseInt(res.getString(7));

            String date = res.getString(8);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Boolean isValid = true;
            try {

                if(before_date.contains("/")){
                    if (!sdf.parse(date).before(sdf.parse(before_date))) {
                        isValid = false;
                    }
                }if(after_date.contains("/")){
                    if (!sdf.parse(date).after(sdf.parse(after_date))) {
                        isValid = false;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(isValid) {
                match = new MatchItem(mYourChar, mOppName, mOppChar, mXLoc, mYLoc, mStage, mDidWin, mId);
                matches.add(match);
            }

        }

        // Create adapter passing in the sample user data
        MatchListAdapter adapter = new MatchListAdapter(this, matches);
        // Attach the adapter to the recyclerview to populate items
        rvMatches.setAdapter(adapter);
        // Set layout manager to position the items
        rvMatches.setLayoutManager(new LinearLayoutManager(this));
        // That's all!

        double winCount = 0;
        for(MatchItem mItem : matches){
            if(mItem.getDidWin() == 1){
                winCount++;
            }
        }

        winCount /= matches.size();
        winCount *= 100;
        String winPercent = String.format("%.2f", winCount);
        titleField.setText(winPercent+"% Win");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void doneWithSearch(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
