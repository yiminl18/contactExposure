package edu.uci.ics.model;

import java.sql.Timestamp;

import java.util.*;
public class Interval {
    Timestamp startTimeStamp;
    Timestamp endTimeStamp;
    String startAP;
    String endAP;
    String deviceID;
    String regionLabel;
    String roomLabel;
    Double roomConfidence;

    List<String> neighbors;//used for find candidate intervals

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }

    public Double getRoomConfidence() {
        return roomConfidence;
    }

    public void setRoomConfidence(Double roomConfidence) {
        this.roomConfidence = roomConfidence;
    }

    public String getRegionLabel() {
        return regionLabel;
    }

    public void setRegionLabel(String regionLabel) {
        this.regionLabel = regionLabel;
    }

    public String getRoomLabel() {
        return roomLabel;
    }

    public void setRoomLabel(String roomLabel) {
        this.roomLabel = roomLabel;
    }

    public String getStartAP() {
        return this.startAP;
    }

    public String getEndAP() {
        return this.endAP;
    }

    public void setStartAP(String startAP) {
        this.startAP = startAP;
    }

    public void setEndAP(String endAP) {
        this.endAP = endAP;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public Timestamp getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public void setStartTimeStamp(Timestamp startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Timestamp getEndTimeStamp() {
        return this.endTimeStamp;
    }

    public void setEndTimeStamp(Timestamp endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }
}
