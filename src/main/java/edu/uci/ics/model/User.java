package edu.uci.ics.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    String userID;
    HashMap<String, Integer> visitedAps;//ap to count
    String frequentAP;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public HashMap<String, Integer> getVisitedAps() {
        return visitedAps;
    }

    public void setVisitedAps(HashMap<String, Integer> visitedAps) {
        this.visitedAps = visitedAps;
        int value = 0;
        String key = "null";
        for(Map.Entry<String, Integer> iter: visitedAps.entrySet()){
            if(iter.getValue()>value){
                value = iter.getValue();
                key = iter.getKey();
            }
        }
        this.frequentAP = key;
    }

    public String getFrequentAP(){
        return this.frequentAP;
    }
}
