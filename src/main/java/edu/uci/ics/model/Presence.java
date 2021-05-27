package edu.uci.ics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Presence {
    String userID;//mac or userID
    List<Interval> presence = new ArrayList<>();
    HashMap<String, Integer> visitedAps;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPresence(List<Interval> presence) {
        this.presence = presence;
    }

    public String getUserID() {
        return this.userID;
    }

    public void initPresence(){
        this.presence = new ArrayList<>();
    }

    public List<Interval> getPresence() {
        return this.presence;
    }

    public HashMap<String, Integer> getVisitedAPs() {
        return this.visitedAps;
    }

    public void setVisitedAps(HashMap<String, Integer> visitedAps){
        this.visitedAps = visitedAps;
    }
}
