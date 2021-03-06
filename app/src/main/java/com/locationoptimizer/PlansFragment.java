package com.locationoptimizer;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class PlansFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private boolean settings;
    private Button searchBtn;
    private TextView budgetView;
    private TextView outputView;
    InfoPath current;
    InfoPath bestOp;




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
            Log.i("DM From", entry.getKey());
            Node n = entry.getValue();
            HashMap<String, ArrayList<Double>> nData = n.getAllData();
            for(String s : nData.keySet()){
                Log.i("DM To", s);
            }
        }

        // Selected Locations
        selected = LocationAdapter.getSelectedLocations();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if(selected.isEmpty()){
                    Toast.makeText(getContext(), "No location selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Please wait while we make a plan", Toast.LENGTH_SHORT).show();


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
                    budgetView.setText("Budget : $"+ String.format("%.2f", budget) + "  " + "Search: Brute");

                    bruteForceAlgo();

                    ArrayList<String> bestOption = bestOp.getPath();

                    String s = "";
                    String temp = "";
                    for(int i = 0; i < bestOption.size(); i++){
                        if (i%2 ==0){
                            temp = bestOption.get(i);
                            Log.d("BestOp", bestOption.get(i).toString());
                        }else{
                            if(i+1 < bestOption.size()) {
                                s += temp + " " + bestOption.get(i).toLowerCase() + " to " + bestOption.get(i + 1) + "\n\n";
                                Log.d("BestOp", bestOption.get(i).toString());
                            }
                        }
                    }


                    s += "\n\nCost: " + String.format("%.2f", bestOp.getCost()) + " SGD";
                    s += "\nTime taken: " + String.format("%.2f", bestOp.getTimeTaken()) + " minutes";

                    outputView.setText(s);


                } else if (controller.fetch().getInt(1) == 1 && controller.fetch().getInt(2) == 0){
                    // Fast Search
                    budgetView.setText("Budget : $" + String.format("%.2f", budget) + "  " + "Search: Fast");
                    String output = fastAlgo();
                    outputView.setText(output);
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

    public void bruteForceAlgo(){
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

        //TODO list of list of possible travel paths. calibrate for size
//            Set<List<String>> TransportOptions;
        Set<String> tpOption = ImmutableSet.of("Bus_Train", "Taxi", "Walk");

        Set<List<String>> listOfPosTransportCombo;
//            listOfPosTransportCombo = listOfPosTransportCombo



        int csize = locations.size();

        if(csize == 1){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption));
        }else if(csize == 2){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption));
        }else if(csize == 3){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption, tpOption));
        }else if(csize == 4){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption, tpOption, tpOption));
        }else if(csize == 5){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption, tpOption, tpOption, tpOption));
        }else if(csize == 6){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption, tpOption, tpOption, tpOption, tpOption));
        }else if(csize == 7){
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption, tpOption, tpOption, tpOption, tpOption, tpOption, tpOption, tpOption));
        }else{
            Toast.makeText(getContext(), "This shouldn't be possible, we don't have that many locations", Toast.LENGTH_SHORT);
            listOfPosTransportCombo = Sets.cartesianProduct(ImmutableList.of(tpOption,tpOption, tpOption, tpOption, tpOption, tpOption, tpOption, tpOption, tpOption));
        }

        for (List<String> val : orderperm) {
            Log.i("permu", val.toString());

            //TODO: get tine of 2+mbs & travel types
            ArrayList<String> myCurrentPermu = new ArrayList<>();
            myCurrentPermu.add("Marina Bay Sands");
            myCurrentPermu.addAll(val);
            myCurrentPermu.add("Marina Bay Sands");

            Log.i("permu", myCurrentPermu.toString());


            for (List<String> i : listOfPosTransportCombo){
                InfoPath current = new InfoPath(myCurrentPermu);
                ArrayList<String> holder = new ArrayList<>();
                for (String a : i){
                    holder.add(a);
                }
                current.setTransportPath(holder);
                calcTimeCost(current); //will be set
                allPossiblePaths.add(current);
            }


        }



//
        //checked if this is sorting in correct order or in reverse (once cost fetching is done)




        Log.d("CheckCostSorter", "FirstNow----------");
//        int countRemoved = 0;
//        for(int c = 0; c < allPossiblePaths.size()-countRemoved; c++){
//            InfoPath i = allPossiblePaths.get(c);
//            for(int b = 0; b < allPossiblePaths.size()-countRemoved; b++){
//                InfoPath a = allPossiblePaths.get(b);
//                if (a.getCost() >= i.getCost() && a.getTimeTaken() > i.getTimeTaken()){
//                    //a has a higher cost and takes more time so remove a
//                    allPossiblePaths.remove(a);
//                    countRemoved += 1;
//                }
//            }
//        }
//        Log.d("ArrayOut", allPossiblePaths.getoString());

//        Collections.sort(allPossiblePaths, Collections.reverseOrder());
        Collections.sort(allPossiblePaths);
        /// debug purpose





        // debug end

        settingBestOp();

    }

    private void settingBestOp() {


        for(InfoPath i: allPossiblePaths){
//            Log.d("CheckCostSorter", i.getCost() + " and Time: " + i.getTimeTaken());
            Log.d("CheckCostSorter", "Cost: "+ i.getCost() + " and Time: " + i.getTimeTaken());
            if (i.getCost() <= budget){
                bestOp = i;
//                double currentCost = bestOp.getCost();
                break;
            }
        }
    }

    private void calcTimeCost(InfoPath a) {
        ArrayList<String> path = a.getPath();
        double totalTime = 0;
        double totalCost = 0;
        for(int i = 0; i < path.size()-2; i++){
            if(i%2 == 0){
                String currentLocation = path.get(i);
                String modeOfTransport = path.get(i+1);
                String nextLocation = path.get(i+2);

                totalTime += timeOfTravel(currentLocation, modeOfTransport, nextLocation);
                totalCost += costOfTravel(currentLocation, modeOfTransport, nextLocation);
            }
        }
        a.setTimeTaken(totalTime);
        a.setCost(totalCost);
        
    }

    private double costOfTravel(String currentLocation, String modeOfTransport, String nextLocation) {
        Node a = dataMap.get(currentLocation);

        HashMap<String, ArrayList<Double>> b = a.getAllData();

        List<Double> nextLocationData = b.get(nextLocation);
        Log.d("HashDM", currentLocation);
        Log.d("HashDM", nextLocation);
        Log.d("HashDM", nextLocationData.toString());

        if (modeOfTransport.equals("Bus_Train")){
            return nextLocationData.get(0);
        }else if(modeOfTransport.equals("Taxi")){
            return nextLocationData.get(2);
        }
        return 0;
    }

    //          BusTrain  Taxi    Walking
    // A : {B:  [0.77,17, 5.3, 7, 0, 112]   ,
    //      C:  [                       ]   ,
    //      D:  [                       ]}
    //BusTraincost 0  BusTraintime 1 Taxicost 2 Taxitime 3 Walkingcost 4 Walkingtime 5

    private double timeOfTravel(String currentLocation, String modeOfTransport, String nextLocation) {
        Node a = dataMap.get(currentLocation);
//        String c = a.getToData(nextLocation);
        HashMap<String, ArrayList<Double>> b = a.getAllData();
//        String c = b.toString();
//        Log.d("HashDM", c);
        List<Double> nextLocationData = b.get(nextLocation);
        Log.d("HashDM", currentLocation);
        Log.d("HashDM", nextLocation);
        Log.d("HashDM", nextLocationData.toString());

        if (modeOfTransport.equals("Bus_Train")){
            return nextLocationData.get(1);
        }else if(modeOfTransport.equals("Taxi")){
            return nextLocationData.get(3);
        }else if(modeOfTransport.equals("Walk")){
            return nextLocationData.get(5);
        }
        return 0;
    }

    public Collection<List<String>> permutations (ArrayList<String> a) {
        List<String> vals = a;

        Collection<List<String>> orderPerm = Collections2.permutations(vals);

        return orderPerm;
    }


    /**
     * We use Simulated Annealing
     * Objective function min(Total_Time)
     * Constraint cost <= budget
     * P = exp(-df / T)
     *
     * Travel to all N locations subject to objective and constraint
     *
     * Configuration Settings:
     * This is the permutation of the cities from 1 to N,
     * given in all orders. We want to select the optimal one
     *
     * Rearrangement Settings:
     * We will replace sections of the path and replace with
     * random ones and retest if this is optimal or not by
     * calculating the total time.
     */
    public String fastAlgo(){
        String start = "Marina Bay Sands";
        String end = "Marina Bay Sands";
        ArrayList<String> inBetween = selected;

        String[] transportOption = {"Bus_Train", "Walk", "Taxi"};

        // Records
        String[] locationRecord = new String[inBetween.size() + 2];
        locationRecord[0] = start;
        locationRecord[inBetween.size() + 1] = end;

        String[] locationRecordTemp = new String[inBetween.size() + 2];      // for reverting
        locationRecord[0] = start;
        locationRecord[inBetween.size() + 1] = end;

        String[] transportRecord = new String[inBetween.size() + 1];
        String[] transportRecordTemp = new String[inBetween.size() + 1];    // for reverting


        // Simulated Annealing Variables to manually adjust till we get decent performance
        double t = 10000;   // Temperature
        double coolingRate = 0.005;
        int numberOfIterations = getNumIters(selected.size());

        // Initialize state by setting inBetween in its current order into the locationRecord
        for(int i = 0; i < locationRecord.length; i++){
            if(i != 0 && i != locationRecord.length - 1){
                // Don't modify ends as they are MBS and fixed
                locationRecord[i] = inBetween.get(i - 1);
            }
        }

        // clone into Temp
        locationRecordTemp = locationRecord.clone();

        // Initialize state by randomly insert transportOptions into transportRecord;
        for(int i = 0; i < transportRecord.length; i++){
            // Random generator to generate numbers between 0 and 2 for transportOption
            Random gen = new Random();
            int sel = gen.nextInt(3);
            transportRecord[i] = transportOption[sel];
        }

        // transportRecordTemp = transportRecord.clone();

        // Get currentTime and currentCost based on initial values
        double currentTime = getTotalTime(locationRecord, transportRecord);
        double currentCost = getTotalCost(locationRecord, transportRecord);
        Log.d("FastAlgo", "Current Time: " + Double.toString(currentTime));
        Log.d("FastAlgo", "Current Cost: " + Double.toString(currentCost));


        // Main simulation loop
        for(int i = 0; i < numberOfIterations; i++){

            // Scramble locationsRecordTemp
            Collections.shuffle(inBetween);
            for(int j = 0; j < locationRecordTemp.length; j++){
                if(j != 0 && j != locationRecordTemp.length - 1){
                    // Don't modify ends as they are MBS and fixed
                    locationRecordTemp[j] = inBetween.get(j - 1);
                }
            }

            // Scramble transportRecordTemp
            for(int k = 0; k < transportRecordTemp.length; k++){
                // Random generator to generate numbers between 0 and 2 for transportOption
                Random gen = new Random();
                int sel = gen.nextInt(3);
                transportRecordTemp[k] = transportOption[sel];
            }

            Log.d("fastloop", Arrays.toString(transportRecordTemp));
            Log.d("fastloop", Arrays.toString(locationRecordTemp));
            double tempTime = getTotalTime(locationRecordTemp, transportRecordTemp);
            double tempCost = getTotalCost(locationRecordTemp, transportRecordTemp);


            if(tempTime < currentTime && tempCost <= budget){
                // Update values
                currentTime = tempTime;
                currentCost = tempCost;
                locationRecord = Arrays.copyOf(locationRecordTemp, locationRecordTemp.length);
                transportRecord = Arrays.copyOf(transportRecordTemp, transportRecordTemp.length);
            } else if (Math.exp(currentTime - tempTime / t) < Math.random()  || tempCost > budget){
                continue;
            }

            Log.d("FastAlgo", "Current Time: " + Double.toString(currentTime));
            Log.d("FastAlgo", "Current Cost: " + Double.toString(currentCost));

            // reduce temperature
            t *= coolingRate;
        }

        // After the simulation is completed, hopefully we found a decent solution
        // We may now print out the string
        String s = "";

        if(currentCost > budget) {
            s = "Sorry, this trial found no solutions. Try Again!";
        } else {
            for (int i = 0; i < locationRecord.length; i++) {
                if ((i + 1) < locationRecord.length) {
                    s += locationRecord[i] + " " + transportRecord[i] + " to " + locationRecord[i + 1] + "\n\n";
                }
            }

            s += "\n\nCost: " + String.format("%.2f", currentCost) + " SGD";
            s += "\nTime taken: " + String.format("%.2f", currentTime) + " minutes";
        }
        return s;
    }

    public double getTotalTime(String[] locationRecord, String[] transportRecord){
        double output = 0;
        for(int i = 0; i < locationRecord.length; i++){
            if(i + 1 != locationRecord.length) {
                output = output + timeOfTravel(locationRecord[i], transportRecord[i], locationRecord[i + 1]);
                Log.d("getTotalTime", locationRecord[i] + " " + transportRecord[i] + " " + locationRecord[i + 1]);
                Log.d("getTotalTime", "Time : " + Double.toString(timeOfTravel(locationRecord[i], transportRecord[i], locationRecord[i + 1])));

            }
        }

        return output;
    }

    public double getTotalCost(String[] locationRecord, String[] transportRecord){
        double output = 0;
        for(int i = 0; i < locationRecord.length; i++){
            if(i + 1 != locationRecord.length) {
                output = output + costOfTravel(locationRecord[i], transportRecord[i], locationRecord[i + 1]);
                Log.d("getTotalCost", "Cost : " + Double.toString(costOfTravel(locationRecord[i], transportRecord[i], locationRecord[i + 1])));

            }
        }
        return output;
    }

    public int getNumIters(int length){
        if(length < 3){
            return 600;
        } else if (length >= 3 && length < 5){
            return 1000;
        } else if(length > 5){
            return length * 300 + 500;
        }

        return 0;
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
