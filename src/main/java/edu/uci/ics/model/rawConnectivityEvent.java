package edu.uci.ics.model;

import java.sql.Timestamp;

public class rawConnectivityEvent {
    Timestamp timeStamp;
    String AP;

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAP() {
        return AP;
    }

    public void setAP(String AP) {
        this.AP = AP;
    }
}
