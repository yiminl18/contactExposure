package edu.uci.ics.model;

public class Query {
    String userID;
    String startTime;
    String endTime;
    int semanticMode;//1: summation; 2: continuous/max
    long timeThreshold;
    double confidenceThreshold;
    int contactMode; // 1: weak -> coarse level; 2: fine -> room level contact

    public int getContactMode() {
        return contactMode;
    }

    public void setContactMode(int contactMode) {
        this.contactMode = contactMode;
    }

    public long getTimeThreshold() {
        return timeThreshold;
    }

    public void setTimeThreshold(long timeThreshold) {
        this.timeThreshold = timeThreshold;
    }

    public double getConfidenceThreshold() {
        return confidenceThreshold;
    }

    public void setConfidenceThreshold(double confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
    }

    public int getSemanticMode() {
        return semanticMode;
    }

    public void setSemanticMode(int semanticMode) {
        this.semanticMode = semanticMode;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
