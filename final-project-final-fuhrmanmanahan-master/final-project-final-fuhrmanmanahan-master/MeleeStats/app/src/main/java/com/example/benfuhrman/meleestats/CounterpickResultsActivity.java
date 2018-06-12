package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CounterpickResultsActivity extends AppCompatActivity {

    String charNames[] = {"Bowser", "C. Falcon", "DK", "Dr. Mario", "Falco", "Fox", "Ganondorf", "Ice Climbers", "Jigglypuff", "Kirby", "Link", "Luigi", "Mario", "Marth", "Mewtwo", "Mr. Game & Watch", "Ness", "Peach", "Pichu", "Pikachu", "Roy", "Samus", "Sheik", "Yoshi", "Young Link", "Zelda"};
    String stageNames[] = {"Battlefield", "DreamLand", "Yoshi's Story", "Final Destination", "Pokemon Stadium", "Fountain of Dreams"};

    ArrayList<MatchItem> matches;

    TextView rec1;
    TextView rec2;
    TextView rec3;

    String oppName, oppChar;

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

        setContentView(R.layout.activity_counterpick_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        oppName = data.getStringExtra("oppName");
        oppChar = data.getStringExtra("oppChar");

        databaseHelper = DatabaseHelper.getInstance(this);
        //Cursor res = databaseHelper.getAllData();
        Cursor res = databaseHelper.counterpickData(oppName, oppChar);

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

            match = new MatchItem(mYourChar, mOppName, mOppChar, mXLoc, mYLoc, mStage, mDidWin, mId);
            matches.add(match);
        }

        int[][] charStageWins = new int[26][6];
        int[][] charStageTotal = new int[26][6];

        double[][] winPercentages = new double[26][6];

        for(MatchItem mItem : matches){
            int xIndex = mItem.getYourChar();
            int yIndex = mItem.getStage();

            if(mItem.getDidWin() == 1){
                charStageWins[xIndex][yIndex] += 1;
            }
            charStageTotal[xIndex][yIndex] += 1;
        }

        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 6; j++){
                winPercentages[i][j] =  (double) charStageWins[i][j] / (double) charStageTotal[i][j];
            }
        }

        double first, second, third;
        String sFirst, sSecond, sThird;
        third = first = second = 0;
        sFirst = sSecond = sThird = "0,0";

        for(int a = 0; a < 26; a++) {
            for (int b = 0; b < 6; b++) {
                if(winPercentages[a][b] > first){
                    third = second;
                    sThird = sThird;

                    second = first;
                    sSecond = sFirst;

                    first = winPercentages[a][b];
                    sFirst = "" + a + "," + b;
                }
                else if(winPercentages[a][b] > second){
                        third = second;
                        sThird = sSecond;

                        second = winPercentages[a][b];
                        sSecond = "" + a + "," + b;
                }
                else if(winPercentages[a][b] > third){
                    third = winPercentages[a][b];
                    sThird = "" + a + "," + b;
                }
            }
        }

        first *= 100; second *= 100; third *= 100;

        rec1 = (TextView) findViewById(R.id.rec1);
        rec2 = (TextView) findViewById(R.id.rec2);
        rec3 = (TextView) findViewById(R.id.rec3);

        String[] firstEntry = sFirst.split(",");
        String[] secondEntry = sSecond.split(",");
        String[] thirdEntry = sThird.split(",");

        int firstChar = Integer.parseInt(firstEntry[0]);
        int firstStage = Integer.parseInt(firstEntry[1]);

        int secondChar = Integer.parseInt(secondEntry[0]);
        int secondStage = Integer.parseInt(secondEntry[1]);

        int thirdChar = Integer.parseInt(thirdEntry[0]);
        int thirdStage = Integer.parseInt(thirdEntry[1]);

        String winPercentOne = String.format("%.2f", first);
        String winPercentSecond = String.format("%.2f", second);
        String winPercentThird = String.format("%.2f", third);

        String errorText = "No positive win rates found";
        String text1, text2, text3;

        if(first != 0){
            text1 = "1.  " + charNames[firstChar] + " on " + stageNames[firstStage] + " (" + winPercentOne + "%)";
        } else {
            text1 = "1.  " + errorText;
        }

        if(second != 0){
            text2 = "2.  " + charNames[secondChar] + " on " + stageNames[secondStage] + " (" + winPercentSecond + "%)";
        } else {
            text2 = "2.  " + errorText;
        }

        if(third != 0){
            text3 = "3.  " + charNames[thirdChar] + " on " + stageNames[thirdStage] + " (" + winPercentThird + "%)";
        } else {
            text3 = "3.  " + errorText;
        }

        rec1.setText(text1);
        rec2.setText(text2);
        rec3.setText(text3);

    }

    public void doneWithCounterpick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
