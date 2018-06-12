package com.example.benfuhrman.meleestats;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;

public class AddGameActivity extends AppCompatActivity implements OnItemSelectedListener, LocationListener {

    double[] myLocation = {0, 0};
    LocationManager locationManager;

    // src for preventing pop up keyboard = https://stackoverflow.com/questions/2892615/how-to-remove-auto-focus-keyboard-popup-of-a-field-when-the-screen-shows-up

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

        setContentView(R.layout.activity_add_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestGPS();
        startGPS();

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        EditText locNameText = (EditText) findViewById(R.id.editTextLocName);

        spinner1.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        List<String> charNames = new ArrayList<String>();

        charNames.add("Select a Character");

        if (shared_preferences.contains("character_one")){
            String character_one = shared_preferences.getString("character_one", null);
            charNames.add(character_one);
        } else {
            charNames.add("Main 1");
        }

        if (shared_preferences.contains("character_two")){
            String character_two = shared_preferences.getString("character_two", null);
            charNames.add(character_two);
        } else {
            charNames.add("Main 2");
        }

        if (shared_preferences.contains("character_three")){
            String character_three = shared_preferences.getString("character_three", null);
            charNames.add(character_three);
        } else {
            charNames.add("Main 3");
        }

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
        locNameText.setText(closestLocation());
    }

    // src = https://stackoverflow.com/questions/33865445/gps-location-provider-requires-access-fine-location-permission-for-android-6-0

    public void requestGPS() {
        if (ActivityCompat.checkSelfPermission(AddGameActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddGameActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddGameActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
    }


    public void startGPS() {
        requestGPS();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this);
        Location last_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(last_location != null){
            setGPS(last_location);
        }
    }

    public void setGPS(Location location){
        myLocation[0] = location.getLongitude();
        myLocation[1] = location.getLatitude();
    }

    @Override
    public void onLocationChanged(Location location) {
        setGPS(location);
        //Log.d("Location changed", "" + myLocation[0] + " " + myLocation[1]);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

    // src = https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi

    private double distance(double lat2, double lon2) {
        double lat1 = myLocation[1];
        double lon1 = myLocation[0];
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public String closestLocation(){
        String closestLocationName = "";

        DatabaseHelper databaseHelper;
        databaseHelper = DatabaseHelper.getInstance(this);
        Cursor res = databaseHelper.getAllData();

        double smallestDistance = Double.MAX_VALUE;

        while(res.moveToNext()){
            double mXLoc;
            double mYLoc;


            mXLoc = Double.parseDouble(res.getString(4));
            mYLoc = Double.parseDouble(res.getString(5));

            //Log.d("" + myLocation[0] + " " + myLocation[1], "" + mXLoc + " " + mYLoc);

            double new_distance = distance(mYLoc, mXLoc);
            if(new_distance < smallestDistance){
                smallestDistance = new_distance;
                closestLocationName = res.getString(9);
            }
        }

        return closestLocationName;
    }

    public void next(View view){
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        EditText locNameText = (EditText) findViewById(R.id.editTextLocName);
        EditText oppNameText = (EditText) findViewById(R.id.addGame_opp);

        int spinner1position = spinner1.getSelectedItemPosition();
        int spinner3position = spinner3.getSelectedItemPosition();

        Map<String, Integer> charLookUp = new HashMap<String, Integer>();
        charLookUp.put("Bowser", 1);
        charLookUp.put("C. Falcon", 2);
        charLookUp.put("DK", 3);
        charLookUp.put("Dr. Mario", 4);
        charLookUp.put("Falco", 5);
        charLookUp.put("Fox", 6);
        charLookUp.put("Ganondorf", 7);
        charLookUp.put("Ice Climbers", 8);
        charLookUp.put("Jigglypuff", 9);
        charLookUp.put("Kirby", 10);
        charLookUp.put("Link", 11);
        charLookUp.put("Luigi", 12);
        charLookUp.put("Mario", 13);
        charLookUp.put("Marth", 14);
        charLookUp.put("Mewtwo", 15);
        charLookUp.put("Mr. Game & Watch", 16);
        charLookUp.put("Ness", 17);
        charLookUp.put("Peach", 18);
        charLookUp.put("Pichu", 19);
        charLookUp.put("Pikachu", 20);
        charLookUp.put("Roy", 21);
        charLookUp.put("Samus", 22);
        charLookUp.put("Sheik", 23);
        charLookUp.put("Yoshi", 24);
        charLookUp.put("Young Link", 25);
        charLookUp.put("Zelda", 26);

        if(spinner1position > 0 && spinner1position < 4){
            Integer tmp = charLookUp.get(spinner1.getSelectedItem().toString());
            if(tmp == null){
                tmp = 0;
            }
            spinner1position = tmp;
        } else{
            spinner1position -= 3;
        }

        if(spinner3position > 0 && spinner3position < 4){
            Integer tmp = charLookUp.get(spinner3.getSelectedItem().toString());
            if(tmp == null){
                tmp = 0;
            }
            spinner3position = tmp;
        } else{
            spinner3position -= 3;
        }

        String yourChar = "" + spinner1position;
        String oppName = oppNameText.getText().toString();
        String oppChar = "" + spinner3position;
        // #TODO: Update to include GPS tracking
        String xLoc = String.format("%.2f", myLocation[0]);
        String yLoc = String.format("%.2f", myLocation[1]);
        String locName = locNameText.getText().toString();


        if(spinner1position == 0 || oppName == "" || spinner3position == 0 || locName == ""){
            // Do Nothing
        } else {
            // #Todo only go to camera activity if opponent is not in database, auto
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra("yourChar", yourChar);
            intent.putExtra("oppName", oppName);
            intent.putExtra("oppChar", oppChar);
            intent.putExtra("xLoc", xLoc);
            intent.putExtra("yLoc", yLoc);
            intent.putExtra("locName", locName);
            startActivity(intent);
        }
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
