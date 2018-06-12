/*
References:

- how to get a file path for an imageview: https://stackoverflow.com/questions/4181774/show-image-view-from-file-path?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

 */

package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class SpecificMatchActivity extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST = 3;

    String matchId;
    String mYourChar, mOppName, mOppChar, mXLoc, mYLoc, mStage, mDidWin, mDate, mLocName, mFriendly;
    String picture_id;
    DatabaseHelper databaseHelper;

    String charNames[] = {"Bowser", "C. Falcon", "DK", "Dr. Mario", "Falco", "Fox", "Ganondorf", "Ice Climbers", "Jigglypuff", "Kirby", "Link", "Luigi", "Mario", "Marth", "Mewtwo", "Mr. Game & Watch", "Ness", "Peach", "Pichu", "Pikachu", "Roy", "Samus", "Sheik", "Yoshi", "Young Link", "Zelda"};
    String stageNames[] = {"Battlefield", "DreamLand", "Yoshi's Story", "Final Destination", "Pokemon Stadium", "Fountain of Dreams"};

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

        setContentView(R.layout.activity_specific_match);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        matchId = data.getStringExtra("id");

        databaseHelper = DatabaseHelper.getInstance(this);
        Cursor res = databaseHelper.indivData(matchId);

        while (res.moveToNext()){

            // Get match information
            mYourChar = charNames[Integer.parseInt(res.getString(1))];
            mOppName = res.getString(2);
            mOppChar = charNames[Integer.parseInt(res.getString(3))];
            mXLoc = res.getString(4);
            mYLoc = res.getString(5);
            mStage = stageNames[Integer.parseInt(res.getString(6))];
            if(res.getString(7).contains("1")){
                mDidWin = "Win";
            } else {
                mDidWin = "Loss";
            }
            mDate = res.getString(8);
            mLocName = res.getString(9);
            if (res.getString(10).contains("1")) {
                mFriendly = "Tournament";
            } else {
                mFriendly = "Friendly";
            }
            picture_id = res.getString(11);

        }

        TextView date_textView = (TextView) findViewById(R.id.match_info_date);
        TextView opp_textView = (TextView) findViewById(R.id.match_info_opponent);
        TextView mapChar_textView = (TextView) findViewById(R.id.match_info_mapChar);
        TextView outcome_textView = (TextView) findViewById(R.id.match_info_outcome);
        TextView locName_textView = (TextView) findViewById(R.id.match_info_locName);
        TextView friendly_textView = (TextView) findViewById(R.id.match_info_isTournament);
        TextView coord_textView = (TextView) findViewById(R.id.match_info_coords);
        ImageView specific_person_imageView = (ImageView) findViewById(R.id.specific_person_image_view);

        String mapChar = mYourChar + " v. " + mOppChar + " on " + mStage;
        String coords = "(" + String.format(mXLoc, "%.2f") + ", " + String.format(mYLoc, "%.2f") + ")";

        date_textView.setText(mDate);
        opp_textView.setText(mOppName);
        mapChar_textView.setText(mapChar);
        outcome_textView.setText(mDidWin);
        locName_textView.setText(mLocName);
        friendly_textView.setText(mFriendly);
        coord_textView.setText(coords);

        File picture_of_opponent;

        try{
            picture_of_opponent = new File(picture_id);
            Bitmap new_bitmap = BitmapFactory.decodeFile(picture_of_opponent.getAbsolutePath());
            specific_person_imageView.setImageBitmap(new_bitmap);
        } catch (NullPointerException e){
            Log.d("Missing image", "No file found.");
        }

    }

    public void deleteEntry(View view){
        databaseHelper.removeEntry(matchId);
        Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
