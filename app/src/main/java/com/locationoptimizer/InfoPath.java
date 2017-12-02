package com.locationoptimizer;

import java.util.ArrayList;

/**
 * Created by Caffae on 2/12/17.
 */

class InfoPath {
    double TimeTaken;
    ArrayList<String> path;

    public InfoPath() {
        TimeTaken = -1;
        path = new ArrayList<>();
    }

    public double getTimeTaken() {
        return TimeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        TimeTaken = timeTaken;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

    public void addPath(String a){
        path.add(a);
    }
}
