package edu.uci.ics.model;

import java.util.HashMap;

public class User {
    String userID;
    HashMap<String, Integer> visitedAps;

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
    }
}
