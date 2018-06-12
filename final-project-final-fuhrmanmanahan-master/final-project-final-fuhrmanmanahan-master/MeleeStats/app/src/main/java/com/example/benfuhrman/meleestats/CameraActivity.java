// Mark Manahan

/*

References:
- https://github.com/UVA-CS4720-S18/core-skills-android-final-fuhrmanmanahan/blob/master/app/src/main/java/cs4720/cs/virginia/edu/coreskills/CameraActivity.java
- how to center objects in an android linear layout: https://stackoverflow.com/questions/18051472/how-to-center-the-content-inside-a-linear-layout?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
- xml work: https://stackoverflow.com/questions/37953476/android-how-to-get-a-content-uri-for-a-file-in-the-external-storage-public-d
- center text in textview: https://stackoverflow.com/questions/432037/how-do-i-center-text-horizontally-and-vertically-in-a-textview
- scale an image to fit to image view: https://stackoverflow.com/questions/2521959/how-to-scale-an-image-in-imageview-to-keep-the-aspect-ratio?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

- Code from the lecture slides:
=====================================================================================
// create Intent to take a picture and return control to the calling application
Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// create a file to save the image
fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
// set the image file name
intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
// start the image capture Intent
startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
=====================================================================================

 */

package com.example.benfuhrman.meleestats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    static final int TAKE_PHOTO_PERMISSION = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    ImageView person_image_view;
    Button take_picture_button;
    TextView opponent_textview;

    Uri file;

    String your_char, opponent, opponent_char, x_coord, y_coord, location_name;
    static String picture_id;

    Intent intent_from_add_game, intent_to_map_selection;
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

        setContentView(R.layout.activity_camera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // retrieve intent from initial add game activity
        intent_from_add_game = getIntent();

        // extract information entered in initial add game activity from intent
        your_char = intent_from_add_game.getStringExtra("yourChar");
        opponent = intent_from_add_game.getStringExtra("oppName");
        opponent_char = intent_from_add_game.getStringExtra("oppChar");
        x_coord = intent_from_add_game.getStringExtra("xLoc");
        y_coord = intent_from_add_game.getStringExtra("yLoc");
        location_name = intent_from_add_game.getStringExtra("locName");

        // retrieve objects from linear layout
        take_picture_button = (Button) findViewById(R.id.take_picture_button);
        person_image_view = (ImageView) findViewById(R.id.person_image_view);
        opponent_textview =(TextView) findViewById(R.id.opponent_tag);

        // set text in opponent name TextView to opponent's name
        opponent_textview.setText(opponent);

        databaseHelper = DatabaseHelper.getInstance(this);
        Cursor res = databaseHelper.newOpponent(opponent);

        while(res.moveToNext()){
            picture_id = res.getString(11);
        }

        File picture_of_opponent;
        try{
            picture_of_opponent = new File(picture_id);
            Bitmap new_bitmap = BitmapFactory.decodeFile(picture_of_opponent.getAbsolutePath());
            person_image_view.setImageBitmap(new_bitmap);
        } catch (NullPointerException e){
            Log.d("Missing image", "No file found.");
        }

        // We are giving you the code that checks for permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            take_picture_button.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, TAKE_PHOTO_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // This is called when permissions are either granted or not for the app
        // You do not need to edit this code.

        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                take_picture_button.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        // Add code here to start the process of taking a picture
        // Note you can send an intent to the camera to take a picture...
        // So start by considering that!

        // Log.d("ListExample", "Made it to takePicture");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Add code here to handle results from both taking a picture or pulling
        // from the image gallery.
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Add here.
            person_image_view.setImageURI(file);
        }

    }

    // Add other methods as necessary here
    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo" );

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // retrieve path to picture just taken
        picture_id = mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg";

        return new File(picture_id);

    }

    public void toMapSelection(View view) {

        intent_to_map_selection = new Intent(this, SecondAddGameActivity.class);
        intent_to_map_selection.putExtra("yourChar", your_char);
        intent_to_map_selection.putExtra("oppName", opponent);
        intent_to_map_selection.putExtra("oppChar", opponent_char);
        intent_to_map_selection.putExtra("xLoc", x_coord);
        intent_to_map_selection.putExtra("yLoc", y_coord);
        intent_to_map_selection.putExtra("locName", location_name);
        intent_to_map_selection.putExtra("picture_id", picture_id);

        startActivity(intent_to_map_selection);

    }

}
