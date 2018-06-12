package com.example.benfuhrman.meleestats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben Fuhrman on 4/14/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "matches.db";
    public static final String TABLE_NAME = "matches_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "YOURCHAR";
    public static final String COL_3 = "OPPNAME";
    public static final String COL_4 = "OPPCHAR";
    public static final String COL_5 = "XLOC";
    public static final String COL_6 = "YLOC";
    public static final String COL_7 = "STAGE";
    public static final String COL_8 = "OUTCOME";
    // New columns
    public static final String COL_9 = "DATE";
    public static final String COL_10 = "LOCNAME";
    public static final String COL_11 = "ISTOURNAMENT";

    // picture
    public static final String COL_12 = "PICTURE_ID";

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Source from:
        // https://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " INTEGER, " +
                COL_3 + " TEXT, " +
                COL_4 + " INTEGER, " +
                COL_5 + " REAL, " +
                COL_6 + " REAL, " +
                COL_7 + " INTEGER, " +
                COL_8 + " INTEGER, " +
                // New columns
                COL_9 + " TEXT, " +
                COL_10 + " TEXT, " +
                COL_11 + " INTEGER, " +

                COL_12 + " TEXT)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void dropTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void clearAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public boolean insertEntry(int yourChar, String oppName, int oppChar, double xLoc, double yLoc, int stage, int outcome, String date, String locName, int isTournament, String picture_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // COL_1 is auto-incremented
        contentValues.put(COL_2, yourChar);
        contentValues.put(COL_3, oppName);
        contentValues.put(COL_4, oppChar);
        contentValues.put(COL_5, xLoc);
        contentValues.put(COL_6, yLoc);
        contentValues.put(COL_7, stage);
        contentValues.put(COL_8, outcome);
        // New columns
        contentValues.put(COL_9, date);
        contentValues.put(COL_10, locName);
        contentValues.put(COL_11, isTournament);

        // picture
        contentValues.put(COL_12, picture_id);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        } else {
            return true;
        }

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor indivData(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = new String[] {
                id
        };

        String queryString =
                "select * from " + TABLE_NAME + " where " + COL_1 + "=?";
        Cursor res = db.rawQuery(queryString, whereArgs);
        return res;

    }

    public Cursor newOpponent(String oppName){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = new String[] {
                oppName
        };

        String queryString =
                "select * from " + TABLE_NAME + " where " + COL_3 + "=?";
        Cursor res = db.rawQuery(queryString, whereArgs);
        return res;
    }

    public Cursor counterpickData(String oppName, String oppChar){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = new String[] {
                oppName,
                oppChar
        };

        String queryString =
                "select * from " + TABLE_NAME + " where " + COL_3 + "=? and " + COL_4 + "=?";
        Cursor res = db.rawQuery(queryString, whereArgs);
        return res;
    }

    public void removeEntry(String removeId){
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COL_1 + "=?";

        String[] whereArgs = new String[] {
                String.valueOf(removeId)
        };

        db.delete(TABLE_NAME, whereClause, whereArgs);
    }


    public Cursor searchEntries(String yourChar, String oppName, String oppChar, String locName, String isTournament){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        String[] whereArgs;

        List<String> parameters = new ArrayList<String>(Arrays.asList(yourChar, oppName, oppChar, locName, isTournament));
        List<String> columns = new ArrayList<String>(Arrays.asList(COL_2, COL_3, COL_4, COL_10, COL_11));
        for(int i = 0; i < parameters.size(); i++){

            String tmp = parameters.get(i);
            if(tmp == null) {
                parameters.remove(i);
                columns.remove(i);
                i -= 1;
            } else {
                tmp = tmp.trim();
                if (tmp.length() == 0 || tmp.equals("-1")) {
                    parameters.remove(i);
                    columns.remove(i);
                    i -= 1;
                }
            }
        }

        if(parameters.size() == 0){
            return db.rawQuery("select * from " + TABLE_NAME, null);
        }

        whereArgs = parameters.toArray(new String[parameters.size()]);

        String queryString = "select * from " + TABLE_NAME + " where ";
        for(int j = 0; j < columns.size(); j++) {
            if (j < columns.size() -1){
                queryString += columns.get(j) + "=? and ";
            } else {
                queryString += columns.get(j) + "=?";
            }

        }

        Log.d("QueryString", queryString);

        res = db.rawQuery(queryString, whereArgs);
        return res;
    }



}
