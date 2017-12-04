package com.locationoptimizer;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Caffae on 2/12/17.
 */

class InfoPath implements Comparable<InfoPath>{
    double TimeTaken;

    double Cost;
    ArrayList<String> path;
    ArrayList<String> locationPath;
    ArrayList<String> transportPath;

    public InfoPath() {
        TimeTaken = -1;
        path = new ArrayList<>();
        locationPath = new ArrayList<>();
        transportPath = new ArrayList<>();
    }

    public InfoPath(ArrayList<String> lp) {
        TimeTaken = -1;
        path = new ArrayList<>();
        locationPath = lp;
        transportPath = new ArrayList<>();
    }

    public InfoPath(ArrayList<String> lp, ArrayList tp) {
        TimeTaken = -1;
        path = new ArrayList<>();
        locationPath = lp;
        transportPath = tp;
    }
    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    public double getTimeTaken() {
        return TimeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        TimeTaken = timeTaken;
    }

    public ArrayList<String> getPath() {
        path = new ArrayList<>();
        Log.d("ThePath", locationPath.toString());
        Log.d("ThePath", transportPath.toString());
        for (int i = 0; i < locationPath.size() + transportPath.size(); i++) {
            if (i % 2 == 0) {
                path.add(locationPath.get(i / 2));
                Log.d("AddingPath", locationPath.get(i / 2).toString());
            } else {
                path.add(transportPath.get((i - 1) / 2));
                Log.d("AddingPath", transportPath.get(i / 2).toString());
            }

        }
        return path;
    }

//    public void setPath(ArrayList<String> path) {
//        this.path = path;
//    }

    public void setLocationPath(ArrayList<String> locationPath) {
        this.locationPath = locationPath;
    }

    public ArrayList<String> getTransportPath() {
        return transportPath;
    }

    public void setTransportPath(ArrayList<String> transportPath) {
        this.transportPath = transportPath;
    }

    public void addPath(String a){
        path.add(a);
    }


    @Override
    public int compareTo(InfoPath a) {
        double b = this.getCost();
        double c = a.getCost();
        double myTime = this.getTimeTaken();
        double otherTime = a.getTimeTaken();

        if (myTime == otherTime){
            Log.d("SortFail", b + " Equals " + c);
            if(b == c){
                return 0;
            }else if(b <= c){
                return -1;
            }else{
                return 1;
            }
        } else if(myTime <= otherTime){
            Log.d("SortFail", b + " less than " + c);
            return -1;
        } else {
            Log.d("SortFail", b + " more than " + c);
            return 1;
        }


//        if (b == c){
//            Log.d("SortFail", b + " Equals " + c);
//            if(this.TimeTaken == a.getTimeTaken()){
//                return 0;
//            }else if(this.TimeTaken <= a.getTimeTaken()){
//                return -1;
//            }else{
//                return 1;
//            }
//        } else if(b <= c){
//            Log.d("SortFail", b + " less than " + c);
//            return -1;
//        } else {
//            Log.d("SortFail", b + " more than " + c);
//            return 1;
//        }
    }

}


