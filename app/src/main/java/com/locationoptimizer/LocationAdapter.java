package com.locationoptimizer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> implements Serializable {

    private ArrayList<String> locations;
    private Context myContext;
    public static ArrayList<String> selectedLocations = new ArrayList<>();

    public LocationAdapter(Context _context, ArrayList<String> _locations){
        // We omit MBS as it is the start and end location
        _locations.remove("Marina Bay Sands");
        locations = _locations;
        myContext = _context;
    }

    private Context getContext(){
        return myContext;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View locationView = inflater.inflate(R.layout.recycler_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(locationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final String location = locations.get(position);
        ToggleButton toggleButton = null;

        // Set item views based on your views and data model
        if(!location.equals("Marina Bay Sands")){
            final TextView textView = viewHolder.nameTextView;
            textView.setText(location);
            toggleButton = viewHolder.selectToggleButton;

            // Retrieve Previous Values
            if(!selectedLocations.isEmpty()){
                for(String l : selectedLocations){
                    if(l.equals(location)){
                        toggleButton.setChecked(true);
                        toggleButton.setBackgroundColor(myContext.getResources().getColor(R.color.select));
                    }
                }
            }

            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                // To add or remove values to the array to feed into selectedLocations array
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        compoundButton.setText(myContext.getResources().getText(R.string.recycle_select_on));
                        compoundButton.setBackgroundColor(myContext.getResources().getColor(R.color.select));
                        selectedLocations.add(location);


                    } else {
                        compoundButton.setBackgroundColor(myContext.getResources().getColor(R.color.unselect));
                        compoundButton.setText(myContext.getResources().getText(R.string.recycle_select_off));
                        selectedLocations.remove(location);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static ArrayList<String> getSelectedLocations(){
        return selectedLocations;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public ToggleButton selectToggleButton;

        public ViewHolder(View itemView){
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.recycler_text);
            selectToggleButton = (ToggleButton) itemView.findViewById(R.id.recycler_toggle_btn);
        }

    }

}
