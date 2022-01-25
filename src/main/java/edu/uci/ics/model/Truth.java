package edu.uci.ics.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/*
    this class is used to store ground truth data for point query
 */
public class Truth {
    String deviceID;
    Timestamp timestamp;
    String room;
    String region;
    String building;
    List<String> aps;

    public List<String> getAps() {
        return aps;
    }

    public void setAps(List<String> aps) {
        this.aps = aps;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
