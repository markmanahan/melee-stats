package com.example.benfuhrman.meleestats;

/**
 * Created by Ben Fuhrman on 4/11/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class MatchListAdapter extends
        RecyclerView.Adapter<MatchListAdapter.ViewHolder> {

    String charNames[] = {"Bowser", "C. Falcon", "DK", "Dr. Mario", "Falco", "Fox", "Ganondorf", "Ice Climbers", "Jigglypuff", "Kirby", "Link", "Luigi", "Mario", "Marth", "Mewtwo", "Mr. Game & Watch", "Ness", "Peach", "Pichu", "Pikachu", "Roy", "Samus", "Sheik", "Yoshi", "Young Link", "Zelda"};
    String stageNames[] = {"Battlefield", "DreamLand", "Yoshi's Story", "Final Destination", "Pokemon Stadium", "Fountain of Dreams"};

    private Activity previous_context;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView outcomeTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            // NEED TO CHANGE R.ID.CONTACT_NAME
            nameTextView = (TextView) itemView.findViewById(R.id.match_name);
            outcomeTextView = (TextView) itemView.findViewById(R.id.match_outcome);

        }
    }

    // Store a member variable for the matchitem
    private List<MatchItem> mMatchItems;
    // Store the context for easy access
    private Context mContext;

    // Pass in the match item array into the constructor
    public MatchListAdapter(Context context, List<MatchItem> matchItems) {
        mMatchItems = matchItems;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MatchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        previous_context = (Activity)  context;

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout

        // NEED TO CHANGE R.LAYOUT.ITEM_CONTACT
        View matchitemView = inflater.inflate(R.layout.item_match, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(matchitemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final MatchListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final MatchItem matchitem = mMatchItems.get(position);

        // Set item views based on your views and data model
        final TextView textView = viewHolder.nameTextView;
        TextView outcomeView = viewHolder.outcomeTextView;
        textView.setText(charNames[matchitem.getYourChar()] + " v. " + charNames[matchitem.getOppChar()] + " on " + stageNames[matchitem.getStage()]);
        if(matchitem.getDidWin() == 1){
            outcomeView.setText("W");
        } else {
            outcomeView.setText("L");
        }

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent specific_match = new Intent(view.getContext(), SpecificMatchActivity.class);
                String matchId = "" + matchitem.getId();
                specific_match.putExtra("id", matchId);
                previous_context.startActivityForResult(specific_match, 1);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMatchItems.size();
    }
}