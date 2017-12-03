package com.locationoptimizer;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // Firebase for location information
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference();

    // Local Settings
    public SettingSQLController controller;
    private MenuItem fastItem;
    private MenuItem bruteItem;
    private boolean fastCheck = true;
    private boolean bruteCheck = false;
    private double budget = 20;

    // Data Structure for the Map
    public static HashMap<String, Node> locationDataList = new HashMap<>();

    // To display in 1. Select
    ArrayList<String> locations = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_select:
                    SelectFragment select = new SelectFragment();
                    Bundle selectBundle = new Bundle();
                    selectBundle.putStringArrayList("locations", locations);
                    select.setArguments(selectBundle);
                    transaction.replace(R.id.content, select).commit();
                    return true;
                case R.id.navigation_mapMarker:
                        transaction.replace(R.id.content, new MapFragment()).commit();
                    return true;
                case R.id.navigation_itinerary:
                    transaction.replace(R.id.content, new PlansFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Settings database stuff
        controller = new SettingSQLController(this);
        controller.open();
        Cursor cursor = controller.fetch();
        controller.insert(fastCheck, bruteCheck, budget);


        myRef.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> values = dataSnapshot.getChildren();

                // From Location
                for(DataSnapshot d : values){
                    String fromLocation = d.getKey();
                    locations.add(fromLocation);        // Needed to display locations
                    Log.i("Locations", "\t" + fromLocation);
                    Iterable<DataSnapshot> children = d.getChildren();

                    Node tempNode = new Node();
                    // To Location
                    for(DataSnapshot child : children){
                        String toLocation = child.getKey();
                        Log.i("Child", "\t\t\t" + toLocation);
                        Iterable<DataSnapshot> gChildren = child.getChildren();
                        ArrayList<Double> valueArrayList = new ArrayList<>();

                        // Transport Type
                        for(DataSnapshot gChild : gChildren){
                            Log.i("gChild", "\t\t\t\t" + gChild.getKey());
                            Iterable<DataSnapshot> ggChildren = gChild.getChildren();

                            // Cost and Time
                            for(DataSnapshot ggChild: ggChildren){
                                Log.w("ggChild", "\t\t\t\t\t" + ggChild.getKey() + " : " + ggChild.getValue().toString());
                                if(ggChild.getValue() instanceof Long){
                                    valueArrayList.add((Double) ((Long) ggChild.getValue()).doubleValue());
                                } else {
                                    valueArrayList.add((Double) ggChild.getValue());
                                }
                            }
                            tempNode.add(toLocation, valueArrayList);
                        }
                    }

                    locationDataList.put(fromLocation, tempNode);
                }

                if(!locations.isEmpty()) {
                    // TODO locations array is empty at this point why is locations 0?
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    SelectFragment select = new SelectFragment();
                    Bundle selectBundle = new Bundle();
                    selectBundle.putStringArrayList("locations", locations);
                    Log.d("DEBUG", Integer.toString(locations.size()));
                    select.setArguments(selectBundle);
                    transaction.replace(R.id.content, select).commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase Warning", "Failed to read value.", error.toException());
            }
        });


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        fastItem = menu.findItem(R.id.fast_search_menu);
        bruteItem = menu.findItem(R.id.brute_search_menu);

        if(controller.fetch().isNull(3)) {
            fastItem.setChecked(fastCheck);
            bruteItem.setChecked(bruteCheck);
        } else {
            int a = controller.fetch().getInt(0);
            int b = controller.fetch().getInt(1);

            if(a == 1 && b == 0){
                fastCheck = true;
                bruteCheck = false;
                fastItem.setChecked(fastCheck);
                bruteItem.setChecked(bruteCheck);
            } else if (a == 0 && b == 1){
                fastCheck = false;
                bruteCheck = true;

                fastItem.setChecked(fastCheck);
                bruteItem.setChecked(bruteCheck);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fast_search_menu:
                fastCheck = !fastCheck;
                item.setChecked(fastCheck);

                bruteCheck = !bruteCheck;
                bruteItem.setChecked(bruteCheck);
                controller.update(1, fastCheck, bruteCheck, budget);
                return true;

            case R.id.brute_search_menu:
                fastCheck = !fastCheck;
                fastItem.setChecked(fastCheck);

                bruteCheck = !bruteCheck;
                item.setChecked(bruteCheck);
                controller.update(1, fastCheck, bruteCheck, budget);
                return true;

            case R.id.set_budget_menu:
                // Edit Text for user input
                final EditText userBudgetView = new EditText(this);
                userBudgetView.setText(Double.toString(budget));
                userBudgetView.setGravity(1);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set Desired Budget (SGD)");
                builder.setMessage("Note: Omit $ in input");
                builder.setView(userBudgetView);
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            double newUserBudget = Double.parseDouble(userBudgetView.getText().toString());
                            budget = newUserBudget;
                            controller.update(1, fastCheck, bruteCheck, budget);
                        } catch (NumberFormatException e){
                            Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                builder.show();
                return true;

            default:
                return false;
        }
    }


    public static HashMap<String, Node> getLocationDataList() {
        return locationDataList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }


}

class Node{
    //          BusTrain  Taxi    Walking
    // A : {B:  [0.77,17, 5.3, 7, 0, 112]   ,
    //      C:  [                       ]   ,
    //      D:  [                       ]}
    public HashMap<String, ArrayList<Double>> data;

    public Node(){
        data = new HashMap<String, ArrayList<Double>>();
    }

    public void add(String to, ArrayList<Double> values){
        data.put(to, values);
    }

    public ArrayList<Double> getToData(String to){
        return data.get(to);
    }

    public HashMap<String, ArrayList<Double>> getAllData(){
        return data;
    }
}