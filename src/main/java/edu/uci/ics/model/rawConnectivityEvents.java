package edu.uci.ics.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class rawConnectivityEvents {
    String userID;
    List<rawConnectivityEvent> events;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<rawConnectivityEvent> getEvents() {
        return events;
    }

    public void setEvents(List<rawConnectivityEvent> events) {
        this.events = events;
    }

    public void initEvents(){
        this.events = new ArrayList<>();
    }
}
