package com.locationoptimizer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PlansFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private boolean settings;
    private Button searchBtn;
    private TextView budgetView;
    private TextView outputView;



    HashMap<String, Node> dataMap;

    HashMap<String, Node> data;
    ArrayList<String> selected;
    ArrayList<String> locations;
    ArrayList<InfoPath> allPossiblePaths;
    double budget;

    // Database
    private SettingSQLController controller = new SettingSQLController(getContext());

    public PlansFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_plans, container, false);

        searchBtn = (Button) rootview.findViewById(R.id.plan_search_button);
        outputView = (TextView) rootview.findViewById(R.id.plan_output);
        budgetView = (TextView) rootview.findViewById(R.id.plan_budget);

        // Location Data
        dataMap = MainActivity.getLocationDataList();

        // ToDo Parsing Data From MainActivity
        for(Map.Entry<String, Node> entry : dataMap.entrySet()){
            Log.i("From", entry.getKey());
            Node n = entry.getValue();
            HashMap<String, ArrayList<Double>> nData = n.getAllData();
            for(String s : nData.keySet()){
                Log.i("To", s);
            }
        }

        // Selected Locations
        selected = LocationAdapter.getSelectedLocations();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("sql", Integer.toString(controller.fetch().getInt(1)));
                Log.i("sql", Integer.toString(controller.fetch().getInt(2)));
                Log.i("sql", Double.toString(controller.fetch().getDouble(3)));

                // fetch budget
                budget = controller.fetch().getDouble(3);

                // fetch settings
                Set<String> selectedSet = new HashSet<>();
                selectedSet.addAll(selected);
                selectedSet.add("Marina Bay Sands"); // This is our start and end point

                // Check out count
                int count = selectedSet.size();

                if(controller.fetch().getInt(1) == 0 && controller.fetch().getInt(2) == 1){
                    // Brute Search
                    budgetView.setText("Budget : $"+Double.toString(budget) + "\t" + "Search: Brute");
                    // Selected locations
                    Node node = dataMap.get("Marina Bay Sands");

                    for(String entry : selectedSet){
                        ArrayList<Double> entryData = node.getToData(entry);

                        // Loop through the from locations.
                        int countLocation = 0;
//                        for(int i = 0; i < entryData.size(); i = i + 2){
//                            double cost = entryData.get(i);
//                            double time = entryData.get(i + 1);
//                        }
                    }

                    BruteForceAlgo();


                } else if (controller.fetch().getInt(1) == 1 && controller.fetch().getInt(2) == 0){
                    // Fast Search
                    budgetView.setText("Budget : $"+Double.toString(budget) + "\t" + "Search: Fast");

                }

            }
        });




        return rootview;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void BruteForceAlgo(){
        /** generate all possible paths.
         * set it as global pathsPossible */
        allPossiblePaths = new ArrayList<>();
        locations = LocationAdapter.getSelectedLocations();
        Log.i("Location number", String.valueOf(locations.size()) );
        int count = 0;
        for(String a : locations){
            count += 1;
            Log.i("location order: ", a + " " + count);
        }
        // TODO: get all permutations of locations and travel
        Collection<List<String>> orderperm = permutations(locations);

        for (List<String> val : orderperm) {
            Log.i("permu", val.toString());

            //TODO: get tine of 2+mbs & travel types




        }





        //TODO: add to permutation to arraylist & add the time too (if less than budget)



        //TODO: add MBS to arraylist (front and back) and add time for each mode.



    }

    public Collection<List<String>> permutations (ArrayList<String> a) {
        List<String> vals = a;

        Collection<List<String>> orderPerm = Collections2.permutations(vals);

        return orderPerm;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
