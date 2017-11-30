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
                for(DataSnapshot d : values){
                    Log.w("Locations", "\t\t" + d.getKey());

                    // Add the data into selectFragItem to display buttons
                    locations.add(d.getKey());

                    Iterable<DataSnapshot> children = d.getChildren();

                    for(DataSnapshot child : children){
                        Log.w("Child", "\t\t\t" + child.getKey());

                        Iterable<DataSnapshot> gChildren = child.getChildren();

                        for(DataSnapshot gChild : gChildren){
                            Log.w("gChild", "\t\t\t\t" + gChild.getKey());

                            Iterable<DataSnapshot> ggChildren = gChild.getChildren();

                            for(DataSnapshot ggChild: ggChildren){
                                Log.w("ggChild", "\t\t\t\t\t" + ggChild.getKey() +
                                        ": " + ggChild.getValue().toString());

                            }
                        }
                    }
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

        fastItem.setChecked(fastCheck);
        bruteItem.setChecked(bruteCheck);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }


}
