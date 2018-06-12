package com.example.benfuhrman.meleestats;

/**
 * Created by Ben Fuhrman on 4/11/2018.
 */

import java.util.ArrayList;

public class MatchItem {
    private int mYourChar;
    private String mOppName;
    private int mOppChar;
    private double mXLoc;
    private double mYLoc;
    private int mStage;
    private int mDidWin;
    private int mId;

    public MatchItem(int yourChar, String oppName, int oppChar, double xLocation, double yLocation, int stage, int didWin, int id){
        mYourChar = yourChar;
        mOppName = oppName;
        mOppChar = oppChar;
        mXLoc = xLocation;
        mYLoc = yLocation;
        mStage = stage;
        mDidWin = didWin;
        mId = id;
    }

    public int getYourChar(){
        return mYourChar;
    }

    public String getOppName(){
        return mOppName;
    }

    public int getOppChar(){
        return mOppChar;
    }

    public double getXLocation(){
        return mXLoc;
    }

    public double getYLocation(){
        return mYLoc;
    }

    public int getStage(){
        return mStage;
    }

    public int getDidWin(){
        return mDidWin;
    }

    public int getId() { return mId; }

    private static int lastMatchItemId = 0;

    public static ArrayList<MatchItem> createMatchItemsList(int numMatchItems){
        ArrayList<MatchItem> matchItems = new ArrayList<MatchItem>();

        double[] tempLocations = {0.0, 23.0};

        matchItems.add(new MatchItem(1, "Evil Rush", 2, tempLocations[0], tempLocations[1], 3, 1, 1));
        matchItems.add(new MatchItem(2, "Mark", 2, tempLocations[0], tempLocations[1], 1, 0, 2));
        matchItems.add(new MatchItem(3, "Hbox", 2, tempLocations[0], tempLocations[1], 2, 1, 3));
        matchItems.add(new MatchItem(4, "Mango", 2, tempLocations[0], tempLocations[1],2, 1, 4));
        matchItems.add(new MatchItem(5, "PPMD", 2, tempLocations[0], tempLocations[1],4, 0, 5));

        return matchItems;
    }
}
